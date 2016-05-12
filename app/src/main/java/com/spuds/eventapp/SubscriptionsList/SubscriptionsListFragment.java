package com.spuds.eventapp.SubscriptionsList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spuds.eventapp.R;
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

        subscriptions = new ArrayList<>();
        subscriptions.add(new Subscription("Arjun", R.drawable.arjun, true));
        subscriptions.add(new Subscription("Jiggly", R.drawable.arjun, true));
        subscriptions.add(new Subscription("Joe", R.drawable.arjun, true));
        subscriptions.add(new Subscription("Kraken", R.drawable.arjun, true));
        subscriptions.add(new Subscription("Regirock", R.drawable.arjun, true));

        adapter = new SubscriptionsListRVAdapter(subscriptions);
        rv.setAdapter(adapter);

        return v;
    }



}
