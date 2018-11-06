package com.completesportsnigeria.completesports.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.completesportsnigeria.completesports.R;
import com.completesportsnigeria.completesports.activity.NewsDetailActivity;
import com.completesportsnigeria.completesports.activity.SelectedNewsActivity;
import com.completesportsnigeria.completesports.adapter.DynamicViewAdapter;
import com.completesportsnigeria.completesports.classes.FeedItem;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class SelectedNewsActivityFragment extends Fragment {

    private LinearLayout indicator;
    private int mDotCount;
    ArrayList<FeedItem> somefeeds;
    private static int page_position;

    private ImageView close_button;
    private LinearLayout[] mDots;
    private ViewPager viewPager;
    //        private List<String> listItem = new ArrayList<>();
    private DynamicViewAdapter fragmentAdapter;

    public SelectedNewsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selected_news, container);
        indicator = (LinearLayout) view.findViewById(R.id.indicators);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager_itemList);

        setData(page_position);



        return view;
    }


    private void setData(int position) {

        fragmentAdapter = new DynamicViewAdapter(getContext(), getFragmentManager(), somefeeds, position);
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
            mDots[i] = new LinearLayout(getContext());
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

}
