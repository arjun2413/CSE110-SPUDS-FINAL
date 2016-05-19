package com.spuds.eventapp.SubscriptionsList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

        // TODO (M): database call, populate with real data
        // fake data

        subscriptions = new ArrayList<>();
        subscriptions.add(new Subscription("1", "Arjun", R.drawable.arjun, true));
        subscriptions.add(new Subscription("1", "Jiggly", R.drawable.arjun, true));
        subscriptions.add(new Subscription("1", "Joe", R.drawable.arjun, true));
        subscriptions.add(new Subscription("1", "Kraken", R.drawable.arjun, true));
        subscriptions.add(new Subscription("1", "Regirock", R.drawable.arjun, true));

        adapter = new SubscriptionsListRVAdapter(subscriptions, this);
        rv.setAdapter(adapter);

        refreshing(v);

        return v;
    }
    //TODO: Needs database to finish
    public void refreshing(View view){
        SwipeRefreshLayout mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                    }
                }
        );
    }

}
