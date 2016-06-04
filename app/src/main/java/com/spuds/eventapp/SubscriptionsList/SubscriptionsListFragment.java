

package com.spuds.eventapp.SubscriptionsList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spuds.eventapp.Firebase.UserFirebase;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.Shared.Subscription;

import java.util.ArrayList;
/*---------------------------------------------------------------------------
Class Name:                SubscriptionsListFragment
Description:               Contains information about Subscriptions List
---------------------------------------------------------------------------*/
public class SubscriptionsListFragment extends Fragment {
    private ArrayList<Subscription> subscriptions;
    public SubscriptionsListRVAdapter adapter;
    UserFirebase userFirebase;

    /*---------------------------------------------------------------------------
    Function Name:                SubscriptionsListFragment
    Description:                  Required default no-argument constructor
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public SubscriptionsListFragment() {
        // Required empty public constructor
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreate
    Description:                  called when the fragment is created
    Input:                        Bundle savedInstanceState
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userFirebase = new UserFirebase();

    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreateView()
    Description:                  Inflates View layout and sets fonts programmatically
    Input:                        LayoutInflater inflater - inflates layout
                                  ViewGroup container - parent view group
                                  Bundle savedInstanceState
    Output:                       View to be inflated
    ---------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.recycler, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Subscriptions");
        final RecyclerView rv=(RecyclerView) v.findViewById(R.id.rv);
        //create array list to hold a user's subscriptions
        subscriptions = new ArrayList<>();

        //get the subscriptions from firebase
        userFirebase.getSubscriptions(subscriptions);

        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        rv.setLayoutManager(llm);

        //create and set an adapter
        adapter = new SubscriptionsListRVAdapter(subscriptions, this);
        rv.setAdapter(adapter);

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (userFirebase.numSubscriptions > subscriptions.size() || !userFirebase.getSubscriptionsThreadCheck) {
                    //("sublist", "numsubs" + userFirebase.numSubscriptions);
                    //("sublist", "subscriptions size" + subscriptions.size());
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

        refreshing(v);

        return v;
    }

    /*---------------------------------------------------------------------------
    Function Name:                refreshing
    Description:                  called when user pulls down to refresh the page
    Input:                        View view
    Output:                       View
    ---------------------------------------------------------------------------*/
    public void refreshing(View view) {
        final SwipeRefreshLayout mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        subscriptions.clear();

                        userFirebase.getSubscriptions(subscriptions);

                        //("refresh", "here");
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                //("refresh", "hereherehere");

                                while (userFirebase.numSubscriptions > subscriptions.size() || !userFirebase.getSubscriptionsThreadCheck) {
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


    /*---------------------------------------------------------------------------
    Function Name:                onResume()
    Description:                  called every time the About Fragment comes into view
                                 remove the search toolbar
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).addSearchToolbar();
        ((MainActivity)getActivity()).searchType = getString(R.string.fragment_my_sub);
    }

}
