package com.spuds.eventapp.FilteredCategoryFeed;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spuds.eventapp.Firebase.EventsFirebase;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.Event;
import com.spuds.eventapp.Shared.EventsFeedRVAdapter;

import java.util.ArrayList;

public class CategoryFeedTabsFragment extends Fragment {

    CategoryFeedViewPagerAdapter categoryFeedViewPagerAdapter;

    private ArrayList<Event> events;
    private ArrayList<String> cat;
    public EventsFeedRVAdapter adapter;
    String tabType;
    String catType;
    EventsFirebase eventsFirebase;
    public CategoryFeedTabsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        catType = bundle.getString(getString(R.string.category_bundle));
        //Log.v("jkl;", ""+catType);
        //categoryFeedViewPagerAdapter = new CategoryFeedViewPagerAdapter(getChildFragmentManager(), this, catType);

        events = new ArrayList<>();

        // TODO (M): Get arraylist of events based on tab type [new, hot, now]
        // Fake data
        eventsFirebase = new EventsFirebase(events, 0, tabType, catType, adapter);
        eventsFirebase.createEL();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_feed_tabs, container, false);
        //tabs(view);

        View view = inflater.inflate(R.layout.recycler, container, false);

        final RecyclerView rv=(RecyclerView) view.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        adapter = new EventsFeedRVAdapter(events, this, getString(R.string.fragment_category_feed));
        rv.setAdapter(adapter);

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (events.size() == 0) {
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rv.setAdapter(adapter);
                    }
                });
            }
        }).start();

        //call refreshing function
        refreshing(view);
        return view;
    }

    //TODO: Needs database to finish
    public void refreshing(View view) {
        final SwipeRefreshLayout mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        events.clear();

                        eventsFirebase.createEL();

                        Log.v("refresh", "here");
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                Log.v("refresh", "hereherehere");

                                while (events.size() == 0) {
                                    //Log.v("refresh", "size: " + events.size());
                                    try {
                                        Thread.sleep(70);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                Log.v("refresh", "here2");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run()
                                    {
                                        Log.v("refresh", "here3");

                                        adapter.notifyDataSetChanged();
                                        mySwipeRefreshLayout.setRefreshing(false);

                                    }
                                });
                            }
                        }).start();
                    }
                }
        );

    }

    private void tabs(View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.profile_tabs);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);

        viewPager.setAdapter(categoryFeedViewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);

        final TabLayout.Tab newFilter = tabLayout.newTab();
        final TabLayout.Tab hotFilter = tabLayout.newTab();
        final TabLayout.Tab happeningNowFilter = tabLayout.newTab();

        newFilter.setText("New");
        hotFilter.setText("Hot");
        happeningNowFilter.setText("Now");

        tabLayout.addTab(newFilter, 0);
        tabLayout.addTab(hotFilter, 1);
        tabLayout.addTab(happeningNowFilter, 2);


        //tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.tab_selector));
        //tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.indicator));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
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
        });    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


}
