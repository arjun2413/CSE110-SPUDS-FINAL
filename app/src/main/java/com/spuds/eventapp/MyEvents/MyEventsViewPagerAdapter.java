package com.spuds.eventapp.MyEvents;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.spuds.eventapp.R;

/**
 * Created by tina on 4/30/16.
 */
public class MyEventsViewPagerAdapter extends FragmentStatePagerAdapter {

    Fragment myEventsFeedTabsFragment;

    public MyEventsViewPagerAdapter(FragmentManager fm, Fragment myEventsFeedTabsFragment) {
        super(fm);
        this.myEventsFeedTabsFragment = myEventsFeedTabsFragment;
    }

    @Override
    public Fragment getItem(int position) {
        MyEventsFeedFragment myEventsFeedFragment = new MyEventsFeedFragment();

        Bundle bundle = new Bundle();

        switch (position) {
            // Going
            case 0:
                bundle.putString(myEventsFeedTabsFragment.getString(R.string.tab_tag),
                        myEventsFeedTabsFragment.getString(R.string.tab_new));
                break;
            // Hot
            case 1:
                bundle.putString(myEventsFeedTabsFragment.getString(R.string.tab_tag),
                        myEventsFeedTabsFragment.getString(R.string.tab_hot));
                break;
        }

        myEventsFeedFragment.setArguments(bundle);

        return myEventsFeedFragment;
        //return new MyEventsFeedFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

}