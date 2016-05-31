package com.spuds.eventapp.InvitePeople;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.spuds.eventapp.Firebase.EventsFirebase;
import com.spuds.eventapp.Firebase.UserFirebase;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.Shared.PushBuilder;
import com.spuds.eventapp.Shared.User;

import java.util.ArrayList;


public class InvitePeopleFragment extends Fragment {

    ArrayList<User> followers;
    ArrayList<User> invited;
    InvitePeopleRVAdapter adapter;
    EventsFirebase eventsFirebase;
    String eventId;

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

        Bundle bundle = getArguments();
        eventId = bundle.getString(getString(R.string.event_id));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_no_refresh, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Invite People");
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
                    //("inviteppl", "numfollowers" + eventsFirebase.numFollowers);
                    //("inviteppl", "followers size" + followers.size());
                    try {
                        Thread.sleep(Integer.parseInt(getString(R.string.sleepTime)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        rv.setAdapter(adapter);
                    }
                });
            }

        }).start();

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

           /*case R.id.select_all:
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
                return true;*/

            case R.id.done:
                if (invited.size() != 0) {
                    GoogleCloudMessaging gcm = ((MainActivity) getActivity()).gcm;
                    ArrayList<String> inviteNotif = new ArrayList<>();

                    for (User user : invited) {
                        inviteNotif.add(user.getUserId());
                    }

                    PushBuilder push_builder = new PushBuilder(inviteNotif, eventId, UserFirebase.thisUser.getName(), UserFirebase.uId, gcm);
                    push_builder.sendNotification();

                }
                getActivity().getSupportFragmentManager().popBackStack();
                return true;

            default:
                return true;


        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).addSearchToolbar();
        ((MainActivity)getActivity()).searchType = getString(R.string.fragment_invite_people);
    }

}
