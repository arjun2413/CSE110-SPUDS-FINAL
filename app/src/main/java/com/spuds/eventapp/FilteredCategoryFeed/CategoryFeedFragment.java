package com.spuds.eventapp.FilteredCategoryFeed;

import android.content.Context;
import android.graphics.Typeface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView;

import com.spuds.eventapp.CreateEvent.CreateEventRVAdapter;
import com.spuds.eventapp.Firebase.EventsFirebase;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.Event;
import com.spuds.eventapp.Shared.EventsFeedRVAdapter;
import com.spuds.eventapp.Shared.MainActivity;

import java.util.ArrayList;

public class CategoryFeedFragment extends Fragment {

    private ArrayList<Event> events;
    private ArrayList<String> cat;
    public EventsFeedRVAdapter adapter;
    String tabType;
    String catType;

    public CategoryFeedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getArguments();
        tabType = extras.getString(getString(R.string.tab_tag));
        catType = extras.getString("Category Type");

        events = new ArrayList<>();

        // TODO (M): Get arraylist of events based on tab type [new, hot, now]
        // Fake data
        EventsFirebase ef = new EventsFirebase(events, 0, tabType, catType, adapter);
        ef.createEL();


        /*events.add(new Event("1", "yj.jpg", "Sun God Festival", "RIMAC Field", "April 30, 2016", 1054,
                "Social", "Concert", "UCSD", "spr lame"));
        events.add(new Event("2", "foosh.jpg", "Foosh Show", "Muir", "May 5, 2016", 51,
               "Social", null, "Foosh Improv Comedy Club", "spr funny"));
        events.add(new Event("3", "foosh.jpg", "Circle K GBM #1", "Center 101", "July 4, 2016", 51,
                "Social", null, "Foosh Improv Comedy Club", "spr funny"));
        events.add(new Event("4", "foosh.jpg", "David's Birthday Party", "420 Strip Club", "May 17, 2016", 51,
                "Social", "Food", "Foosh Improv Comedy Club", "spr funny"));
        events.add(new Event("5", "foosh.jpg", "Reggie Wu Fan Club", "Reggie's Apartment", "June 7, 2016", 51,
                "Social", null, "Foosh Improv Comedy Club", "spr funny"));*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        SwipeRefreshLayout mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //EventsFirebase ef = new EventsFirebase(events, 0, tabType);
                        //ef.createEL();
                    }
                }
        );

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}


