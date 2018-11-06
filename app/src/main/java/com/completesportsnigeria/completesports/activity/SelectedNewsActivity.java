package com.completesportsnigeria.completesports.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.completesportsnigeria.completesports.R;
import com.completesportsnigeria.completesports.adapter.DynamicViewAdapter;
import com.completesportsnigeria.completesports.classes.FeedItem;

import java.util.ArrayList;

public class SelectedNewsActivity extends AppCompatActivity {

    ArrayList<FeedItem> somefeeds;
    int page_position;

    private LinearLayout indicator;
    private int mDotCount;

    private LinearLayout[] mDots;
    private ViewPager viewPager;
    private DynamicViewAdapter fragmentAdapter;

//    public static FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set TypeFace for ToolBar
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);

            if (view instanceof TextView) {
                TextView textView = (TextView) view;

                Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
                textView.setTypeface(myCustomFont);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            }


        }

        Intent intent = getIntent();
        page_position = (int) intent.getIntExtra("position", 0);
        somefeeds = (ArrayList<FeedItem>) intent.getSerializableExtra("feeditems");


        indicator = (LinearLayout) findViewById(R.id.indicators);
        viewPager = (ViewPager) findViewById(R.id.viewPager_itemList);

        setData(page_position);

        setAppBarHeight();

//        fab = (FloatingActionButton) findViewById(R.id.fab);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

//    public void loadFragment(Fragment fragment, Boolean bool) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment, fragment);
//        if (bool)
//            transaction.addToBackStack(null);
//        transaction.commit();
//    }

    private void setData(int position) {

        fragmentAdapter = new DynamicViewAdapter(SelectedNewsActivity.this, getSupportFragmentManager(), somefeeds, position);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(page_position);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mDotCount; i++) {
                    mDots[i].setBackgroundResource(R.drawable.nonselected_item);
                }
                mDots[position].setBackgroundResource(R.drawable.selected_item);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setUiPageViewController();

    }


    private void setUiPageViewController() {
        mDotCount = fragmentAdapter.getCount();
        mDots = new LinearLayout[mDotCount];

        for (int i = 0; i < mDotCount; i++) {
            mDots[i] = new LinearLayout(this);
            mDots[i].setBackgroundResource(R.drawable.nonselected_item);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);
            indicator.addView(mDots[i], params);
            mDots[0].setBackgroundResource(R.drawable.selected_item);
        }
    }

    private void setAppBarHeight() {
        AppBarLayout appBarLayout = findViewById(R.id.selected_news_appbar);
        appBarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight() + dpToPx(15 + 56)));
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

//    @Override
//    public void onBackPressed() {
//
//        int count = getFragmentManager().getBackStackEntryCount();
//
//        if (count == 1) {
//            super.onBackPressed();
//            //additional code
//        } else {
//            getFragmentManager().popBackStack();
//        }
//
//    }
//
//    private void hideKeyboard() {
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(mInputField.getWindowToken(), 0);
//    }
}
