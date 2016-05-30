package com.spuds.eventapp.HomeFeed;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.spuds.eventapp.Firebase.EventsFirebase;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.Event;
import com.spuds.eventapp.Shared.EventsFeedRVAdapter;

import java.util.ArrayList;

public class HomeFeedFragment extends Fragment {

    private ArrayList<Event> events;
    public EventsFeedRVAdapter adapter;
    String tabType;
    RecyclerView rv;

    EventsFirebase eventsFirebase;

    public HomeFeedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Firebase.setAndroidContext();
        super.onCreate(savedInstanceState);

        Bundle extras = getArguments();
        tabType = extras.getString(getString(R.string.tab_tag));

        events = new ArrayList<>();

        // TODO (M): Get arraylist of events based on tab type [new, hot, now]
        // Fake data
        eventsFirebase = new EventsFirebase(events, 0, tabType);
        eventsFirebase.createEL();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("EVENTORY");

        rv = (RecyclerView) view.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        adapter = new EventsFeedRVAdapter(events, this, getString(R.string.fragment_home_feed));
        rv.setAdapter(adapter);

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (events.size() == 0) {
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

        //call refreshing function
        setupRefresh(view);
        return view;
    }
    //TODO: Needs database to finish
    public void setupRefresh(View view) {
        final SwipeRefreshLayout mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        events.clear();

                        eventsFirebase.createEL();

                        //("refresh", "here");
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                //("refresh", "hereherehere");

                                while (events.size() == 0) {
                                    ////("refresh", "size: " + events.size());
                                    try {
                                        Thread.sleep(70);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

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
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}


