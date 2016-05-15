package com.spuds.eventapp.SubscriptionFeed;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.Event;
import com.spuds.eventapp.Shared.EventsFeedRVAdapter;

import java.util.ArrayList;
import java.util.List;


public class SubscriptionFeedFragment extends Fragment {

    private List<Event> events;
    public EventsFeedRVAdapter adapter;

    String tabType;

    public SubscriptionFeedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getArguments();
        tabType = extras.getString(getString(R.string.tab_tag));

        // TODO (M): Get arraylist of events based on tab type [new, hot, now]
        // Fake data
        events = new ArrayList<>();
        events.add(new Event("1", "yj.jpg", "SunGod", "UCSD", "04.29.16", 1054,
                "Social", "Concert", "UCSD", "desc"));
        events.add(new Event("2", "foosh.jpg", "Foosh Show", "Muir", "04.28.16", 51,
                "Social", null, "Foosh Improv Comedy Club", "desc"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler, container, false);
        RecyclerView rv=(RecyclerView) view.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        adapter = new EventsFeedRVAdapter(events, this, getString(R.string.fragment_my_sub_feed));
        rv.setAdapter(adapter);

        return view;    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
