package com.completesportsnigeria.completesports.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.completesportsnigeria.completesports.R;
import com.completesportsnigeria.completesports.activity.NewsDetailActivity;
import com.completesportsnigeria.completesports.classes.DetectConnection;
import com.completesportsnigeria.completesports.classes.ReadRss;

import butterknife.ButterKnife;


public class EachMenuFragment extends Fragment {

    public static String menu_title = "";

    RecyclerView recyclerView;
    View view;
    ProgressBar progressBar1;
    LinearLayout internetNotAvailable;
    SwipeRefreshLayout swipeRefreshLayout;
    private Button btnRetry;
    String address;

    public EachMenuFragment() {
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
                if (swipeRefreshLayout != null){
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
        view = inflater.inflate(R.layout.fragment_each_menu, container, false);
        ButterKnife.bind(this, view);
        //...........................
        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        }

        progressBar1 = (ProgressBar) view.findViewById(R.id.each_menu_view_progressBar1);
        recyclerView = (RecyclerView) view.findViewById(R.id.each_menu_view_recyclerview);
        internetNotAvailable = (LinearLayout) view.findViewById(R.id.each_menu_view_internetNotAvailable);

//        Config config = new Config();
        Bundle args = getArguments();
        menu_title = args.getString("title");
        this.address = args.getString("url");

        if (!DetectConnection.checkInternetConnection(getActivity())) {
            internetNotAvailable.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            internetNotAvailable.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            if (swipeRefreshLayout != null){
                swipeRefreshLayout.setRefreshing(false);
            }
            generateFeed(getContext(), recyclerView, address);
        }

//        NewsDetailActivity.titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
//        NewsDetailActivity.titleView.setText(menu_title);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(menu_title);
//        NewsDetailActivity.toolbar.setTitle(menu_title);
        NewsDetailActivity.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/*");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Complete Sports");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check this page: " + address);
                startActivity(Intent.createChooser(shareIntent, "Share Using"));
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.each_menu_view_swipeRefreshLayout);
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
                    if (swipeRefreshLayout != null){
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    generateFeed(getContext(), recyclerView, address);
                }

            }
        });

        btnRetry = (Button) view.findViewById(R.id.each_menu_view_btn_retry);
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
        String name = "menu";
        //Call Read rss asyntask to fetch rss
        ReadRss readRss = new ReadRss(cnt, rclView, url, name, progressBar1);
        readRss.execute();
    }

}
