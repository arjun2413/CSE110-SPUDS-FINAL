package com.spuds.eventapp.HomeFeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.spuds.eventapp.R;

/**
 * Created by tina on 4/30/16.
 */

/*---------------------------------------------------------------------------
Class Name:                HomeFeedViewPagerAdapter
Description:               Sets up adapter for tabs: creates new home feeds
                           new/hot/now for each tab/page
---------------------------------------------------------------------------*/
public class HomeFeedViewPagerAdapter extends FragmentStatePagerAdapter {

    // Reference to fragment instantiated from
    Fragment homeFeedTabsFragment;

    /*---------------------------------------------------------------------------
    Function Name:                HomeFeedViewPagerAdapter()
    Description:                  Initalizing instance variables
    Input:                        FragmentManager fm - reference to fragment manager
                                  Fragment homeFeedTabsFragment - reference
                                    to fragment instantiated from
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public HomeFeedViewPagerAdapter(FragmentManager fm, Fragment homeFeedTabsFragment) {
        super(fm);

        this.homeFeedTabsFragment = homeFeedTabsFragment;
    }

    /*---------------------------------------------------------------------------
    Function Name:                getItem()
    Description:                  Called each time fragment is created; gets
                                  information passed to this fragment
    Input:                        int position - which tab to instantiated
                                    based on position
    Output:                       Fragment - home feed fragment to be instantiated
    ---------------------------------------------------------------------------*/
    @Override
    public Fragment getItem(int position) {

        // Create new home feed fragment
        HomeFeedFragment homeFeedFragment = new HomeFeedFragment();

        Bundle bundle = new Bundle();

        // Based on the position, put in the tab type in the bundle for the home feed
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

        // Set the arguments for the bundle to the home feed fragment
        homeFeedFragment.setArguments(bundle);

        return homeFeedFragment;
    }

    /*---------------------------------------------------------------------------
    Function Name:                getCount()
    Description:                  Return how many tabs there are
    Input:                        None
    Output:                       int - how many tabs there are
    ---------------------------------------------------------------------------*/
    @Override
    public int getCount() {
        return 3;
    }

}