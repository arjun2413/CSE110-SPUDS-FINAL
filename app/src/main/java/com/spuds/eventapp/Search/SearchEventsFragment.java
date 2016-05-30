package com.spuds.eventapp.Search;

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
import com.spuds.eventapp.Shared.Event;
import com.spuds.eventapp.Shared.Subscription;

import java.util.ArrayList;

/**
 * Created by David on 5/28/16.
 */
public class SearchEventsFragment extends Fragment {
    private ArrayList<Event> events;
    public SearchEventsRVAdapter adapter;
    UserFirebase userFirebase;

    public SearchEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //userFirebase = new UserFirebase();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.recycler, container, false);
        final RecyclerView rv=(RecyclerView) v.findViewById(R.id.rv);

        events = new ArrayList<>();


        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        rv.setLayoutManager(llm);


        adapter = new SearchEventsRVAdapter(events, this, "Search Events Fragment");
        rv.setAdapter(adapter);

        /*
        new Thread(new Runnable() {
            //TODO: Change firebase calls to events instead of subscriptions

            @Override
            public void run() {
                while (userFirebase.numSubscriptions > events.size() || !userFirebase.getSubscriptionsThreadCheck) {
                    //("sublist", "numsubs" + userFirebase.numSubscriptions);
                    //("sublist", "subscriptions size" + events.size());
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
        */



        //calls the function to refresh the page.
        refreshing(v);

        return v;
    }
    //TODO: Needs database to finish
    public void refreshing(View view) {
        final SwipeRefreshLayout mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        events.clear();

                        //TODO: Get events from firebase

                        //("refresh", "here");
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                //("refresh", "hereherehere");

                                //TODO: Change firebase calls to events instead of subscriptions
                                /*
                                while (userFirebase.numSubscriptions > events.size() || !userFirebase.getSubscriptionsThreadCheck) {
                                    ////("refresh", "size: " + events.size());
                                    try {
                                        Thread.sleep(70);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                */

                                //("refresh", "here2");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run()
                                    {
                                        //("refresh", "here3");

                                        adapter.notifyDataSetChanged();
                                        mySwipeRefreshLayout.setRefreshing(false);

                                    }
                                });
                            }
                        }).start();
                    }
                }
        );
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


}
