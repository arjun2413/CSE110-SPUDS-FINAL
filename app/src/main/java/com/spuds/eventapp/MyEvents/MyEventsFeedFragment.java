package com.spuds.eventapp.MyEvents;

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

import java.util.ArrayList;

/*---------------------------------------------------------------------------
Class Name:                MyEventsFeedFragment
Description:               Sets up the feeds going/hosting in my events tab
                           in navigation drawer
---------------------------------------------------------------------------*/
public class MyEventsFeedFragment extends Fragment {

    // Events to be put on the feed
    private ArrayList<Event> events;

    //Adapter for the connecting events to the recycler view
    public EventsFeedRVAdapter adapter;

    // Reference to backend methods
    EventsFirebase eventsFirebase;

    // Tab going/hosting for my events tab in navigation drawer
    String myEventsTab;

    /*---------------------------------------------------------------------------
    Function Name:                MyEventsFeedFragment
    Description:                  Required default no-argument constructor
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public MyEventsFeedFragment() {
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreate()
    Description:                  Called each time fragment is created; initializes
                                  instance variables based on info passed into this
                                  fragment
    Input:                        Bundle savedInstanceState
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get information passed into this fragment
        Bundle extras = getArguments();
        myEventsTab = extras.getString(getString(R.string.tab_tag));

        // Initialize events arraylist
        events = new ArrayList<>();

        // Get arraylist of events based on my events filter [going or hosting]
        eventsFirebase = new EventsFirebase(events, 0, myEventsTab);
        eventsFirebase.createEL();

    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreateView()
    Description:                  Inflates View layout and sets fonts programmatically
                                  Also sets up recycler view for feed and toolbar
    Input:                        LayoutInflater inflater - inflates layout
                                  ViewGroup container - parent view group
                                  Bundle savedInstanceState
    Output:                       View to be inflated
    ---------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflates recycler view for the feed
        View view = inflater.inflate(R.layout.recycler, container, false);

        // Sets title action bar
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My Events");

        // Sets up requirements for recyclerview for the feed: layout manager
        final RecyclerView rv=(RecyclerView) view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        // Adapter for the recycler view
        adapter = new EventsFeedRVAdapter(events, this, getString(R.string.fragment_my_events));
        rv.setAdapter(adapter);

        // Wait until the events from the database start filling the events arraylist
        new Thread(new Runnable() {

            @Override
            public void run() {

                // While the events is empty wait
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
                        // Reset the adapter for the recycler view
                        rv.setAdapter(adapter);
                    }
                });
            }
        }).start();

        refreshing(view);
        return view;
    }

    /*---------------------------------------------------------------------------
    Function Name:                setupRefresh()
    Description:                  Sets up allowing the user to refresh
    Input:                        final View view
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public void refreshing(View view) {
        final SwipeRefreshLayout mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // Clear events arraylist to re-get events from the database
                        events.clear();
                        eventsFirebase.createEL();

                        // Wait until the events from the database start filling the events arraylist
                        new Thread(new Runnable() {

                            @Override
                            public void run() {

                                // While the events is empty wait
                                while (events.size() == 0) {
                                    try {
                                        Thread.sleep(70);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run()
                                    {
                                        // Notify the adapter that the data has changed
                                        adapter.notifyDataSetChanged();

                                        // Stop the refresh icon from showing up
                                        mySwipeRefreshLayout.setRefreshing(false);

                                    }
                                });
                            }
                        }).start();
                    }
                }
        );
    }

}


