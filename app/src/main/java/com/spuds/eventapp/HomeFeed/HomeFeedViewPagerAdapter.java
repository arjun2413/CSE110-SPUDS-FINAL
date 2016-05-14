package com.spuds.eventapp.HomeFeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.spuds.eventapp.R;

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
        HomeFeedFragment homeFeedFragment = new HomeFeedFragment();

        Bundle bundle = new Bundle();

        switch (position) {
            // New
            case 0:
                bundle.putString(homeFeedTabsFragment.getString(R.string.tab_tag),
                        homeFeedTabsFragment.getString(R.string.tab_new));
                break;
            // Hot
            case 1:
                bundle.putString(homeFeedTabsFragment.getString(R.string.tab_tag),
                        homeFeedTabsFragment.getString(R.string.tab_hot));
                break;
            // Now
            case 2:
                bundle.putString(homeFeedTabsFragment.getString(R.string.tab_tag),
                        homeFeedTabsFragment.getString(R.string.tab_now));
                break;
        }

        homeFeedFragment.setArguments(bundle);

        return homeFeedFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

}