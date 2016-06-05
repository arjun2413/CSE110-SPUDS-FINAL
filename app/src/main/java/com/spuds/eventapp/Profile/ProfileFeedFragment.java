package com.spuds.eventapp.Profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
Class Name:                ProfileFeedFragment
Description:               Sets up screen for the going/hosting feed on the profile
---------------------------------------------------------------------------*/
public class ProfileFeedFragment extends Fragment {

    // Contains all event information
    ArrayList<Event> events;

    // Reference to user id and tab type
    String userId;
    String tabType;

    // Reference to backend/model methods
    EventsFirebase eventsFirebase;
    // Adapter for connecting the information for events to the view/laout
    EventsFeedRVAdapter adapter;

    /*---------------------------------------------------------------------------
    Function Name:                HomeFeedFragment
    Description:                  Required default no-argument constructor
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public ProfileFeedFragment() {
        // Required empty public constructor
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

        // Initialize events arraylist
        events = new ArrayList<>();

        // Get user and tab information from bundle passed into this fragment
        Bundle extras = getArguments();
        userId = extras.getString(getString(R.string.user_id));
        tabType = extras.getString(getString(R.string.tab_tag));

        // Init events firebase reference
        eventsFirebase = new EventsFirebase(events, 0, null);

        // Call the methods to firebase to get events for going and hosting tabs
        if (tabType.equals(getString(R.string.tab_going))) {
            eventsFirebase.getEventsGoingList(userId);
        } else {
            eventsFirebase.getEventsHostingList(userId);
        }

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
        // Inflates layout recycler for the profile feed
        View view = inflater.inflate(R.layout.recycler, container, false);

        // Sets up requirements for recycler view for the feed: layout manager
        final RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        // Adapter for the recycler view
        adapter = new EventsFeedRVAdapter(events, this, getString(R.string.fragment_profile_feed));
        rv.setAdapter(adapter);

        // If the tab for the profile feed is going
        if (tabType.equals(getString(R.string.tab_going))) {
            // Wait until the events from the database start filling the events array list
            new Thread(new Runnable() {
                @Override
                public void run() {

                    // While the getting the events for going, wait
                    while (!eventsFirebase.goingListThreadCheck) {
                        try {
                            Thread.sleep(75);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Notify to the adapter that the events has changed
                            adapter.notifyDataSetChanged();
                        }
                    });

                }
            }).start();

        // If the tab for the profile feed is hosting
        } else {
            // Wait until the events from the database start filling the events array list
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // While the getting the events for hosting, wait
                    while (!eventsFirebase.hostingListThreadCheck) {
                        try {
                            Thread.sleep(75);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // notify the adapter that the data has changed
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();
        }

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
        // Init refresh layout
        SwipeRefreshLayout mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        // Set a refresh listener
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                    }
                }
        );
    }

}
