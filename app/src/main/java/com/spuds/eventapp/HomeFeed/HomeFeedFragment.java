package com.spuds.eventapp.HomeFeed;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.Event;
import com.spuds.eventapp.Shared.EventsFeedRVAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFeedFragment extends Fragment {

    private List<Event> events;
    public EventsFeedRVAdapter adapter;
    String tabType;

    public HomeFeedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getArguments();
        tabType = extras.getString(getString(R.string.tab_tag));

        // TODO (M): Get arraylist of events based on tab type [new, hot, now]
        // Fake data
        events = new ArrayList<>();
        events.add(new Event("1", "yj.jpg", "Sun God Festival", "RIMAC Field", "April 30, 2016", 1054,
                "Social", "Concert", "UCSD", "spr lame"));
        events.add(new Event("2", "foosh.jpg", "Foosh Show", "Muir", "January 1, 2016", 51,
                "Social", null, "Foosh Improv Comedy Club", "spr funny"));
        events.add(new Event("3", "foosh.jpg", "Christine Wu Birthday Party", "Apt 121", "May 6, 2016", 51,
                "Social", null, "Christine Wu", "wow so cool"));
        events.add(new Event("4", "foosh.jpg", "Reggie Wu GBM #1", "Reggie's Apartment", "June 17, 2016", 60,
                "Social", "Academic", "Reggie's Fan Club", "wow reggie is so cool"));
        events.add(new Event("5", "foosh.jpg", "Karaoke Party", "Karoke 101", "July 30, 2016", 405,
                "Social", "Food", "TASA", "wow so fun"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler, container, false);
        RecyclerView rv=(RecyclerView) view.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        adapter = new EventsFeedRVAdapter(events, this, getString(R.string.fragment_home_feed));
        rv.setAdapter(adapter);

        return view;
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


