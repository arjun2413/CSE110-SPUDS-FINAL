package com.spuds.eventapp.InvitePeople;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.User;

import java.util.ArrayList;


public class InvitePeopleFragment extends Fragment {

    ArrayList<User> followers;
    ArrayList<User> invited;

    InvitePeopleRVAdapter adapter;

    public InvitePeopleFragment() {
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
        View view = inflater.inflate(R.layout.recycler, container, false);

        // TODO (M): remember when loading do not override the followers arraylist, add to it

        followers = new ArrayList<>();
        // TODO (M)
        // fake data for followers
        invited = new ArrayList<>();
        followers.add(new User("1", "Reggie Wu", "#wutangclan", true, 100,
                1, "reggie.jpg", false));
        followers.add(new User("1", "Youngjin Yun", "#wutangclan", true, 100,
                1, "reggie.jpg", false));

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        adapter = new InvitePeopleRVAdapter(followers, invited, this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            /*
            case R.id.select_all:
                if (!adapter.selectAll) {

                    for (int i = 0; i < followers.size(); ++i) {
                        if (!invited.contains(followers.get(i))) {
                            invited.add(followers.get(i));
                        }
                    }
                    adapter.selectAll = true;
                    adapter.notifyDataSetChanged();

                } else {

                    for (int i = 0; i < followers.size(); ++i) {
                        if (!invited.contains(followers.get(i))) {
                            invited.remove(followers.get(i));
                        }
                    }
                    adapter.selectAll = false;
                    adapter.notifyDataSetChanged();

                }
                return true;

            case R.id.done:
                if (invited.size() != 0) {
                    // TODO (M): Send invited array to database & do notification
                    // Pop this fragment from backstack
                }
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            */
            default:
                return true;


        }

    }

}
