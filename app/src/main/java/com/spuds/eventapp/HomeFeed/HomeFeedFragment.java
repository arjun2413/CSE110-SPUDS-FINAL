package com.spuds.eventapp.HomeFeed;

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
Class Name:                HomeFeedFragment
Description:               Sets up screen for the event feed on the home page
---------------------------------------------------------------------------*/
public class HomeFeedFragment extends Fragment {

    // Type of the tab [how, new, now]
    String tabType;
    // Reference to modle methods
    EventsFirebase eventsFirebase;
    RecyclerView rv;
    public EventsFeedRVAdapter adapter;
    // Contains all event information
    private ArrayList<Event> events;

    /*---------------------------------------------------------------------------
    Function Name:                HomeFeedFragment
    Description:                  Required default no-argument constructor
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public HomeFeedFragment() {
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreate()
    Description:                  Called each time fragment is created; gets
                                  information passed to this fragment; intializes
                                  instance variables; sets up recycler view to show
                                  event information to the user
    Input:                        Bundle savedInstanceState
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get tab type from bundle passed to this fragment
        Bundle extras = getArguments();
        tabType = extras.getString(getString(R.string.tab_tag));

        // Init events list
        events = new ArrayList<>();

        // Init eventsFirebase reference
        eventsFirebase = new EventsFirebase(events, 0, tabType);


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
        // Inflates layout recycler for the home feed
        View view = inflater.inflate(R.layout.recycler, container, false);

        // Sets up the toolbar title
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("EVENTORY");

        // Sets up requirements for recycler view for the feed: layout manager
        rv = (RecyclerView) view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        // Adapter for the recycler view
        adapter = new EventsFeedRVAdapter(events, this, getString(R.string.fragment_home_feed));
        rv.setAdapter(adapter);

        // Wait until the events from the database start filling the events arraylist
        new Thread(new Runnable() {

            @Override
            public void run() {

                // While the events is empty wait
                while (events.size() == 0) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Reset the adapter for the recycler view
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rv.setAdapter(adapter);
                    }
                });

            }
        }).start();

        setupRefresh(view);
        return view;
    }

    /*---------------------------------------------------------------------------
    Function Name:                setupRefresh()
    Description:                  Sets up allowing the user to refresh
    Input:                        final View view
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public void setupRefresh(View view) {
        // Layout view object for setupRefresh
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
                                        Thread.sleep(Integer.parseInt(getString(R.string.sleepTime)));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                // Reset the adapter for the recycler view
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run()
                                    {

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

    /*---------------------------------------------------------------------------
    Function Name:                onResume()
    Description:                  Every time the this fragment comes into view
                                  remove the search toolbar and get the events
                                  again/set up all views again and set up search
    Input:                        None.
    Output:                       None.
   ---------------------------------------------------------------------------*/
    @Override
    public void onResume() {
        super.onResume();

        // Clear all the events in the array list
        events.clear();

        // Get events again from the database
        eventsFirebase.createEL();

        // Add the search toolbar and set up the search by specifying search type
        ((MainActivity)getActivity()).addSearchToolbar();
        ((MainActivity)getActivity()).searchType = getString(R.string.fragment_home_feed);

    }
}


