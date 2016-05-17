package com.spuds.eventapp.SubscriptionsList;

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
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.Shared.Subscription;

import java.util.ArrayList;
import java.util.List;


public class SubscriptionsListFragment extends Fragment {
    private List<Subscription> subscriptions;
    public SubscriptionsListRVAdapter adapter;

    public SubscriptionsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.recycler, container, false);
        RecyclerView rv=(RecyclerView) v.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        rv.setLayoutManager(llm);

        // TODO (M): database call, populate with real data
        // fake data

        subscriptions = new ArrayList<>();
        subscriptions.add(new Subscription("1", "Arjune", R.drawable.arjun, true));
        subscriptions.add(new Subscription("1", "Jiggly", R.drawable.arjun, true));
        subscriptions.add(new Subscription("1", "18uh", R.drawable.arjun, true));
        subscriptions.add(new Subscription("1", "Jone Ayy Thin", R.drawable.arjun, true));
        subscriptions.add(new Subscription("1", "Kraken", R.drawable.arjun, true));
        subscriptions.add(new Subscription("1", "Regirock", R.drawable.arjun, true));

        adapter = new SubscriptionsListRVAdapter(subscriptions, this);
        rv.setAdapter(adapter);

        return v;
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
