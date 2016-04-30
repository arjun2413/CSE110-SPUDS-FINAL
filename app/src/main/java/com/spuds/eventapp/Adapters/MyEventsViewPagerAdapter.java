package com.spuds.eventapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.spuds.eventapp.NewsFeeds.MyEventsFeedFragment;

/**
 * Created by tina on 4/30/16.
 */
public class MyEventsViewPagerAdapter extends FragmentStatePagerAdapter {

    public MyEventsViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new MyEventsFeedFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

}