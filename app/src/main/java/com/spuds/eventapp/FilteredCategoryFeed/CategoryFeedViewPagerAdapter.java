package com.spuds.eventapp.FilteredCategoryFeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.spuds.eventapp.R;

/**
 * Created by tina on 5/11/16.
 */
public class CategoryFeedViewPagerAdapter extends FragmentStatePagerAdapter {

    Fragment categoryFeedTabsFragment;
    String catType;

    public CategoryFeedViewPagerAdapter(FragmentManager fm, Fragment categoryFeedTabsFragment, String catType) {
        super(fm);
        this.categoryFeedTabsFragment = categoryFeedTabsFragment;
        this.catType = catType;
    }

    //TODO: Create different fragments passing in filter
    @Override
    public Fragment getItem(int position) {
        CategoryFeedFragment categoryFeedFragment = new CategoryFeedFragment();

        Bundle bundle = new Bundle();

        switch (position) {
            // New
            case 0:
                bundle.putString(categoryFeedTabsFragment.getString(R.string.tab_tag),
                        categoryFeedTabsFragment.getString(R.string.tab_new));
                break;
            // Hot
            case 1:
                bundle.putString(categoryFeedTabsFragment.getString(R.string.tab_tag),
                        categoryFeedTabsFragment.getString(R.string.tab_hot));
                break;
            // Now
            case 2:
                bundle.putString(categoryFeedTabsFragment.getString(R.string.tab_tag),
                        categoryFeedTabsFragment.getString(R.string.tab_now));
                break;
        }

        bundle.putString("Category Type", catType);

        categoryFeedFragment.setArguments(bundle);

        return categoryFeedFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
