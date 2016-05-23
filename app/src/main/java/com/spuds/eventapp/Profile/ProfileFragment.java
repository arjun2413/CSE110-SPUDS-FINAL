package com.spuds.eventapp.Profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.EditProfile.EditProfileFragment;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.Event;
import com.spuds.eventapp.Shared.EventsFeedRVAdapter;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.Shared.User;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

    // Reference to this fragment
    ProfileFragment profileFragment;

    String profileType;
    String userId;

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

        userId = extras.getString(getString(R.string.user_id));
        // TODO (M): GET request to get user details using userId
        //fake data
        user = new User("1", "Reggie Wu", "#wutangclan", true, 100,
                1, "reggie.jpg", false);

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
        Bitmap src = BitmapFactory.decodeResource(this.getResources(), R.drawable.arjun);
        RoundedBitmapDrawable dr =
                RoundedBitmapDrawableFactory.create(this.getResources(), src);
        dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
        userImage.setImageDrawable(dr);


        if (!user.verified) {
            ((ViewManager) userVerified.getParent()).removeView(userVerified);
        }



        // Set image for button for subscribe or edit profile
        if (profileType.equals(getString(R.string.profile_type_owner))) {

            // TODO (V): Uncomment when edit_profile picture is inserted in drawable
            //buttonSubscribedOrEdit.setImageResource(R.drawable.edit_profile);
            buttonSubscribedOrEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Add in
                    EditProfileFragment editProfileFragment = new EditProfileFragment();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(getString(R.string.user_details), user);
                    editProfileFragment.setArguments(bundle);

                    ((MainActivity) getActivity()).removeSearchToolbar();
                    profileFragment.getFragmentManager().beginTransaction()
                            .replace(R.id.fragment_frame_layout, editProfileFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(getString(R.string.fragment_edit_profile))
                            .commit();
                }
            });

        } else {

            // TODO (V): Uncomment once get drawables for subscribe buttons
            /*if (user.subscribed)
                buttonSubscribedOrEdit.setImageResource(R.drawable.button_subscribed);
            else
                buttonSubscribedOrEdit.setImageResource(R.drawable.button_not_subscribed);*/

            buttonSubscribedOrEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // TODO (M): Update subscribed boolean in database & error checking
                    user.subscribed = !user.subscribed;
                    /*if (user.subscribed)
                        buttonSubscribedOrEdit.setImageResource(R.drawable.button_subscribed);
                    else
                        buttonSubscribedOrEdit.setImageResource(R.drawable.button_not_subscribed);*/

                }
            });

        }

        numberFollowing.setText(String.valueOf(user.numberFollowing));
        numberHosting.setText(String.valueOf(user.numberHosting));

        setUpRecyclerViewsGoingAndHosting();

    }

    void setUpRecyclerViewsGoingAndHosting() {
        // List for events hosting
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        eventsHostingRV.setLayoutManager(llm);
        eventsHostingRV.setHasFixedSize(true);

        // TODO (M): Get first three events for events hosting
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Social");
        categories.add("Concert");
        eventsHosting = new ArrayList<>();
        eventsHosting.add(new Event("1", "2", "Sun God Festival", "spr lame", "RIMAC Field", "04/20/2016|16:20", 1054,
                "yj.jpg", categories, "UCSD"));
        eventsHosting.add(new Event("2", "2", "Foosh Show", "spr funny", "Muir", "04/20/2016|16:20", 51,
                "foosh.jpg", categories, "Foosh Improv Comedy Club"));
        eventsHosting.add(new Event("2", "2", "Foosh Show", "spr funny", "Muir", "04/20/2016|16:20", 51,
                "foosh.jpg", categories, "Foosh Improv Comedy Club"));
        eventsHosting.add(null);

        EventsFeedRVAdapter eventsFeedRVAdapterHosting = new EventsFeedRVAdapter(eventsHosting, this, getString(R.string.fragment_profile), getString(R.string.tab_hosting), user.userId);
        eventsHostingRV.setAdapter(eventsFeedRVAdapterHosting);


        // List for events hosting
        llm = new LinearLayoutManager(getContext());
        eventsGoingRV.setLayoutManager(llm);
        eventsGoingRV.setHasFixedSize(true);

        // TODO (M): Get first three events for events going
        eventsGoing = new ArrayList<>();


        eventsGoing.add(new Event("1", "2", "Sun God Festival", "spr lame", "RIMAC Field", "04/20/2016|16:20", 1054,
                "yj.jpg", categories, "UCSD"));
        eventsGoing.add(new Event("2", "2", "Foosh Show", "spr funny", "Muir", "04/20/2016|16:20", 51,
                "foosh.jpg", categories, "Foosh Improv Comedy Club"));
        eventsGoing.add(new Event("2", "2", "Foosh Show", "spr funny", "Muir", "04/20/2016|16:20", 51,
                "foosh.jpg", categories, "Foosh Improv Comedy Club"));
        eventsGoing.add(null);

        EventsFeedRVAdapter eventsFeedRVAdapterGoing = new EventsFeedRVAdapter(eventsGoing, this, getString(R.string.fragment_profile), getString(R.string.tab_going), user.userId);
        eventsGoingRV.setAdapter(eventsFeedRVAdapterGoing);
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
