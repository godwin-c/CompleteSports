package com.completesportsnigeria.completesports.classes;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.completesportsnigeria.completesports.adapter.FeedsAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class ReadRss extends AsyncTask<Void, Void, Void> {
    Context context;
    //    String address = "http://www.sciencemag.org/rss/news_current.xml";
    String address;
    String filename;
    public ProgressBar progressBar;
    ArrayList<FeedItem> feedItems;
    RecyclerView recyclerView;

    URL url;

    public ReadRss(Context context, RecyclerView recyclerView, String address, String filename, ProgressBar progressBar) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.address = address;
        this.filename = filename;
        this.progressBar = progressBar;

    }

    //before fetching of rss statrs show progress to user
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressBar != null || !progressBar.isShown()) {
            progressBar.setVisibility(View.VISIBLE);;
        }
    }


    //This method will execute in background so in this method download rss feeds
    @Override
    protected Void doInBackground(Void... params) {
        //call process xml method to process document we downloaded from getData() method
        ProcessXml(Getdata());

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if ((feedItems == null) || (feedItems.size() < 1)) {
            Toast.makeText(context, "Please check your internet Connection", Toast.LENGTH_SHORT).show();
        } else {

            SaveToStorage saveToStorage = new SaveToStorage(this.filename,feedItems);
            saveToStorage.execute();

            FeedsAdapter adapter;
            if (recyclerView.getAdapter() == null || recyclerView.getAdapter().getItemCount() < 1){
                adapter = new FeedsAdapter(context, feedItems);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.addItemDecoration(new VerticalSpace(20));
                recyclerView.setAdapter(adapter);
            }else {
                adapter = (FeedsAdapter)recyclerView.getAdapter();
                adapter.setItems(feedItems);
                adapter.notifyDataSetChanged();
            }
        }

        progressBar.setVisibility(View.INVISIBLE);;
    }

    // In this method we will process Rss feed  document we downloaded to parse useful information from it
    private void ProcessXml(Document data) {
        if (data != null) {
            //Store the data here
//            String info = String.valueOf(data);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer newTransformer = null;
            StringWriter sw = new StringWriter();

            try {
                newTransformer = transformerFactory.newTransformer();
                newTransformer.transform(new DOMSource(data), new StreamResult(sw));
            } catch (Exception e) {

            }

            feedItems = new ArrayList<>();
            Element root = data.getDocumentElement();
            Node channel = root.getChildNodes().item(1);
            NodeList items = channel.getChildNodes();
            for (int i = 0; i < items.getLength(); i++) {
                Node cureentchild = items.item(i);
                if (cureentchild.getNodeName().equalsIgnoreCase("item")) {
                    FeedItem item = new FeedItem();
                    NodeList itemchilds = cureentchild.getChildNodes();
                    for (int j = 0; j < itemchilds.getLength(); j++) {
                        Node cureent = itemchilds.item(j);
                        if (cureent.getNodeName().equalsIgnoreCase("title")) {
                            item.setTitle(cureent.getTextContent());
                        } else if (cureent.getNodeName().equalsIgnoreCase("description")) {

                            if (cureent.hasChildNodes()) {
                                NodeList test = cureent.getChildNodes();

//                                Log.d("* NodeList *", "ProcessXml: " + test.item(0).getTextContent());
                                String s = test.item(0).getTextContent();
                                final Pattern pattern = Pattern.compile("<div>(.+?)</div>");
                                final Matcher matcher = pattern.matcher(s);
                                matcher.find();

                                try {
                                    Element node = DocumentBuilderFactory
                                            .newInstance()
                                            .newDocumentBuilder()
                                            .parse(new ByteArrayInputStream(matcher.group(1).getBytes()))
                                            .getDocumentElement();

                                    Log.d("* NodeList *", "ProcessXml222: " + node.getAttributes().item(2).getTextContent());
                                    item.setThumbnailUrl(node.getAttributes().item(2).getTextContent());
                                } catch (Exception e) {

                                }

//                                NodeList test = cureent.getChildNodes();
                                String descText = test.item(0).getTextContent();
                                final Pattern pattern_desc = Pattern.compile("<p>(.+?)</p>");
                                final Matcher matcher_desc = pattern_desc.matcher(descText);
                                matcher_desc.find();

                                item.setDescription(matcher_desc.group(1));
                                Log.d("* NodeList *", "ProcessXml555: " + matcher_desc.group(1));

                            }
                        } else if (cureent.getNodeName().equalsIgnoreCase("pubDate")) {
                            item.setPubDate(cureent.getTextContent());
                        } else if (cureent.getNodeName().equalsIgnoreCase("link")) {
                            item.setLink(cureent.getTextContent());
                        } else if (cureent.getNodeName().equalsIgnoreCase("description")) {
                            //this will return us thumbnail url
//                            String url = cureent.getAttributes().item(0).getTextContent();
//                            item.setThumbnailUrl(url);


                        }
                    }
                    feedItems.add(item);


                }
            }
        }


    }

    private void saveToFile(String filename, ArrayList<FeedItem> data) {
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);

//            OutputStreamWriter outputWriter = new OutputStreamWriter(fos);
//            outputWriter.write(data);
//            outputWriter.close();

            objectOutputStream.writeObject(data);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SaveToStorage extends AsyncTask<ArrayList<FeedItem>, Void, Boolean>{
        String address;
        ArrayList<FeedItem> data;

        public SaveToStorage(String address, ArrayList<FeedItem> data){
            this.address = address;
            this.data = data;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(ArrayList<FeedItem>... voids) {
            try {
                FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);

                objectOutputStream.writeObject(data);
                objectOutputStream.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }

    //This method will download rss feed document from specified url
    public Document Getdata() {
        try {
            url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDoc = builder.parse(inputStream);
            return xmlDoc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
