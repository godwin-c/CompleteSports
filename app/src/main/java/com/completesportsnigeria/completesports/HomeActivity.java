package com.completesportsnigeria.completesports;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.completesportsnigeria.completesports.activity.NewsDetailActivity;
import com.completesportsnigeria.completesports.adapter.CategoryFragmentAdapter;
import com.completesportsnigeria.completesports.adapter.ExpandableListAdapter;
import com.completesportsnigeria.completesports.classes.ExpandedMenuModel;
import com.completesportsnigeria.completesports.classes.RestartApp;
import com.completesportsnigeria.completesports.fragments.Blogs;
import com.completesportsnigeria.completesports.fragments.Competitions;
import com.completesportsnigeria.completesports.fragments.LifeStyle;
import com.completesportsnigeria.completesports.fragments.News;
import com.completesportsnigeria.completesports.fragments.NigProfLeague;
import com.completesportsnigeria.completesports.fragments_int.CricketSportsFragment;
import com.completesportsnigeria.completesports.fragments_int.IntFootballFragment;
import com.completesportsnigeria.completesports.fragments_int.IntGolfFragment;
import com.completesportsnigeria.completesports.fragments_int.IntNewsFragment;
import com.completesportsnigeria.completesports.fragments_int.IntRugbyFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import br.com.joinersa.oooalertdialog.Animation;
import br.com.joinersa.oooalertdialog.OnClickListener;
import br.com.joinersa.oooalertdialog.OoOAlertDialog;

public class HomeActivity extends AppCompatActivity
        implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener, NavigationView.OnNavigationItemSelectedListener {

    ViewPager vp;
    public static TabLayout tabLayout;

//    public static String COUNTRY = "country";

    ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;
    DrawerLayout mDrawerLayout;

    boolean doubleBackToExitPressedOnce = false;

//    public static AnimateHorizontalProgressBar progressBar;

    Button toolBarBtn;
//    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.FragTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        // Set TypeFace for ToolBar
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);

            if (view instanceof TextView) {
                TextView textView = (TextView) view;


                textView.setTypeface(myCustomFont);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            }


        }

        toolBarBtn = (Button) findViewById(R.id.btn_live);
//        toolBarBtn.setBackgroundDrawable(null);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);


        //VIEWPAGER
        vp = (ViewPager) findViewById(R.id.pager);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        String country = preferences.getString("country", "");
        if(country.equalsIgnoreCase(""))
        {
            //        String country = getUserCountry(this);
            country = getCountry(this);
            editor.putString("country",country);
            editor.apply();
        }

        this.addPages(country);

        //TABLAYOUT
        tabLayout = (TabLayout) findViewById(R.id.mTab_ID);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(vp);

//        count = tabLayout.getTabCount();

        toolBarBtn.setTypeface(myCustomFont);
        if (country.equals("ng")){
            toolBarBtn.setBackground(getResources().getDrawable(R.drawable.us_flag));
        }else{
            toolBarBtn.setBackground(getResources().getDrawable(R.drawable.nig_flag));
        }
        toolBarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

                final SharedPreferences preferences2 = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                final SharedPreferences.Editor editor2 = preferences2.edit();

                String country2 = preferences2.getString("country", "");
                if(country2.equalsIgnoreCase("ng"))
                {
                    //        String country = getUserCountry(this);
//
//                    builder.setTitle("Content Switch");
//                    builder.setMessage("Switch to International News?");
//
//                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            editor2.putString("country","int");
//                            editor2.apply();
//
//                            dialog.dismiss();
//
//                            RestartApp.restartThroughIntentCompatMakeRestartActivityTask(getApplicationContext());
//                        }
//                    });
//
//                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            // Do nothing
//                            dialog.dismiss();
//                        }
//                    });
//
//                    AlertDialog alert = builder.create();
//                    alert.show();

                    new OoOAlertDialog.Builder(HomeActivity.this)
                            .setMessage("Switch to International News?")
                            .setImage(R.drawable.ic_header)
                            .setAnimation(Animation.POP)
                            .setPositiveButton("Yes", new OnClickListener() {
                                @Override
                                public void onClick() {
                                    editor2.putString("country","int");
                                    editor2.apply();

//                                    dialog.dismiss();

                                    RestartApp.restartThroughIntentCompatMakeRestartActivityTask(getApplicationContext());
                                }
                            })
                            .setNegativeButton("No", new OnClickListener() {
                                @Override
                                public void onClick() {
                                    Toast.makeText(HomeActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .build();


                }else{

//                    builder.setTitle("Content Switch");
//                    builder.setMessage("Switch to Nigerian News?");
//
//                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            editor2.putString("country","ng");
//                            editor2.apply();
//
//                            dialog.dismiss();
//
//                            RestartApp.restartThroughIntentCompatMakeRestartActivityTask(getApplicationContext());
//                        }
//                    });
//
//                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            // Do nothing
//                            dialog.dismiss();
//                        }
//                    });
//
//                    AlertDialog alert = builder.create();
//                    alert.show();

                    new OoOAlertDialog.Builder(HomeActivity.this)
                            .setMessage("Switch to Nigerian News?")
                            .setImage(R.drawable.ic_header)
                            .setAnimation(Animation.POP)
                            .setPositiveButton("Yes", new OnClickListener() {
                                @Override
                                public void onClick() {
                                    editor2.putString("country","ng");
                                    editor2.apply();

//                                    dialog.dismiss();

                                    RestartApp.restartThroughIntentCompatMakeRestartActivityTask(getApplicationContext());
                                }
                            })
                            .setNegativeButton("No", new OnClickListener() {
                                @Override
                                public void onClick() {
                                    Toast.makeText(HomeActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .build();
                }
            }
        });


        // Set TypeFace for Tabs
        ViewGroup viewGroup = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = viewGroup.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) viewGroup.getChildAt(j);

            int tabChildsCount = vgTab.getChildCount();

            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    //Put your font in assests folder
                    //assign name of the font here (Must be case sensitive)
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf"));
                    ((TextView) tabViewChild).setTextSize(10);
                }
            }
        }

//        tabLayout.setOnTabSelectedListener(this);

        setAppBarHeight();

        prepareListData(country);
        mMenuAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, expandableList);

        // setting list adapter
        expandableList.setAdapter(mMenuAdapter);


// Check back for other countries

        if (country.equalsIgnoreCase("ng")){

            expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                    String title = mMenuAdapter.getChild(groupPosition, childPosition).toString();
                    Log.d("HomeActivity ***", "onChildClick: " + title);
                    try {

                        JSONObject obj = new JSONObject(loadJSONFromAsset("menu_files.json"));
                        JSONArray files = obj.getJSONArray("files");

                        boolean found = Boolean.FALSE;
                        String url_file = "";
                        String file_title = "";
                        for (int i = 0; i < files.length(); i++) {
                            JSONObject each_file = files.getJSONObject(i);

                            file_title = each_file.getString("title");

                            CHECK:
                            if (title.equalsIgnoreCase(file_title)) {
                                url_file = each_file.getString("url");
                                found = Boolean.TRUE;
                                break CHECK;
                            }
                        }

                        if (Boolean.FALSE.equals(found)) {
                            Toast.makeText(HomeActivity.this, "Yet to be implemented", Toast.LENGTH_LONG).show();
                        } else {
                            Log.d("Click Chek ***", "onChildClick: " + url_file);
                            Bundle bundle = new Bundle();
                            bundle.putString("url", url_file);
                            bundle.putString("title", title);
                            bundle.putString("from", "menu");

                            Intent intent = new Intent(HomeActivity.this, NewsDetailActivity.class);
                            intent.putExtras(bundle);

                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        Toast.makeText(HomeActivity.this, "Error has been encountered", Toast.LENGTH_SHORT).show();
                    }

                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
            });

            final int[] values = {0, 1, 4, 5, 6};
            expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    //Log.d("DEBUG", "heading clicked");
                    if (contains(values, groupPosition)) {
                        String title = listDataHeader.get(groupPosition).getIconName();

                        Log.d("HomeActivity +++", "onGroupClick: " + title);

                        try {
                            JSONObject obj = new JSONObject(loadJSONFromAsset("menu_files.json"));
                            JSONArray files = obj.getJSONArray("files");

                            boolean found = Boolean.FALSE;
                            String file_url = "";
                            String file_title = "";
                            for (int i = 0; i < files.length(); i++) {
                                JSONObject each_file = files.getJSONObject(i);

                                file_title = each_file.getString("title");


                                CHECK:
                                if (title.equalsIgnoreCase(file_title)) {
                                    file_url = each_file.getString("url");
                                    found = Boolean.TRUE;
                                    break CHECK;
                                }
                            }

                            if (Boolean.FALSE.equals(found)) {
                                Toast.makeText(HomeActivity.this, "Yet to be implemented", Toast.LENGTH_LONG).show();
                            } else {
                                Log.d("Group Check +++", "onGroupClick: " + file_url);
                                Bundle bundle = new Bundle();
                                bundle.putString("url", file_url);
                                bundle.putString("title", title);
                                bundle.putString("from", "menu");

                                Intent intent = new Intent(HomeActivity.this, NewsDetailActivity.class);
                                intent.putExtras(bundle);

                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            Toast.makeText(HomeActivity.this, "Error has been encountered", Toast.LENGTH_SHORT).show();
                        }

                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }
                    return false;
                }
            });
        }else{

            expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                    String title = mMenuAdapter.getChild(groupPosition, childPosition).toString();
                    Log.d("HomeActivity ***", "onChildClick: " + title);
                    try {

                        JSONObject obj = new JSONObject(loadJSONFromAsset("menu_int_files.json"));
                        JSONArray files = obj.getJSONArray("files");

                        boolean found = Boolean.FALSE;
                        String url_file = "";
                        String file_title = "";
                        for (int i = 0; i < files.length(); i++) {
                            JSONObject each_file = files.getJSONObject(i);

                            file_title = each_file.getString("title");

                            CHECK:
                            if (title.equalsIgnoreCase(file_title)) {
                                url_file = each_file.getString("url");
                                found = Boolean.TRUE;
                                break CHECK;
                            }
                        }

                        if (Boolean.FALSE.equals(found)) {
                            Toast.makeText(HomeActivity.this, "Yet to be implemented", Toast.LENGTH_LONG).show();
                        } else {
                            Log.d("Click Chek ***", "onChildClick: " + url_file);
                            Bundle bundle = new Bundle();
                            bundle.putString("url", url_file);
                            bundle.putString("title", title);
                            bundle.putString("from", "menu");

                            Intent intent = new Intent(HomeActivity.this, NewsDetailActivity.class);
                            intent.putExtras(bundle);

                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        Toast.makeText(HomeActivity.this, "Error has been encountered", Toast.LENGTH_SHORT).show();
                    }

                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
            });

            final int[] values = {0, 1, 3, 4, 5, 6};
            expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    //Log.d("DEBUG", "heading clicked");
                    if (contains(values, groupPosition)) {
                        String title = listDataHeader.get(groupPosition).getIconName();

                        Log.d("HomeActivity +++", "onGroupClick: " + title);

                        try {
                            JSONObject obj = new JSONObject(loadJSONFromAsset("menu_int_files.json"));
                            JSONArray files = obj.getJSONArray("files");

                            boolean found = Boolean.FALSE;
                            String file_url = "";
                            String file_title = "";
                            for (int i = 0; i < files.length(); i++) {
                                JSONObject each_file = files.getJSONObject(i);

                                file_title = each_file.getString("title");


                                CHECK:
                                if (title.equalsIgnoreCase(file_title)) {
                                    file_url = each_file.getString("url");
                                    found = Boolean.TRUE;
                                    break CHECK;
                                }
                            }

                            if (Boolean.FALSE.equals(found)) {
                                Toast.makeText(HomeActivity.this, "Yet to be implemented", Toast.LENGTH_LONG).show();
                            } else {
                                Log.d("Group Check +++", "onGroupClick: " + file_url);
                                Bundle bundle = new Bundle();
                                bundle.putString("url", file_url);
                                bundle.putString("title", title);
                                bundle.putString("from", "menu");

                                Intent intent = new Intent(HomeActivity.this, NewsDetailActivity.class);
                                intent.putExtras(bundle);

                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            Toast.makeText(HomeActivity.this, "Error has been encountered", Toast.LENGTH_SHORT).show();
                        }

                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }
                    return false;
                }
            });
        }

    }

    private void addPages(String country) {
        CategoryFragmentAdapter pagerAdapter = new CategoryFragmentAdapter(this.getSupportFragmentManager());

        if (!country.equals("ng")) {
            pagerAdapter.addFragment(new IntNewsFragment());
            pagerAdapter.addFragment(new IntFootballFragment());
            pagerAdapter.addFragment(new IntGolfFragment());
            pagerAdapter.addFragment(new IntRugbyFragment());
            pagerAdapter.addFragment(new CricketSportsFragment());
        } else {
            pagerAdapter.addFragment(new News());
            pagerAdapter.addFragment(new Blogs());
            pagerAdapter.addFragment(new Competitions());
            pagerAdapter.addFragment(new LifeStyle());
            pagerAdapter.addFragment(new NigProfLeague());
        }
        //SET ADAPTER TO VP
        vp.setAdapter(pagerAdapter);
    }

    public boolean contains(final int[] array, final int key) {
        Arrays.sort(array);
        return Arrays.binarySearch(array, key) >= 0;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (tabLayout.getSelectedTabPosition() == 0) {

                // *** Check how many times back Button was clicked
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);

            } else {
                tabLayout.getTabAt(0).select();
            }

        }
    }

    public void onTabSelected(TabLayout.Tab tab) {
        vp.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }


    private void prepareListData(String country) {

        listDataHeader = new ArrayList<ExpandedMenuModel>();
        listDataChild = new HashMap<ExpandedMenuModel, List<String>>();

        if(country.equalsIgnoreCase("ng")) {


            ExpandedMenuModel news = new ExpandedMenuModel();
            news.setIconName("News");
            int news_img = getResources().getIdentifier("com.completesportsnigeria.completesports:drawable/news", null, null);
            news.setIconImg(news_img);
            // Adding data header
            listDataHeader.add(news);

            ExpandedMenuModel item2 = new ExpandedMenuModel();
            item2.setIconName("Stars Abroad");
            int item2_img = getResources().getIdentifier("com.completesportsnigeria.completesports:drawable/stars_abroad", null, null);
            item2.setIconImg(item2_img);
            listDataHeader.add(item2);

            ExpandedMenuModel item3 = new ExpandedMenuModel();
            item3.setIconName("World Football");
            int item3_img = getResources().getIdentifier("com.completesportsnigeria.completesports:drawable/world_football", null, null);
            item3.setIconImg(item3_img);
            listDataHeader.add(item3);

            ExpandedMenuModel item4 = new ExpandedMenuModel();
            item4.setIconName("Competitions");
            int item4_img = getResources().getIdentifier("com.completesportsnigeria.completesports:drawable/competitions", null, null);
            item4.setIconImg(item4_img);
            listDataHeader.add(item4);

            ExpandedMenuModel item5 = new ExpandedMenuModel();
            item5.setIconName("Video");
            int item5_img = getResources().getIdentifier("com.completesportsnigeria.completesports:drawable/videos", null, null);
            item5.setIconImg(item5_img);
            listDataHeader.add(item5);

            ExpandedMenuModel item6 = new ExpandedMenuModel();
            item6.setIconName("Sports Extra");
            int item6_img = getResources().getIdentifier("com.completesportsnigeria.completesports:drawable/sports_extra", null, null);
            item6.setIconImg(item6_img);
            listDataHeader.add(item6);

            ExpandedMenuModel item7 = new ExpandedMenuModel();
            item7.setIconName("Features");
            int item7_img = getResources().getIdentifier("com.completesportsnigeria.completesports:drawable/features", null, null);
            item7.setIconImg(item7_img);
            listDataHeader.add(item7);


            List<String> heading3 = new ArrayList<String>();
            heading3.add("EPL News");
            heading3.add("Bundesliga News");
            heading3.add("La Liga News");
            heading3.add("Serie A News");
            heading3.add("Ligue 1 News");
            heading3.add("UCL News");

            List<String> heading4 = new ArrayList<String>();
            heading4.add("World Cup");
            heading4.add("AFCON");
            heading4.add("CHAN");
            heading4.add("CAF Champions League");
//        heading4.add("P&W Leader Board");


            listDataChild.put(listDataHeader.get(2), heading3);
            listDataChild.put(listDataHeader.get(3), heading4);
        }else{
            ExpandedMenuModel news = new ExpandedMenuModel();
            news.setIconName("News");
            int news_img = getResources().getIdentifier("com.completesportsnigeria.completesports:drawable/news", null, null);
            news.setIconImg(news_img);
            // Adding data header
            listDataHeader.add(news);

            ExpandedMenuModel item2 = new ExpandedMenuModel();
            item2.setIconName("Transfers");
            int item2_img = getResources().getIdentifier("com.completesportsnigeria.completesports:drawable/stars_abroad", null, null);
            item2.setIconImg(item2_img);
            listDataHeader.add(item2);

            ExpandedMenuModel item3 = new ExpandedMenuModel();
            item3.setIconName("Football");
            int item3_img = getResources().getIdentifier("com.completesportsnigeria.completesports:drawable/world_football", null, null);
            item3.setIconImg(item3_img);
            listDataHeader.add(item3);

            ExpandedMenuModel item4 = new ExpandedMenuModel();
            item4.setIconName("NBA");
            int item4_img = getResources().getIdentifier("com.completesportsnigeria.completesports:drawable/competitions", null, null);
            item4.setIconImg(item4_img);
            listDataHeader.add(item4);

            ExpandedMenuModel item5 = new ExpandedMenuModel();
            item5.setIconName("Rugby Union");
            int item5_img = getResources().getIdentifier("com.completesportsnigeria.completesports:drawable/rugby", null, null);
            item5.setIconImg(item5_img);
            listDataHeader.add(item5);

            ExpandedMenuModel item6 = new ExpandedMenuModel();
            item6.setIconName("Life Style");
            int item6_img = getResources().getIdentifier("com.completesportsnigeria.completesports:drawable/sports_extra", null, null);
            item6.setIconImg(item6_img);
            listDataHeader.add(item6);

            ExpandedMenuModel item7 = new ExpandedMenuModel();
            item7.setIconName("Fitness");
            int item7_img = getResources().getIdentifier("com.completesportsnigeria.completesports:drawable/fitness", null, null);
            item7.setIconImg(item7_img);
            listDataHeader.add(item7);


            List<String> heading3 = new ArrayList<String>();
            heading3.add("MLS News");
            heading3.add("Bundesliga News");
            heading3.add("La Liga News");
            heading3.add("Serie A News");
            heading3.add("Ligue 1 News");
            heading3.add("UCL News");

//            List<String> heading4 = new ArrayList<String>();
//            heading4.add("World Cup");
//            heading4.add("AFCON");
//            heading4.add("CHAN");
//            heading4.add("CAF Champions League");
//        heading4.add("P&W Leader Board");


            listDataChild.put(listDataHeader.get(2), heading3);
//            listDataChild.put(listDataHeader.get(3), heading4);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        //revision: this don't works, use setOnChildClickListener() and setOnGroupClickListener() above instead
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setAppBarHeight() {
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight() + dpToPx(48 + 56)));
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

    public String loadJSONFromAsset(String filename) {
        String json = null;
        try {

            InputStream is = getAssets().open(filename);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }


    public String getCountry(Context context) {
        String country;

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                country = getUserCountry(context);
                if (country != null) {

                    return country;
                }

            } else {

                Location location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                } else {
                    Geocoder gcd = new Geocoder(context, Locale.getDefault());
                    List<Address> addresses;
                    try {
                        addresses = gcd.getFromLocation(location.getLatitude(),
                                location.getLongitude(), 1);

                        if (addresses != null && !addresses.isEmpty()) {
                            country = addresses.get(0).getCountryName();
                            if (country != null) {

                                return country;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        country = getUserCountry(context);
        if (country != null) {

            return country;
        }
        return null;
    }

    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     *
     * @param context Context reference to get the TelephonyManager instance from
     * @return country code or null
     */
    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

}
