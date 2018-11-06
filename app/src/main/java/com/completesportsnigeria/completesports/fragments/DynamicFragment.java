package com.completesportsnigeria.completesports.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.completesportsnigeria.completesports.R;
import com.completesportsnigeria.completesports.classes.CustomWebView;
import com.completesportsnigeria.completesports.classes.DetectConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import butterknife.ButterKnife;


public class DynamicFragment extends Fragment {


    View view;
    ProgressBar progressBar1;
    LinearLayout internetNotAvailable;
    SwipeRefreshLayout swipeRefreshLayout;
    WebView simpleWebView;
    private Button btnRetry;

    String main_url = "";

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            if (!DetectConnection.checkInternetConnection(getActivity())) {
                internetNotAvailable.setVisibility(View.VISIBLE);
                simpleWebView.setVisibility(View.GONE);
                // cancel the Visual indication of a refresh
                swipeRefreshLayout.setRefreshing(false);
            } else {
                internetNotAvailable.setVisibility(View.GONE);
                simpleWebView.setVisibility(View.VISIBLE);
                if (simpleWebView.getUrl() != null && !TextUtils.isEmpty(simpleWebView.getUrl())) {
//                    loadWebPage(simpleWebView.getUrl());
                    Log.d("***Check 1", "onRefresh: " + simpleWebView.getUrl());
                    MyAsynTask myAsynTask = new MyAsynTask(simpleWebView.getUrl());
                    myAsynTask.execute();
                } else {
//                    loadWebPage(getArguments().getString("url"));
                    Log.d("***Check 2", "onRefresh: " + getArguments().getString("url"));
                    MyAsynTask myAsynTask = new MyAsynTask(getArguments().getString("url"));
                    myAsynTask.execute();
                }

            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.nested_dynamic_view, container, false);
        ButterKnife.bind(this, view);
        //...........................
        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        }

        progressBar1 = (ProgressBar) view.findViewById(R.id.news_detail_progressBar1);
        simpleWebView = (WebView) view.findViewById(R.id.news_detail_simpleWebView);
        internetNotAvailable = (LinearLayout) view.findViewById(R.id.news_detail_internetNotAvailable);

        simpleWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.getSettings().setAllowFileAccess(true);
        simpleWebView.setWebChromeClient(new WebChromeClient());


        Bundle bundle = getArguments();
        String url = bundle.getString("url");
        main_url = url;
        String title = bundle.getString("title");

        if (!DetectConnection.checkInternetConnection(getActivity())) {
            internetNotAvailable.setVisibility(View.VISIBLE);
            simpleWebView.setVisibility(View.GONE);
        } else {
            internetNotAvailable.setVisibility(View.GONE);
            simpleWebView.setVisibility(View.VISIBLE);
//            loadWebPage(url);
            MyAsynTask myAsynTask = new MyAsynTask(url);
            myAsynTask.execute();

        }

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.news_detail_swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (!DetectConnection.checkInternetConnection(getActivity())) {
                    internetNotAvailable.setVisibility(View.VISIBLE);
                    simpleWebView.setVisibility(View.GONE);
                    // cancel the Visual indication of a refresh
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    internetNotAvailable.setVisibility(View.GONE);
                    simpleWebView.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);

//                    loadWebPage(simpleWebView.getUrl());
                    Log.d("*** Check 3", "onRefresh: " + simpleWebView.getUrl());
                    MyAsynTask myAsynTask = new MyAsynTask(main_url);
                    myAsynTask.execute();
                }

            }
        });

        btnRetry = (Button) view.findViewById(R.id.btn_retry);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                internetNotAvailable.setVisibility(View.GONE);
                simpleWebView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                onRefreshListener.onRefresh();
            }
        });

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/*");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Complete Sports");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this News: " + main_url);
                startActivity(Intent.createChooser(shareIntent, "Share Using"));
            }
        });

        return view;
    }

    private class MyAsynTask extends AsyncTask<Void, Void, Document> {

        String url = "";

        public MyAsynTask(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar1.setVisibility(View.VISIBLE);
        }

        @Override
        protected Document doInBackground(Void... voids) {

            Document document = null;
            try {
                document = Jsoup.connect(url).get();
                document.getElementsByClass("main-navigation").remove();
                document.getElementsByClass("custom-header-image").remove();
                document.getElementsByClass("header-bg header-layout-default").remove();
                document.getElementsByClass("post-breadcrumb").remove();
                document.getElementsByClass("at-share-btn-elements").remove();

                document.getElementsByClass("widget widget_socialcountplus").remove();
                document.getElementsByClass("post-footer").remove();
                document.getElementsByClass("blog-pager").remove();
                document.getElementsByClass("wordpress-comments-inner comments-inner").remove();
                document.getElementsByClass("section main-sidebar sticky-inside").remove();

                document.getElementsByClass("usz0KuhH").remove();

                document.getElementsByAttributeValue("target", "_blank").first().remove();
                document.getElementsByAttributeValue("target", "_blank").first().remove();

                document.getElementsByClass("post-bot-media post-bot-ads").remove();
                document.getElementsByClass("section footer-wide").remove();
//                document.getElementById("adContent").remove();

                document.getElementById("comments").remove();
                document.getElementsByClass("facebook-comments-title comments-title comments-title-tab").remove();
                document.getElementsByClass("social-count-plus").remove();
                document.getElementsByClass("footer-inner Shad").remove();


            } catch (IOException e) {
                e.printStackTrace();
            }
            return document;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
//          try{

            simpleWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
            simpleWebView.getSettings().setJavaScriptEnabled(true);
            simpleWebView.getSettings().setAllowFileAccess(true);
            simpleWebView.setWebChromeClient(new WebChromeClient());

            simpleWebView.loadDataWithBaseURL(url, document.toString(), "text/html", "utf-8", "");
            simpleWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);


            simpleWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

//                          // Otherwise allow the OS to handle it
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
//                      }

                    Toast toast =  Toast.makeText(getContext(), "Click Back Button to Close Dialog", Toast.LENGTH_SHORT);
                    View toastView = toast.getView();

                    /* And now you can get the TextView of the default View of the Toast. */
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.WHITE);
//                    toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_fly, 0, 0, 0);
//                    toastMessage.setGravity(Gravity.CENTER);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//                    toastMessage.setCompoundDrawablePadding(16);
                    toastView.setBackgroundColor(Color.RED);
                    toast.show();

                    popUpDialog(url);
//                      view.loadUrl(url);
//                    return super.shouldOverrideUrlLoading(view, url);
                    return true;
                }

            });


            progressBar1.setVisibility(View.GONE);

        }
    }

    private void popUpDialog(String url) {
        Bundle args = new Bundle();
        args.putString("url", url);

        FragmentActivity activity = (FragmentActivity) (getContext());
        FragmentManager fm = activity.getSupportFragmentManager();

        ShowDynamicDialog showDynamicDialog = new ShowDynamicDialog();
        showDynamicDialog.setArguments(args);

        showDynamicDialog.show(fm, "dialog");
    }

    // The Fragment that shows the Dialog
    public static class ShowDynamicDialog extends DialogFragment {

        CustomWebView webView;
        LinearLayout internetNotAvailable;
        ProgressBar progressBar1;

        String url;

        public ShowDynamicDialog() {

        }

        public static ShowDynamicDialog newInstance() {
            ShowDynamicDialog f = new ShowDynamicDialog();
            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Bundle mArgs = getArguments();
            url = mArgs.getString("url");

            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogStyle);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_web_view, container);
            webView = (CustomWebView) view.findViewById(R.id.news_detail_simpleWebView);
            internetNotAvailable = (LinearLayout)view.findViewById(R.id.news_detail_internetNotAvailable);
            progressBar1 = (ProgressBar) view.findViewById(R.id.news_detail_progressBar1);

            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setAllowFileAccess(true);
            webView.setWebChromeClient(new WebChromeClient());

            if (!DetectConnection.checkInternetConnection(getActivity())) {
                internetNotAvailable.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
            }else{
                internetNotAvailable.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                //            new SerchRelatedNews(document,webView,url).execute();

                if (url.startsWith("https://www.completesportsnigeria.com/") || url.startsWith("http://www.completesportsnigeria.com/")){
                    SerchRelatedNews serchRelatedNews = new SerchRelatedNews(webView,url, progressBar1);
                    serchRelatedNews.execute();
                }else{
                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                        url = "http://" + url;

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }

            }

            return view;
        }

    }

    private static class SerchRelatedNews extends AsyncTask<Void, Void, Void> {
        Document document;
        CustomWebView view;
        ProgressBar progressBar;
        String inner_url;

        public SerchRelatedNews(CustomWebView view, String inner_url, ProgressBar progressBar) {
            this.view = view;
            this.inner_url = inner_url;
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                document = Jsoup.connect(inner_url).get();

                document.getElementsByClass("main-navigation").remove();
                document.getElementsByClass("custom-header-image").remove();
                document.getElementsByClass("header-bg header-layout-default").remove();
                document.getElementsByClass("post-breadcrumb").remove();
                document.getElementsByClass("at-share-btn-elements").remove();

                document.getElementsByClass("section main-sidebar sticky-inside").remove();
                document.getElementsByClass("wordpress-comments-inner comments-inner").remove();
                document.getElementsByClass("post-footer").remove();

                document.getElementsByClass("widget widget_socialcountplus").remove();
                document.getElementsByClass("trc_rbox thumbnails-a trc-content-sponsored").remove();
                document.getElementsByClass("post-labels post-section").remove();
                document.getElementsByClass("wordpress-comments comments active").remove();
                document.getElementsByClass("post-section-title comments-title-tabs-name comments-title-tab").remove();
                document.getElementsByClass("wordpress-comments-title comments-title active comments-title-tab").remove();

                document.getElementsByClass("usz0KuhH").remove();

                document.getElementsByAttributeValue("target", "_blank").first().remove();
                document.getElementsByAttributeValue("target", "_blank").first().remove();

                document.getElementsByClass("post-bot-media post-bot-ads").remove();
                document.getElementsByClass("section footer-wide").remove();

                document.getElementsByClass("facebook-comments-title comments-title comments-title-tab").remove();
                document.getElementsByClass("social-count-plus").remove();
                document.getElementsByClass("footer-inner Shad").remove();
                document.getElementsByClass("yarpp-related").remove();
                document.getElementsByClass("blog-pager").remove();
                document.getElementById("comments").remove();


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            view.getSettings().setPluginState(WebSettings.PluginState.ON);
            view.getSettings().setJavaScriptEnabled(true);
            view.getSettings().setAllowFileAccess(true);
            view.setWebChromeClient(new WebChromeClient());

            view.loadDataWithBaseURL(inner_url, document.toString(), "text/html", "utf-8", "");
            view.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}
