package com.spuds.eventapp.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.spuds.eventapp.CreateEvent.CreateEventFragment;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.Event;
import com.spuds.eventapp.Shared.EventsFeedRVAdapter;
import com.spuds.eventapp.Shared.MainActivity;

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
        events.add(new Event("1", "yj.jpg", "Sun God Festival", "RIMAC Field", "04.29.16", 1054,
                "Social", "Concert", "UCSD", "spr lame"));
        events.add(new Event("2", "foosh.jpg", "Foosh Show", "Muir", "04.28.16", 51,
                "Social", null, "Foosh Improv Comedy Club", "spr funny"));

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

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_create_event) {
            CreateEventFragment createEventFragment = new CreateEventFragment();

            ((MainActivity)getActivity()).removeSearchToolbar();
            // Add Event Details Fragment to fragment manager
            this.getFragmentManager().beginTransaction()
                    .show(createEventFragment)
                    .replace(R.id.fragment_frame_layout, createEventFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(getString(R.string.fragment_create_event))
                    .commit();
        }

        return true;
    }


}
