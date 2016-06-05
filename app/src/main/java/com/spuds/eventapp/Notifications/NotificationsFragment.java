package com.spuds.eventapp.Notifications;

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

import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.Shared.Notification;

import java.util.ArrayList;
import java.util.List;

/*---------------------------------------------------------------------------
Class Name:                NotificationsFragment
Description:               Contains information about Notifications Fragment
---------------------------------------------------------------------------*/
public class NotificationsFragment extends Fragment {

    private List<Notification> notificationList;
    public NotificationsRVAdapter notificationsRVAdapter;

/*---------------------------------------------------------------------------
    Function Name:                NotificationsFragment
    Description:                  Required default no-argument constructor
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public NotificationsFragment() {
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreate()
    Description:                  Called each time fragment is created
    Input:                        Bundle savedInstanceState
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // create list of notifications
        notificationList = new ArrayList<Notification>();
        //fake data
        notificationList.add(new Notification("Invite Notification", "1", "1", "",
                "David ShanSrinivsan Ma Nguyen", "04/05/25 | 16:20", "420 Blaze It"));
        notificationList.add(new Notification("Update Notification", "1", "1", "",
                "A.S. Concertcatsreggie", "16/05/25 | 16:20", "Sun God"));
        notificationList.add(new Notification("Reply Notification", "1", "1", "",
                "Reggie Wu", "16/05/25 | 16:20", "Birthday Party", "Will there be food?"));

        notificationsRVAdapter = new NotificationsRVAdapter(notificationList, this);

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
        View view = inflater.inflate(R.layout.recycler, container, false);

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Notifications");

        //set the layout manager to linear
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.setAdapter(notificationsRVAdapter);

        //call refreshing function
        refreshing(view);

        return view;
    }

    /*---------------------------------------------------------------------------
    Function Name:                refreshing
    Description:                  called when user pulls down to refresh the page
    Input:                        View view
    Output:                       View
    ---------------------------------------------------------------------------*/
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

    /*---------------------------------------------------------------------------
    Function Name:                onResume()
    Description:                  called when fragment is resumed
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).removeSearchToolbar();
    }

}
