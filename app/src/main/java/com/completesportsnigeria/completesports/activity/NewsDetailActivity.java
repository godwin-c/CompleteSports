package com.completesportsnigeria.completesports.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.completesportsnigeria.completesports.R;
import com.completesportsnigeria.completesports.fragments.EachMenuFragment;
import com.completesportsnigeria.completesports.fragments.WebViewFragment;

public class NewsDetailActivity extends AppCompatActivity {

    String url;
    String title;

    //    public static ImageView menu;
    public static FloatingActionButton share;
//    public static TextView titleView;
//    public static View customView;

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        titleView = (TextView) findViewById(R.id.title);
        share = (FloatingActionButton) findViewById(R.id.share);
//        customView = (View) findViewById(R.id.titleLayout);

//        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
//        titleView.setTypeface(typeface);

//        viewPager = (ViewPager)findViewById(R.id.news_detail_pager);

        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url");
        title = bundle.getString("title");



        if (intent.hasExtra("from")){


            EachMenuFragment eachMenuFragment = new EachMenuFragment();

            bundle.putString("url", url);
            bundle.putString("title",title);
            eachMenuFragment.setArguments(bundle);
            loadFragment(eachMenuFragment, false);

        }else{

            WebViewFragment webViewFragment = new WebViewFragment();
            bundle.putString("url", url);
            bundle.putString("title", title);
            webViewFragment.setArguments(bundle);
            loadFragment(webViewFragment, false);

        }

        setAppBarHeight();
    }

    public void loadFragment(Fragment fragment, Boolean bool) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        if (bool)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setAppBarHeight() {
        AppBarLayout appBarLayout = findViewById(R.id.selected_news_appbar);
        appBarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight() + dpToPx(18 + 56)));
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int dpToPx(int dp) {
        float density = getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
