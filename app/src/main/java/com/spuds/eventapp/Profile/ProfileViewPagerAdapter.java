package com.spuds.eventapp.Profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.spuds.eventapp.R;

/**
 * Created by David on 5/28/16.
 */
public class ProfileViewPagerAdapter extends FragmentStatePagerAdapter {
    Fragment profileTabsFragment;

    public ProfileViewPagerAdapter(FragmentManager fm, Fragment profileTabsFragment) {
        super(fm);
        this.profileTabsFragment = profileTabsFragment;
    }

    @Override
    public Fragment getItem(int position) {
        ProfileFeedFragment profileFeedFragment = new ProfileFeedFragment();

        Bundle bundle = new Bundle();

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


        profileFeedFragment.setArguments(bundle);

        return profileFeedFragment;
        //return new profileFeedFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
