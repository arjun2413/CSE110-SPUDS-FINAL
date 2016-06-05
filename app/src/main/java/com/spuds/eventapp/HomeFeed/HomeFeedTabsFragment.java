package com.spuds.eventapp.HomeFeed;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;

/*---------------------------------------------------------------------------
Class Name:                HomeFeedTabsFragment
Description:               Sets up creating tabs for home feed: new, hot, now
---------------------------------------------------------------------------*/
public class HomeFeedTabsFragment extends Fragment {

    // Adapter for the tablayout and viewpager
    HomeFeedViewPagerAdapter myHomeFeedViewPagerAdapter;

    /*---------------------------------------------------------------------------
    Function Name:                HomeFeedTabsFragment
    Description:                  Required default no-argument constructor
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public HomeFeedTabsFragment() {
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreate()
    Description:                  Called each time fragment is created
    Input:                        Bundle savedInstanceState
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreateView()
    Description:                  Inflates View layout and sets fonts programmatically
                                  and setting up fonts
    Input:                        LayoutInflater inflater - inflates layout
                                  ViewGroup container - parent view group
                                  Bundle savedInstanceState
    Output:                       View to be inflated
    ---------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for the tabs
        View view = inflater.inflate(R.layout.fragment_feed_tabs, container, false);

        // Set fonts for all text views in this layout
        overrideFonts(view.getContext(), view);

        setupTabs(view);
        return view;
    }

    /*---------------------------------------------------------------------------
    Function Name:                setupTabs()
    Description:                  Sets up the new/hot/now tabs
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    private void setupTabs(View view) {
        // Layout view for the tabs
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.profile_tabs);

        // Page view for the tabs
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);

        // Sets an adapter for the page viewer limiting number of tabs to 3
        viewPager.setAdapter(myHomeFeedViewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);

        // Creates new tabs for the tablayout
        final TabLayout.Tab newFilter = tabLayout.newTab();
        final TabLayout.Tab hotFilter = tabLayout.newTab();
        final TabLayout.Tab happeningNowFilter = tabLayout.newTab();

        // Sets text for the tabs
        newFilter.setText("New");
        hotFilter.setText("Hot");
        happeningNowFilter.setText("Now");

        // Add tabs to the tablayout
        tabLayout.addTab(newFilter, 0);
        tabLayout.addTab(hotFilter, 1);
        tabLayout.addTab(happeningNowFilter, 2);

        // Connect the tab layout to the view pager for swiping
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // Creates a tab listener for the tab layout to change to different view pages [new hot now]
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /*---------------------------------------------------------------------------
    Function Name:                onAttach()
    Description:                  Every time this fragment is attached then create
                                  a new view pager adapter
    Input:                        Context context
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Init view pager adapter
        myHomeFeedViewPagerAdapter = new HomeFeedViewPagerAdapter(getChildFragmentManager(), this);
    }

    /*---------------------------------------------------------------------------
    Function Name:                overrideFonts()
    Description:                  Sets fonts for all TextViews
    Input:                        final Context context
                                  final View v
    Output:                       View to be inflated
    ---------------------------------------------------------------------------*/
    private void overrideFonts(final Context context, final View v) {
        try {

            // If the view is a ViewGroup
            if (v instanceof ViewGroup) {

                ViewGroup vg = (ViewGroup) v;

                // Iterate through ViewGroup children
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);

                    // Call method again for each child
                    overrideFonts(context, child);
                }

                // If the view is a TextView set the font
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "raleway-regular.ttf"));
            }

        }
        catch (Exception e) {
            // Print out error if one is encountered
            System.err.println(e.toString());
        }
    }

    /*---------------------------------------------------------------------------
    Function Name:                onResume()
    Description:                  Every time this fragment comes into view
                                  remove the search bar and define the search type
                                  for searching
    Input:                        None.
    Output:                       None.
   ---------------------------------------------------------------------------*/
    @Override
    public void onResume() {
        super.onResume();

        // Add the search toolbar
        ((MainActivity)getActivity()).addSearchToolbar();

        // Define the search type for search
        ((MainActivity)getActivity()).searchType = getString(R.string.fragment_home_feed);

    }

}


