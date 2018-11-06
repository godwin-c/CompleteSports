package com.completesportsnigeria.completesports.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.completesportsnigeria.completesports.R;
import com.completesportsnigeria.completesports.adapter.FeedsAdapter;
import com.completesportsnigeria.completesports.classes.Config;
import com.completesportsnigeria.completesports.classes.DetectConnection;
import com.completesportsnigeria.completesports.classes.FeedItem;
import com.completesportsnigeria.completesports.classes.ReadRss;
import com.completesportsnigeria.completesports.classes.VerticalSpace;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import butterknife.ButterKnife;


public class News extends Fragment {

    public final static String TITLE = "News";

    public final static String STORAGE_NAME = "news_store";

    RecyclerView recyclerView;
    View view;
    ProgressBar progressBar1;
    LinearLayout internetNotAvailable;
    SwipeRefreshLayout swipeRefreshLayout;
    private Button btnRetry;
    String address;

    ArrayList<FeedItem> feedItems;

    public News() {
        // Required empty public constructor
    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            if (!DetectConnection.checkInternetConnection(getActivity())) {
                internetNotAvailable.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                // cancel the Visual indication of a refresh
                swipeRefreshLayout.setRefreshing(false);
            } else {
                internetNotAvailable.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                generateFeed(getContext(), recyclerView, address);

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
        view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        //...........................
        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        }

        progressBar1 = (ProgressBar) view.findViewById(R.id.news_view_progressBar1);
        recyclerView = (RecyclerView) view.findViewById(R.id.news_view_recyclerview);
        internetNotAvailable = (LinearLayout) view.findViewById(R.id.news_view_internetNotAvailable);

        Config config = new Config();
        this.address = config.news_url;

//        HomeActivity.progressBar.setVisibility(View.VISIBLE);

        if (!fileExist(STORAGE_NAME)) {
            if (!DetectConnection.checkInternetConnection(getActivity())) {
                internetNotAvailable.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                internetNotAvailable.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                generateFeed(getContext(), recyclerView, address);
            }
        } else {
            internetNotAvailable.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            ReadStorage readStorage = new ReadStorage(STORAGE_NAME, progressBar1);
            readStorage.execute();

        }

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.news_view_swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (!DetectConnection.checkInternetConnection(getActivity())) {
                    internetNotAvailable.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    // cancel the Visual indication of a refresh
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    internetNotAvailable.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    generateFeed(getContext(), recyclerView, address);
                }

            }
        });
//        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);

        btnRetry = (Button) view.findViewById(R.id.news_view_btn_retry);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                internetNotAvailable.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                swipeRefreshLayout.setRefreshing(true);
                onRefreshListener.onRefresh();
            }
        });


        return view;

    }

    private void generateFeed(Context cnt, RecyclerView rclView, String url) {
        //Call Read rss asyntask to fetch rss
        ReadRss readRss = new ReadRss(cnt, rclView, url, STORAGE_NAME, progressBar1);
        readRss.execute();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
//        savedInstanceState.putInt("position", recyclerView.getChildAdapterPosition(view)); // get current recycle view position here.
//        super.onSaveInstanceState(savedInstanceState);
    }

    public boolean fileExist(String fname) {
        File file = getContext().getFileStreamPath(fname);
        return file.exists();
    }

    private ArrayList<FeedItem> readFromStorage(String address) {

        ArrayList<FeedItem> storedData = null;
        FileInputStream fis;
        try {
            fis = getActivity().openFileInput(address);
            ObjectInputStream ois = new ObjectInputStream(fis);
            storedData = (ArrayList<FeedItem>) ois.readObject();

        } catch (Exception e) {
            Log.d("Read Store News", String.valueOf(e));
            // reply = false;
        }

        return storedData;
    }

    private class ReadStorage extends AsyncTask<Void, Void, Void> {
        String address;
        private ProgressBar progressBar;

        public ReadStorage(String address, ProgressBar progressBar){
            this.address = address;
            this.progressBar = progressBar;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressBar != null || !progressBar.isShown()) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            feedItems = readFromStorage(address);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.INVISIBLE);;

            if (feedItems != null && feedItems.size() > 0){

                FeedsAdapter adapter = new FeedsAdapter(getContext(), feedItems);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.addItemDecoration(new VerticalSpace(20));
                recyclerView.setAdapter(adapter);
            }else {
                Toast.makeText(getContext(),"Error has been encountered, please try again",Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public String toString() {
        return TITLE;
    }
}
