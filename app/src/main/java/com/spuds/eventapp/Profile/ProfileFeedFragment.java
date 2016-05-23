package com.spuds.eventapp.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.Event;
import com.spuds.eventapp.Shared.EventsFeedRVAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileFeedFragment extends Fragment {

    List<Event> firstFourEvents;
    List<Event> events;

    String userId;
    String tabType;

    EventsFeedRVAdapter adapter;

    public ProfileFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        events = new ArrayList<>();

        Bundle extras = getArguments();

        userId = extras.getString(getString(R.string.user_id));
        tabType = extras.getString(getString(R.string.tab_tag));

        // TODO (M): Get arraylist of events based on userId & tabType [user going or hosting]
        // Fake data
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Social");
        categories.add("Concert");

        events.add(new Event("1", "2", "Sun God Festival", "spr lame", "RIMAC Field", "04/20/2016|16:20", 1054,
                "yj.jpg", categories, "UCSD"));
        events.add(new Event("2", "2", "Foosh Show", "spr funny", "Muir", "04/20/2016|16:20", 51,
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

        adapter = new EventsFeedRVAdapter(events, this, getString(R.string.fragment_profile_feed));
        rv.setAdapter(adapter);

        //calls the function to refresh the page.
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
                    }
                }
        );
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


}
