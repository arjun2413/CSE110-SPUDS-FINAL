package com.spuds.eventapp.MyEvents;

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
public class MyEventsViewPagerAdapter extends FragmentStatePagerAdapter {

    // Reference to fragment instantiated from
    Fragment myEventsFeedTabsFragment;

    /*---------------------------------------------------------------------------
    Function Name:                MyEventsViewPagerAdapter()
    Description:                  Initalizing instance variables
    Input:                        FragmentManager fm - reference to fragment manager
                                  Fragment homeFeedTabsFragment - reference
                                    to fragment instantiated from
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public MyEventsViewPagerAdapter(FragmentManager fm, Fragment myEventsFeedTabsFragment) {
        super(fm);
        this.myEventsFeedTabsFragment = myEventsFeedTabsFragment;
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

        // Create new events feed fragment
        MyEventsFeedFragment myEventsFeedFragment = new MyEventsFeedFragment();

        Bundle bundle = new Bundle();

        // Based on the position, put in the tab type in the bundle for the my events feed
        switch (position) {
            // Going
            case 0:
                bundle.putString(myEventsFeedTabsFragment.getString(R.string.tab_tag),
                        myEventsFeedTabsFragment.getString(R.string.tab_going));
                break;
            // Hot
            case 1:
                bundle.putString(myEventsFeedTabsFragment.getString(R.string.tab_tag),
                        myEventsFeedTabsFragment.getString(R.string.tab_hosting));
                break;
        }

        // Set the arguments for the bundle to the home feed fragment
        myEventsFeedFragment.setArguments(bundle);

        return myEventsFeedFragment;
        //return new MyEventsFeedFragment();
    }

    /*---------------------------------------------------------------------------
    Function Name:                getCount()
    Description:                  Return how many tabs there are
    Input:                        None
    Output:                       int - how many tabs there are
    ---------------------------------------------------------------------------*/
    @Override
    public int getCount() {
        return 2;
    }

}