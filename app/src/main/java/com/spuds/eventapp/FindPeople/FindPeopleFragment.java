package com.spuds.eventapp.FindPeople;

import android.content.Context;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.Shared.User;

import java.util.ArrayList;

/*---------------------------------------------------------------------------
Class Name:                FindPeopleFragment
Description:               Fragment that contains information about the
                           find people screen (search for users)
---------------------------------------------------------------------------*/
public class FindPeopleFragment extends Fragment {
    ArrayList<User> people;
    FindPeopleRVAdapter adapter;


    /*---------------------------------------------------------------------------
    Function Name:                FindPeopleFragment
    Description:                  Required default no-argument constructor
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public FindPeopleFragment() {
        // Required empty public constructor
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
        setHasOptionsMenu(true);

        Bundle bundle = new Bundle();

        people = (ArrayList<User>) bundle.getSerializable("Search People Array List");
        if (people == null) {
            people = new ArrayList<>();
        }
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

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Find People");

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

    /*---------------------------------------------------------------------------
    Function Name:                refreshing()
    Description:                  called every time the screen is refreshed
    Input:                        View view: the view to be refreshed
    Output:                       None.
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
    Function Name:                onCreateOptionsMenu()
    Description:                  called every time the system starts the activity
                                  to show items in the app bar
    Input:                        View view: the view to be refreshed
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.removeItem(R.id.action_create_event);
        //ginflater.inflate(R.menu.invite_people, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /*---------------------------------------------------------------------------
    Function Name:                onResume()
    Description:                  called every time the fragment is resumed
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).addSearchToolbar();
        ((MainActivity)getActivity()).searchType = getString(R.string.fragment_find_people);
    }

}
