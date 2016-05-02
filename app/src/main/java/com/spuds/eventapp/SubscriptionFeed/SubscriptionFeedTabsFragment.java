package com.spuds.eventapp.SubscriptionFeed;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

        return view;
    }
    private void tabs(View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        viewPager.setAdapter(subscriptionFeedViewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);

        final TabLayout.Tab newFilter = tabLayout.newTab();
        final TabLayout.Tab hotFilter = tabLayout.newTab();
        final TabLayout.Tab happeningNowFilter = tabLayout.newTab();

        newFilter.setText("New");
        hotFilter.setText("Hot");
        happeningNowFilter.setText("Happening Now");

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
        subscriptionFeedViewPagerAdapter = new SubscriptionFeedViewPagerAdapter(getChildFragmentManager()); //here used child fragment manager
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dashboard, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}


