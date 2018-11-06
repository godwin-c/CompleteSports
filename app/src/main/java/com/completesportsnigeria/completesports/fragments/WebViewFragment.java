package com.completesportsnigeria.completesports.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.completesportsnigeria.completesports.R;
import com.completesportsnigeria.completesports.activity.NewsDetailActivity;
import com.completesportsnigeria.completesports.classes.CustomWebView;
import com.completesportsnigeria.completesports.classes.DetectConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import butterknife.ButterKnife;

public class WebViewFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_web_view, container, false);
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

//        NewsDetailActivity.titleView.setText(title);
//        NewsDetailActivity.customView.setVisibility(View.GONE);
        NewsDetailActivity.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/*");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Complete Sports");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check this page: " + simpleWebView.getUrl());
                startActivity(Intent.createChooser(shareIntent, "Share Using"));
            }
        });
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

        return view;
    }

//    private void loadWebPage(String url) {
//        simpleWebView.getSettings().setSupportMultipleWindows(true);
//        simpleWebView.getSettings().setJavaScriptEnabled(true); // enable javascript
//        simpleWebView.getSettings().setLoadWithOverviewMode(true);
//        simpleWebView.getSettings().setUseWideViewPort(true);
//        simpleWebView.getSettings().setBuiltInZoomControls(true);
//
//        simpleWebView.getSettings().setAllowFileAccess(true);
//
//        simpleWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        simpleWebView.getSettings().setDisplayZoomControls(false);
//        simpleWebView.setWebViewClient(new MyWebViewClient());
//
//
//        try {
//            simpleWebView.loadUrl(url);
//        } catch (Exception e) {
//            internetNotAvailable.setVisibility(View.VISIBLE);
//            simpleWebView.setVisibility(View.GONE);
//        }
//        simpleWebView.setWebChromeClient(new WebChromeClient() {
//
//            @Override
//            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, android.os.Message resultMsg) {
//                WebView.HitTestResult result = view.getHitTestResult();
//                try {
//                    String data = result.getExtra();
//                    Context context = view.getContext();
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
//                    browserIntent.setData(Uri.parse(data));
//                    getActivity().startActivity(browserIntent);
//                } catch (Exception e) {
//                    Toast.makeText(getActivity(), "Please try again.", Toast.LENGTH_LONG).show();
//                }
//
//
//                return false;
//            }
//
//
//        });
//
//
//        simpleWebView.setOnKeyListener(new View.OnKeyListener() {
//
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK
//                        && event.getAction() == MotionEvent.ACTION_UP
//                        && simpleWebView.canGoBack()) {
//                    if (simpleWebView.canGoBack()) {
//                        simpleWebView.goBack();
//                    }
//                    return true;
//                }
//
//                return false;
//            }
//
//        });
//
//
//    }

//    private class MyWebViewClient extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//            Log.d("Clicked URL", url.toString());
//            if (URLUtil.isNetworkUrl(url)) {
//                if (url.equalsIgnoreCase("https://www.android.com/") || url.equalsIgnoreCase("https://play.google.com/store/") || url.equalsIgnoreCase("http://abhiandroid.com/sourcecode/webview/") || url.equalsIgnoreCase("https://stephenlegal.com.ng/")) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
//                    return true;
//                } else if (url.endsWith(".mp3") || url.endsWith(".pdf")) {
//                    Log.d("Download Code", "Download Start");
//                    if (isReadStorageAllowed()) {
//                        Uri source = Uri.parse(url);
//                        // Make a new request pointing to the .mp3 url
//                        DownloadManager.Request request = new DownloadManager.Request(source);
//                        // appears the same in Notification bar while downloading
//                        request.setDescription("Description for the DownloadManager Bar");
//                        request.setTitle(url.substring(url.lastIndexOf("/") + 1));
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                            request.allowScanningByMediaScanner();
//                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                        }
//                        // save the file in the "Downloads" folder of SDCARD
//                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url.substring(url.lastIndexOf("/") + 1));
//                        // get download service and enqueue file
//                        DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
//                        manager.enqueue(request);
//                        Toast.makeText(getActivity(), "Downloading Started...", Toast.LENGTH_LONG).show();
//                    } else {
//                        requestStoragePermission();
//                    }
//                    return true;
//                }
//                return false;
//            }
//
//            // Otherwise allow the OS to handle it
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(intent);
//            return true;
//
//        }
//
//        @Override
//        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//            Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            progressBar1.setVisibility(View.VISIBLE);
//        }
//
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            progressBar1.setVisibility(View.GONE);
//            // cancel the Visual indication of a refresh
//            swipeRefreshLayout.setRefreshing(false);
//        }
//
//        @Override
//        public void onLoadResource(WebView view, String url) {
//
//            try {
//                view.loadUrl("javascript:(window.onload = function() { " +
//                        "(elem1 = document.getElementById('id1')); elem.parentNode.removeChild(elem1); " +
//                        "(elem2 = document.getElementById('id2')); elem2.parentNode.removeChild(elem2); " +
//                        "})()");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

//    private boolean isReadStorageAllowed() {
//        //Getting the permission status
//        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
//        int result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        //If permission is granted returning true
//        if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED)
//            return true;
//
//        //If permission is not granted returning false
//        return false;
//    }
//
//    //Requesting permission
//    private void requestStoragePermission() {
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            //If the user has denied the permission previously your code will come to this block
//            //Here you can explain why you need this permission
//            //Explain here why you need this permission
//        }
//
//        //And finally ask for the permission
//        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//    }
//
//    //This method will be called when the user will tap on allow or deny
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        //Checking the request code of our request
//        if (requestCode == 1) {
//
//            //If permission is granted
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//
//            } else {
//                //Displaying another toast if permission is not granted
//                Toast.makeText(getActivity(), "Oops, you just denied the permission", Toast.LENGTH_LONG).show();
//            }
//        }
//    }

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

            simpleWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
            simpleWebView.getSettings().setJavaScriptEnabled(true);
            simpleWebView.getSettings().setAllowFileAccess(true);
            simpleWebView.setWebChromeClient(new WebChromeClient());

            simpleWebView.loadDataWithBaseURL(url, document.toString(), "text/html", "utf-8", "");
            simpleWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);



            simpleWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                    if (url.startsWith("https://www.completesportsnigeria.com/")){
//                        Document document = null;
//                        try {
//                            document = Jsoup.connect(url).get();
//                            document.getElementsByClass("main-navigation").remove();
//                            document.getElementsByClass("custom-header-image").remove();
//                            document.getElementsByClass("header-bg header-layout-default").remove();
//                            document.getElementsByClass("post-breadcrumb").remove();
//                            document.getElementsByClass("at-share-btn-elements").remove();
//
//                            document.getElementsByClass("trc_rbox thumbnails-a trc-content-sponsored").remove();
//                            document.getElementsByClass("post-labels post-section").remove();
//                            document.getElementsByClass("wordpress-comments comments active").remove();
//                            document.getElementsByClass("post-section-title comments-title-tabs-name comments-title-tab").remove();
//                            document.getElementsByClass("wordpress-comments-title comments-title active comments-title-tab").remove();
//
//                            document.getElementsByClass("facebook-comments-title comments-title comments-title-tab").remove();
//                            document.getElementsByClass("social-count-plus").remove();
//                            document.getElementsByClass("footer-inner Shad").remove();
//
//                            simpleWebView.loadDataWithBaseURL(url, document.toString(), "text/html", "utf-8", "");
//                            simpleWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    view.loadUrl(url);
                    popUpDialog(url);

                    return super.shouldOverrideUrlLoading(view, request);
                }

                @Override
                public void onLoadResource(WebView view, String url) {
                    super.onLoadResource(view, url);

                    view.loadUrl("javascript:(function() { " +
                            "var h_img = document.getElementsByClass('custom-header-image').style.display='none';" +
                            "var h_bg = document.getElementsByClass('header-bg header-layout-default').style.display='none';" +
                            "var p_breadcrb = document.getElementsByClass('post-breadcrumb').style.display='none';" +
                            "var share_btn = document.getElementsByClass('at-share-btn-elements'').style.display='none';" +
                            "var trc = document.getElementsByClass('trc_rbox thumbnails-a trc-content-sponsored').style.display='none';" +
                            "var pst_lbl = document.getElementsByClass('post-labels post-section').style.display='none';" +
                            "var wp_cmts = document.getElementsByClass('wordpress-comments comments active').style.display='none';" +
                            "var pst_sec = document.getElementsByClass('post-section-title comments-title-tabs-name comments-title-tab').style.display='none';" +
                            "var wp_cmt = document.getElementsByClass('wordpress-comments-title comments-title active comments-title-tab').style.display='none';" +
                            "var fcbk_cmt = document.getElementsByClass('facebook-comments-title comments-title comments-title-tab').style.display='none';" +
                            "var scl_cnt = document.getElementsByClass('social-count-plus').style.display='none';" +
                            "var ft_inner = document.getElementsByClass('footer-inner Shad').style.display='none';" +
                            "})()");
                }
            });

            progressBar1.setVisibility(View.GONE);

//            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()){
//                swipeRefreshLayout.setRefreshing(false);
//            }
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

    public static class ShowDynamicDialog extends DialogFragment {

        CustomWebView webView;
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
            webView = (CustomWebView)view.findViewById(R.id.news_detail_simpleWebView);

            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setAllowFileAccess(true);
            webView.setWebChromeClient(new WebChromeClient());

            Document document = null;
                        try {
                            document = Jsoup.connect(url).get();
                            document.getElementsByClass("main-navigation").remove();
                            document.getElementsByClass("custom-header-image").remove();
                            document.getElementsByClass("header-bg header-layout-default").remove();
                            document.getElementsByClass("post-breadcrumb").remove();
                            document.getElementsByClass("at-share-btn-elements").remove();

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

//                            document.getElementsByAttributeValue("target", "_blank").remove();
                            document.getElementsByClass("facebook-comments-title comments-title comments-title-tab").remove();
                            document.getElementsByClass("social-count-plus").remove();
                            document.getElementsByClass("footer-inner Shad").remove();

                            webView.loadDataWithBaseURL(url, document.toString(), "text/html", "utf-8", "");
                            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
            return view;
        }

    }
}
