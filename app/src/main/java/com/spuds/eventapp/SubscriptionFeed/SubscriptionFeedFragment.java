package com.spuds.eventapp.SubscriptionFeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spuds.eventapp.Firebase.EventsFirebase;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.Event;
import com.spuds.eventapp.Shared.EventsFeedRVAdapter;
import com.spuds.eventapp.Shared.MainActivity;

import java.util.ArrayList;

/*---------------------------------------------------------------------------
Class Name:                SubscriptionFeedFragment
Description:               Contains information about Subscription Feed Fragment
---------------------------------------------------------------------------*/
public class SubscriptionFeedFragment extends Fragment {

    private ArrayList<Event> events;
    public EventsFeedRVAdapter adapter;
    EventsFirebase eventsFirebase;

    /*---------------------------------------------------------------------------
    Function Name:                SubscriptionFeedFragment
    Description:                  Required default no-argument constructor
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public SubscriptionFeedFragment() {
        //required
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
    Input:                        LayoutInflater inflater - inflates layout
                                  ViewGroup container - parent view group
                                  Bundle savedInstanceState
    Output:                       View to be inflated
    ---------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Subscription Feed");
        final RecyclerView rv=(RecyclerView) view.findViewById(R.id.rv);

        //set the layout manager to be a linear one
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        //initialize events list
        events = new ArrayList<>();
        //create eventsFirebase object
        eventsFirebase = new EventsFirebase(events, 0, null);
        eventsFirebase.createSubFeed();

        adapter = new EventsFeedRVAdapter(events, this, getString(R.string.fragment_my_sub_feed));
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
        //call setupRefresh function
        setupRefresh(view);

        return view;
    }

    /*---------------------------------------------------------------------------
    Function Name:                setupRefresh()
    Description:                  used to implement the swipe down to refresh functionality
    Input:                        View view: the view to be refreshed
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public void setupRefresh(View view) {
        final SwipeRefreshLayout mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        events.clear();

                        eventsFirebase.createSubFeed();

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

    /*---------------------------------------------------------------------------
    Function Name:                onResume()
    Description:                  called when fragment is resumed
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).addSearchToolbar();
        ((MainActivity)getActivity()).searchType = getString(R.string.fragment_my_sub_feed);
    }

}


