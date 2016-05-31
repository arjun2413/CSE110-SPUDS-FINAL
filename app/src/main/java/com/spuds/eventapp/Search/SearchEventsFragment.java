package com.spuds.eventapp.Search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spuds.eventapp.Firebase.EventsFirebase;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.Event;
import com.spuds.eventapp.Shared.MainActivity;

import java.util.ArrayList;

/**
 * Created by David on 5/28/16.
 */
public class SearchEventsFragment extends Fragment {
    private ArrayList<Event> events;
    public SearchEventsRVAdapter adapter;
    EventsFirebase eventsFirebase;
    String eventId;

    public SearchEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventsFirebase = new EventsFirebase();
        Bundle bundle = getArguments();
        eventId = bundle.getString(getString(R.string.event_id));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.recycler_no_refresh, container, false);
        final RecyclerView rv=(RecyclerView) v.findViewById(R.id.rv);

        events = new ArrayList<>();


        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        rv.setLayoutManager(llm);


        adapter = new SearchEventsRVAdapter(events, this, "Search Events Fragment");
        rv.setAdapter(adapter);

        eventsFirebase.getEventDetails(eventId);


        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!EventsFirebase.detailsThreadCheck) {
                    try {
                        //("sleepingthread","fam");

                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                EventsFirebase.eventDetailsEvent.setEventId(eventId);

                events.add(EventsFirebase.eventDetailsEvent);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyItemInserted(0);
                    }
                });

            }
        }).start();

        //calls the function to refresh the page.
        //refreshing(v);

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


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).addSearchToolbar();
    }

}
