package com.spuds.eventapp.SubscriptionFeed;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Social");
        categories.add("Concert");

        events.add(new Event("1", "2", "Sun God Festival", "spr lame", "RIMAC Field", "04.29.16", 1054,
                "yj.jpg", categories, "UCSD"));
        events.add(new Event("2", "2", "Foosh Show", "spr funny", "Muir", "04.28.16", 51,
                "foosh.jpg", categories, "Foosh Improv Comedy Club"));
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
}
