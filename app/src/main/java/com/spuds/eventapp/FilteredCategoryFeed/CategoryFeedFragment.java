package com.spuds.eventapp.FilteredCategoryFeed;

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

public class CategoryFeedFragment extends Fragment {

    private ArrayList<Event> events;
    private ArrayList<String> cat;
    public EventsFeedRVAdapter adapter;
    String tabType;
    String catType;
    EventsFirebase eventsFirebase;

    public CategoryFeedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getArguments();
        tabType = extras.getString(getString(R.string.tab_tag));
        catType = extras.getString("Category Type");

        events = new ArrayList<>();
        //("cattypecattype", "cattye" + catType);
        System.err.println("cattype" + catType);

        // TODO (M): Get arraylist of events based on tab type [new, hot, now]
        // Fake data
        eventsFirebase = new EventsFirebase(events, 0, tabType, catType, adapter);
        eventsFirebase.createEL();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler, container, false);

        Log.v("cattypecattype", "cattye" + catType);
        if( catType.equals(getString(R.string.cat_food))) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Food");
        }
        else if( catType.equals(getString(R.string.cat_social))) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Social");
        }
        else if( catType.equals(getString(R.string.cat_concerts))) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Concerts");
        }
        else if( catType.equals(getString(R.string.cat_sports))) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Sports");
        }
        else if( catType.equals(getString(R.string.cat_student_orgs))) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Student Orgs");
        }
        else if( catType.equals(getString(R.string.cat_academic))) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Academic");
        }
        else if(catType.equals(getString(R.string.cat_free))){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Free");
        }
        else {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Poop");
        }

        final RecyclerView rv=(RecyclerView) view.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        adapter = new EventsFeedRVAdapter(events, this, getString(R.string.fragment_category_feed));
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
        refreshing(view);
        return view;
    }
    //TODO: Needs database to finish
    public void refreshing(View view) {
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


