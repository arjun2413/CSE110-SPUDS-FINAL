package com.spuds.eventapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.spuds.eventapp.NewsFeeds.MySubscriptionFeedFragment;

/**
 * Created by tina on 4/30/16.
 */
public class MySubscriptionFeedViewPagerAdapter extends FragmentStatePagerAdapter {

    public MySubscriptionFeedViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //TODO: Create different fragments passing in filter
    @Override
    public Fragment getItem(int position) {
        return new MySubscriptionFeedFragment();
    }

    @Override
    public int getCount() {
        return 3;
   }

}