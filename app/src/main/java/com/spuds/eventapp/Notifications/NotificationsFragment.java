package com.spuds.eventapp.Notifications;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spuds.eventapp.R;
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
        super.onCreate(savedInstanceState);

        notificationList = new ArrayList<Notification>();
        // TODO (M): get notifications for the user
        //fake data
        notificationList.add(new Notification("Invite Notification", "1", "1", "eventorprofile.jpg",
                "David Shan", "04/20/2016|04:20 PM", "420 Blaze It"));
        notificationList.add(new Notification("Update Notification", "1", "1", "eventorprofile.jpg",
                "A.S. Concert", "04/20/2016|04:20 PM", "Sun God"));
        notificationList.add(new Notification("Reply Notification", "1", "1", "eventorprofile.jpg",
                "Reggie Wu", "04/20/2016|04:20 PM", "Birthday Party", "Will there be food?"));

        notificationsRVAdapter = new NotificationsRVAdapter(notificationList, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler, container, false);

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.setAdapter(notificationsRVAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
