package com.spuds.eventapp.SubscriptionFeed;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spuds.eventapp.R;

public class SubscriptionFeedTabsFragment extends Fragment {

    SubscriptionFeedViewPagerAdapter subscriptionFeedViewPagerAdapter;

    public SubscriptionFeedTabsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_tabs, container, false);
        tabs(view);
        overrideFonts(view.getContext(),view);
        return view;
    }
    private void tabs(View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);

        viewPager.setAdapter(subscriptionFeedViewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);

        final TabLayout.Tab newFilter = tabLayout.newTab();
        final TabLayout.Tab hotFilter = tabLayout.newTab();
        final TabLayout.Tab happeningNowFilter = tabLayout.newTab();

        newFilter.setText("New");
        hotFilter.setText("Hot");
        happeningNowFilter.setText("Now");

        tabLayout.addTab(newFilter, 0);
        tabLayout.addTab(hotFilter, 1);
        tabLayout.addTab(happeningNowFilter, 2);


        //tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.tab_selector));
        //tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.indicator));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        subscriptionFeedViewPagerAdapter = new SubscriptionFeedViewPagerAdapter(getChildFragmentManager(), this); //here used child fragment manager
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Medium.ttf"));
            }
        }
        catch (Exception e) {
        }
    }
}


