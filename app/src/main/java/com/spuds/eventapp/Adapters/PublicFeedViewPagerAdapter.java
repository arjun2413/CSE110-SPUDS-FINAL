package com.spuds.eventapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.spuds.eventapp.NewsFeeds.PublicFeedFragment;

/**
 * Created by tina on 4/30/16.
 */
public class PublicFeedViewPagerAdapter extends FragmentStatePagerAdapter {

    public PublicFeedViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //TODO: Create different fragments passing in filter
    @Override
    public Fragment getItem(int position) {
        return new PublicFeedFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }

}