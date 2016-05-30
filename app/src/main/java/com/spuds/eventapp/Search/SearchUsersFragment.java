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
import com.spuds.eventapp.Shared.Subscription;

import java.util.ArrayList;

/**
 * Created by David on 5/28/16.
 */
public class SearchUsersFragment extends Fragment {
    private ArrayList<Subscription> users;
    public SearchUsersRVAdapter adapter;
    UserFirebase userFirebase;

    public SearchUsersFragment() {
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

        users = new ArrayList<>();
        //userFirebase.getSubscriptions(users);

        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        rv.setLayoutManager(llm);


        adapter = new SearchUsersRVAdapter(users, this);
        rv.setAdapter(adapter);

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (userFirebase.numSubscriptions > users.size() || !userFirebase.getSubscriptionsThreadCheck) {
                    //("sublist", "numsubs" + userFirebase.numSubscriptions);
                    //("sublist", "subscriptions size" + users.size());
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
                        users.clear();

                        userFirebase.getSubscriptions(users);

                        //("refresh", "here");
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                //("refresh", "hereherehere");

                                while (userFirebase.numSubscriptions > users.size() || !userFirebase.getSubscriptionsThreadCheck) {
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


}
