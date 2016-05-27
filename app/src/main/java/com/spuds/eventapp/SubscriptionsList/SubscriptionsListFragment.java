package com.spuds.eventapp.SubscriptionsList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spuds.eventapp.Firebase.UserFirebase;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.Subscription;

import java.util.ArrayList;


public class SubscriptionsListFragment extends Fragment {
    private ArrayList<Subscription> subscriptions;
    public SubscriptionsListRVAdapter adapter;
    UserFirebase userFirebase;
    public boolean done;

    public SubscriptionsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userFirebase = new UserFirebase();
        subscriptions = new ArrayList<>();
        userFirebase.getSubscriptions(subscriptions);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.recycler, container, false);
        final RecyclerView rv=(RecyclerView) v.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        rv.setLayoutManager(llm);


        adapter = new SubscriptionsListRVAdapter(subscriptions, this);
        rv.setAdapter(adapter);

        if (!done) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    while (userFirebase.numSubscriptions > subscriptions.size() || !userFirebase.getSubscriptionsThreadCheck) {
                        Log.v("sublist", "numsubs" + userFirebase.numSubscriptions);
                        Log.v("sublist", "subscriptions size" + subscriptions.size());
                        try {
                            Thread.sleep(70);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("inviteppl", "" + subscriptions.size());
                            Log.v("inviteppl", "" + subscriptions.get(0).name);
                            Log.v("inviteppl", "" + subscriptions.get(1).name);

                            rv.setAdapter(adapter);
                            done = true;
                        }
                    });
                }
            }).start();
        }



        //calls the function to refresh the page.
        refreshing(v);

        return v;
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
