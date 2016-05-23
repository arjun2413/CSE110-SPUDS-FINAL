package com.spuds.eventapp.FindPeople;

import android.content.Context;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.User;

import java.util.ArrayList;


public class FindPeopleFragment extends Fragment {
    ArrayList<User> people;
    FindPeopleRVAdapter adapter;

    public FindPeopleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        people = new ArrayList<>();
        View view = inflater.inflate(R.layout.recycler, container, false);

        //fake data
        people.add(new User("1", "Kevin Huang", "#meatballs", true, 100, 1, "christinecropped.jpg", false));
        people.add(new User("1", "Arjun Huang", "#meatballs", true, 100, 1, "christinecropped.jpg", false));
        people.add(new User("1", "Christine Wu", "#meatballs", true, 100, 1, "christinecropped.jpg", false));
        people.add(new User("1", "Yung Jin", "#meatballs", true, 100, 1, "christinecropped.jpg", false));
        people.add(new User("1", "Jon Putin", "#meatballs", true, 100, 1, "christinecropped.jpg", false));
        people.add(new User("1", "Vladmir Purplenuts", "#meatballs", true, 100, 1, "christinecropped.jpg", false));
        

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        adapter = new FindPeopleRVAdapter(people, this);
        rv.setAdapter(adapter);

        //calling refresh function
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.removeItem(R.id.action_create_event);
        //ginflater.inflate(R.menu.invite_people, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
