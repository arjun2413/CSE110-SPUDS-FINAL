package com.spuds.eventapp.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.Event;
import com.spuds.eventapp.Shared.EventsFeedRVAdapter;
import com.spuds.eventapp.Shared.User;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

    // Reference to this fragment
    ProfileFragment profileFragment;

    String profileType;

    ImageView userImage;
    TextView userName;
    ImageView userVerified;
    ImageView buttonSubscribedOrEdit;
    TextView numberFollowing;
    TextView numberHosting;
    RecyclerView eventsHostingRV;
    RecyclerView eventsGoingRV;
    User user;

    List<Event> eventsHosting;
    List<Event> eventsGoing;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getArguments();
        profileType = extras.getString(getString(R.string.profile_type));
        user = (User) extras.getSerializable(getString(R.string.user_details));
        profileFragment = this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        setUpProfileDetails(view);

        return view;
    }

    private void setUpProfileDetails(View view) {

        userImage = (ImageView) view.findViewById(R.id.user_image);
        userName = (TextView) view.findViewById(R.id.user_name);
        userVerified = (ImageView) view.findViewById(R.id.user_verified);
        buttonSubscribedOrEdit = (ImageView) view.findViewById(R.id.button_subscribe);
        numberFollowing = (TextView) view.findViewById(R.id.user_number_following);
        numberHosting = (TextView) view.findViewById(R.id.user_number_hosting);
        eventsHostingRV = (RecyclerView) view.findViewById(R.id.rv_events_hosting);
        eventsGoingRV = (RecyclerView) view.findViewById(R.id.rv_events_going);

        // TODO (M): Picasso for userImage

        userName.setText(user.name);

        if (!user.verified) {
            ((ViewManager) userVerified.getParent()).removeView(userVerified);
        }

        // Set image for button for subscribe or edit profile
        /*if (profileType.equals(getString(R.string.profile_type_owner))) {

            // TODO (V): Uncomment when edit_profile picture is inserted in drawable
            buttonSubscribedOrEdit.setImageResource(R.drawable.edit_profile);
            buttonSubscribedOrEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Add in
                    EditProfileFragment editProfileFragment = new EditProfileFragment();

                    // TODO (C): Add user in a bundle to editProfileFragment

                    profileFragment.getFragmentManager().beginTransaction()
                            .replace(R.id.fragment_frame_layout, editProfileFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(getString(R.string.fragment_edit_profile))
                            .commit();
                }
            });

        } else {

            // TODO (V): Uncomment once get drawables for subscribe buttons
            if (user.subscribed)
                buttonSubscribedOrEdit.setImageResource(R.drawable.button_subscribed);
            else
                buttonSubscribedOrEdit.setImageResource(R.drawable.button_not_subscribed);

            buttonSubscribedOrEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // TODO (M): Update subscribed boolean in database & error checking
                    user.subscribed = !user.subscribed;
                    if (user.subscribed)
                        buttonSubscribedOrEdit.setImageResource(R.drawable.button_subscribed);
                    else
                        buttonSubscribedOrEdit.setImageResource(R.drawable.button_not_subscribed);

                }
            });

        }*/

        numberFollowing.setText(String.valueOf(user.numberFollowing));
        numberHosting.setText(String.valueOf(user.numberHosting));

        setUpRecyclerViewsGoingAndHosting();

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    void setUpRecyclerViewsGoingAndHosting() {
        // List for events hosting
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        eventsHostingRV.setLayoutManager(llm);
        eventsHostingRV.setHasFixedSize(true);

        // TODO (M): Get first three events for events hosting
        eventsHosting = new ArrayList<>();
        eventsHosting.add(new Event("1", "yj.jpg", "Sun God Festival", "RIMAC Field", "04.29.16", 1054,
                "Social", "Concert", "UCSD", "spr lame"));
        eventsHosting.add(new Event("2", "foosh.jpg", "Foosh Show", "Muir", "04.28.16", 51,
                "Social", null, "Foosh Improv Comedy Club", "spr funny"));
        eventsHosting.add(new Event("2", "foosh.jpg", "Foosh Show", "Muir", "04.28.16", 51,
                "Social", null, "Foosh Improv Comedy Club", "spr funny"));
        eventsHosting.add(null);

        EventsFeedRVAdapter eventsFeedRVAdapterHosting = new EventsFeedRVAdapter(eventsHosting, this, getString(R.string.fragment_profile), getString(R.string.tab_hosting), user.userId);
        eventsHostingRV.setAdapter(eventsFeedRVAdapterHosting);


        // List for events hosting
        llm = new LinearLayoutManager(getContext());
        eventsGoingRV.setLayoutManager(llm);
        eventsGoingRV.setHasFixedSize(true);

        // TODO (M): Get first three events for events going
        eventsGoing = new ArrayList<>();
        eventsGoing.add(new Event("1", "yj.jpg", "Sun God Festival", "RIMAC Field", "04.29.16", 1054,
                "Social", "Concert", "UCSD", "spr lame"));
        eventsGoing.add(new Event("2", "foosh.jpg", "Foosh Show", "Muir", "04.28.16", 51,
                "Social", null, "Foosh Improv Comedy Club", "spr funny"));
        eventsGoing.add(new Event("2", "foosh.jpg", "Foosh Show", "Muir", "04.28.16", 51,
                "Social", null, "Foosh Improv Comedy Club", "spr funny"));
        eventsGoing.add(null);

        EventsFeedRVAdapter eventsFeedRVAdapterGoing = new EventsFeedRVAdapter(eventsGoing, this, getString(R.string.fragment_profile), getString(R.string.tab_going), user.userId);
        eventsGoingRV.setAdapter(eventsFeedRVAdapterGoing);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
