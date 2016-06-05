package com.spuds.eventapp.Profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.spuds.eventapp.R;

/**
 * Created by David on 5/28/16.
 */
/*---------------------------------------------------------------------------
Class Name:                HomeFeedViewPagerAdapter
Description:               Sets up adapter for tabs: creates new home feeds
                           new/hot/now for each tab/page
---------------------------------------------------------------------------*/
public class ProfileViewPagerAdapter extends FragmentStatePagerAdapter {

    // Reference to fragment instantiated from
    Fragment profileTabsFragment;

    // Reference to the user id from the database
    String userId;

    /*---------------------------------------------------------------------------
    Function Name:                HomeFeedViewPagerAdapter()
    Description:                  Initializing instance variables
    Input:                        FragmentManager fm - reference to fragment manager
                                  Fragment homeFeedTabsFragment - reference
                                    to fragment instantiated from
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public ProfileViewPagerAdapter(FragmentManager fm, Fragment profileTabsFragment, String userId) {
        super(fm);

        this.profileTabsFragment = profileTabsFragment;
        this.userId = userId;
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
        ProfileFeedFragment profileFeedFragment = new ProfileFeedFragment();


        Bundle bundle = new Bundle();

        // Based on the position, put in the tab type in the bundle for the home feed
        switch (position) {
            // Going
            case 0:
                bundle.putString(profileTabsFragment.getString(R.string.tab_tag),
                        profileTabsFragment.getString(R.string.tab_going));
                break;
            // Hot
            case 1:
                bundle.putString(profileTabsFragment.getString(R.string.tab_tag),
                        profileTabsFragment.getString(R.string.tab_hosting));
                break;
        }

        bundle.putString(profileTabsFragment.getString(R.string.user_id), userId);

        // Set the arguments for the bundle to the home feed fragment
        profileFeedFragment.setArguments(bundle);

        return profileFeedFragment;
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
