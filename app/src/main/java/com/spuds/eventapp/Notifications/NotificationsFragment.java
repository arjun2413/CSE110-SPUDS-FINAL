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


public class NotificationsFragment extends Fragment {

    private List<Notification> notificationList;
    public NotificationsRVAdapter notificationsRVAdapter;

    public NotificationsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        /*Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText("Notifications");*/
        super.onCreate(savedInstanceState);

        notificationList = new ArrayList<Notification>();
        // TODO (M): get real notifications for the user, commented out because causes crashes with date
        //fake data

        notificationList.add(new Notification("Invite Notification", "1", "1", "",
                "David ShanSrinivsan Ma Nguyen", "04/05/25 | 16:20", "420 Blaze It"));
        notificationList.add(new Notification("Update Notification", "1", "1", "",
                "A.S. Concertcatsreggie", "16/05/25 | 16:20", "Sun God"));
        notificationList.add(new Notification("Reply Notification", "1", "1", "",
                "Reggie Wu", "16/05/25 | 16:20", "Birthday Party", "Will there be food?"));

        notificationsRVAdapter = new NotificationsRVAdapter(notificationList, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler, container, false);

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Notifications");

        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.setAdapter(notificationsRVAdapter);

        //call refreshing function
        refreshing(view);

        return view;
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

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).removeSearchToolbar();
    }

}
