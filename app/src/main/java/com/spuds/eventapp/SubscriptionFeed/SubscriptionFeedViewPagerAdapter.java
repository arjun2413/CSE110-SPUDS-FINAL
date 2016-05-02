package com.spuds.eventapp.SubscriptionFeed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by tina on 4/30/16.
 */
public class SubscriptionFeedViewPagerAdapter extends FragmentStatePagerAdapter {

    public SubscriptionFeedViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //TODO: Create different fragments passing in filter
    @Override
    public Fragment getItem(int position) {
        return new SubscriptionFeedFragment();
    }

    @Override
    public int getCount() {
        return 3;
   }

}