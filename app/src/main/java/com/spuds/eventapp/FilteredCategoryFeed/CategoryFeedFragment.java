package com.spuds.eventapp.FilteredCategoryFeed;

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
Class Name:                CategoriesListFragment
Description:               Sets up screen for listing feed for the specified
                           category
---------------------------------------------------------------------------*/
public class CategoryFeedFragment extends Fragment {

    // Holds events for the feed
    private ArrayList<Event> events;
    // Adapter to transfer events to the layout/screen/view
    public EventsFeedRVAdapter adapter;

    // Reference to category type
    String catType;

    // Reference to database calls
    EventsFirebase eventsFirebase;

    /*---------------------------------------------------------------------------
    Function Name:                CategoryFeedFragment
    Description:                  Required default no-argument constructor
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public CategoryFeedFragment() {
        // Required empty public constructor
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

        // Init events arraylist
        events = new ArrayList<>();

        // Get category type from previous fragment
        Bundle bundle = getArguments();
        catType = bundle.getString(getString(R.string.category_bundle));


        // Get array list of events based on category type from database
        eventsFirebase = new EventsFirebase(events, 0, "", catType, adapter);
        eventsFirebase.createEL();
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreateView()
    Description:                  Inflates View layout and sets fonts programmatically
                                  Also sets up toolbar and recyclerview
    Input:                        LayoutInflater inflater - inflates layout
                                  ViewGroup container - parent view group
                                  Bundle savedInstanceState
    Output:                       View to be inflated
    ---------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Setup action bar based on the category type and setup search bar by specifying search
        // type category
        if( catType.equals(getString(R.string.cat_food))) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Food");
            ((MainActivity)getActivity()).searchType = getString(R.string.cat_food);
        }
        else if( catType.equals(getString(R.string.cat_social))) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Social");
            ((MainActivity)getActivity()).searchType = getString(R.string.cat_social);
        }
        else if( catType.equals(getString(R.string.cat_concerts))) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Concerts");
            ((MainActivity)getActivity()).searchType = getString(R.string.cat_concerts);
        }
        else if( catType.equals(getString(R.string.cat_sports))) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Sports");
            ((MainActivity)getActivity()).searchType = getString(R.string.cat_sports);
        }
        else if( catType.equals(getString(R.string.cat_student_orgs))) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Student Orgs");
            ((MainActivity)getActivity()).searchType = getString(R.string.cat_student_orgs);
        }
        else if( catType.equals(getString(R.string.cat_academic))) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Academic");
            ((MainActivity)getActivity()).searchType = getString(R.string.cat_academic);
        }
        else if(catType.equals(getString(R.string.cat_free))){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Free");
            ((MainActivity)getActivity()).searchType = getString(R.string.cat_free);
        }
        else {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Poop");
        }

        // Inflate the recycler layout
        View view = inflater.inflate(R.layout.recycler, container, false);

        // Set up recycler view to have a linear layout
        final RecyclerView rv=(RecyclerView) view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        // Create the adapter and set the recyclerview adapter to this adapter
        adapter = new EventsFeedRVAdapter(events, this, getString(R.string.fragment_category_feed));
        rv.setAdapter(adapter);

        // A thread to check if the an event has been added to the event list
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

                // Once the events list is no longer empty
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Set the adapter to the recycler view to show events to the user
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
                        // Clear the events list
                        events.clear();

                        // Get the events again from the database
                        eventsFirebase.createEL();

                        // A thread to check if the an event has been added to the event list
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                while (events.size() == 0) {
                                    ////("refresh", "size: " + events.size());
                                    try {
                                        Thread.sleep(Integer.parseInt(getString(R.string.sleepTime)));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                // Once the events list is no longer empty
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run()
                                    {

                                        // Notify the adapter the data has changed
                                        // updating it to the user
                                        adapter.notifyDataSetChanged();

                                        // Stop refresh icon from showing up
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
                                      remove the search toolbar
        Input:                        None.
        Output:                       None.
       ---------------------------------------------------------------------------*/
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).addSearchToolbar();
    }

}
