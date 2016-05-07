package com.spuds.eventapp.HomeFeed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by tina on 4/30/16.
 */
public class HomeFeedViewPagerAdapter extends FragmentStatePagerAdapter {

    Fragment homeFeedTabsFragment;

    public HomeFeedViewPagerAdapter(FragmentManager fm, Fragment homeFeedTabsFragment) {
        super(fm);
        this.homeFeedTabsFragment = homeFeedTabsFragment;
    }

    //TODO: Create different fragments passing in filter
    @Override
    public Fragment getItem(int position) {
        return new HomeFeedFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }

}