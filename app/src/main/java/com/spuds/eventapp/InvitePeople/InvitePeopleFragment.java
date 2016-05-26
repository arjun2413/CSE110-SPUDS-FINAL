package com.spuds.eventapp.InvitePeople;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.spuds.eventapp.Shared.User;

import java.util.ArrayList;


public class InvitePeopleFragment extends Fragment {

    ArrayList<User> followers;
    ArrayList<User> invited;

    InvitePeopleRVAdapter adapter;

    EventsFirebase eventsFirebase;

    public InvitePeopleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // TODO (M): remember when loading do not override the followers arraylist, add to it
        followers = new ArrayList<>();
        invited = new ArrayList<>();
        eventsFirebase = new EventsFirebase();
        eventsFirebase.getFollowers(followers);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler, container, false);


        final RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        adapter = new InvitePeopleRVAdapter(followers, invited, this);
        rv.setAdapter(adapter);

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (eventsFirebase.numFollowers > followers.size() || !eventsFirebase.followersThreadCheck) {
                    Log.v("inviteppl", "numfollowers" + eventsFirebase.numFollowers);
                    Log.v("inviteppl", "followers size" + followers.size());
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.v("inviteppl",""+ followers.size());
                        rv.setAdapter(adapter);
                    }
                });
            }
        }).start();

        //calling refresh function
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.removeItem(R.id.action_create_event);
        inflater.inflate(R.menu.invite_people, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {

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

            default:
                return true;


        }

    }

}
