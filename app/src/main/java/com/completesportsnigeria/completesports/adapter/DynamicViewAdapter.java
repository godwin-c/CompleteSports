package com.completesportsnigeria.completesports.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.completesportsnigeria.completesports.classes.FeedItem;
import com.completesportsnigeria.completesports.fragments.DynamicFragment;

import java.util.ArrayList;

public class DynamicViewAdapter extends FragmentStatePagerAdapter {
    private Context ctx;
    ArrayList<FeedItem> feedItems;
    private Fragment[] fragments;
    private int position;

    public DynamicViewAdapter(Context ctx, FragmentManager fm, ArrayList<FeedItem> feedItems, int position) {
        super(fm);
        this.ctx = ctx;
        this.feedItems = feedItems;
        this.position = position;
        fragments = new Fragment[feedItems.size()];
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
//        String items = data.get(position);

        FeedItem feedItem = feedItems.get(position);

        DynamicFragment dynamicFragment = new DynamicFragment();

        Bundle bundle = new Bundle();
        bundle.putString("url", feedItem.getLink());
        bundle.putString("title", feedItem.getTitle());

        fragment = dynamicFragment;

        fragment.setArguments(bundle);

        if (fragments[position] == null) {
            fragments[position] = fragment;
        }
        return fragments[position];
    }

    @Override
    public int getCount() {
        if (feedItems != null) {
            return feedItems.size();
        } else {
            return 0;
        }
    }

}
