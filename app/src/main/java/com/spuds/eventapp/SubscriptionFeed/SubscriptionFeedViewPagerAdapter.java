package com.spuds.eventapp.SubscriptionFeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.spuds.eventapp.R;

/**
 * Created by tina on 4/30/16.
 */
public class SubscriptionFeedViewPagerAdapter extends FragmentStatePagerAdapter {

    Fragment subscriptionFeedTabsFragment;

    public SubscriptionFeedViewPagerAdapter(FragmentManager fm, Fragment subscriptionFeedTabsFragment) {
        super(fm);

        this.subscriptionFeedTabsFragment = subscriptionFeedTabsFragment;
    }

    //TODO: Create different fragments passing in filter
    @Override
    public Fragment getItem(int position) {
        SubscriptionFeedFragment subscriptionFeedFragment = new SubscriptionFeedFragment();

        Bundle bundle = new Bundle();

        switch (position) {
            // New
            case 0:
                bundle.putString(subscriptionFeedTabsFragment.getString(R.string.tab_tag),
                        subscriptionFeedTabsFragment.getString(R.string.tab_new));
                break;
            // Hot
            case 1:
                bundle.putString(subscriptionFeedTabsFragment.getString(R.string.tab_tag),
                        subscriptionFeedTabsFragment.getString(R.string.tab_hot));
                break;
            // Now
            case 2:
                bundle.putString(subscriptionFeedTabsFragment.getString(R.string.tab_tag),
                        subscriptionFeedTabsFragment.getString(R.string.tab_now));
                break;
        }

        subscriptionFeedFragment.setArguments(bundle);

        return subscriptionFeedFragment;
    }

    @Override
    public int getCount() {
        return 3;
   }

}