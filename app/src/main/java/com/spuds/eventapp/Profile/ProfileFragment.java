package com.spuds.eventapp.Profile;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.spuds.eventapp.EditProfile.EditProfileFragment;
import com.spuds.eventapp.Firebase.UserFirebase;
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
    TextView userDescription;
    Button buttonSubscribedOrEdit;
    TextView numberFollowing;
    TextView numberHosting;
    RecyclerView eventsHostingRV;
    RecyclerView eventsGoingRV;
    User user;

    List<Event> eventsHosting;
    List<Event> eventsGoing;
    UserFirebase userFirebase;

    ProfileViewPagerAdapter profileViewPagerAdapter;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getArguments();
        profileType = extras.getString(getString(R.string.profile_type));

        if (profileType.equals(getString(R.string.profile_type_other))) {

            user = (User) extras.getSerializable(getString(R.string.user_details));
            Log.v("profilefragmnet", "user name " + user.getName());

        } else {
            user = UserFirebase.thisUser;
        }

        userFirebase = new UserFirebase();
        userFirebase.isSubscribed(user.getUserId());

        profileFragment = this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        overrideFonts(view.getContext(),view);

        Typeface raleway_medium = Typeface.createFromAsset(getActivity().getAssets(),  "Raleway-Medium.ttf");

        //title font
        TextView name = (TextView) view.findViewById(R.id.user_name);
        name.setTypeface(raleway_medium);

        /*TextView hosting = (TextView) view.findViewById(R.id.label_events_hosting);
        hosting.setTypeface(raleway_medium);

        TextView going = (TextView) view.findViewById(R.id.label_events_going);
        going.setTypeface(raleway_medium);*/

        TextView sub_num = (TextView) view.findViewById(R.id.user_number_following);
        sub_num.setTypeface(raleway_medium);

        TextView events_num = (TextView) view.findViewById(R.id.user_number_hosting);
        events_num.setTypeface(raleway_medium);

        Button subscribe = (Button) view.findViewById(R.id.button_subscribe);
        subscribe.setTypeface(raleway_medium);

        setUpProfileDetails(view);
        tabs(view);
        overrideFonts(view.getContext(),view);

        return view;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpProfileDetails(View view) {

        userImage = (ImageView) view.findViewById(R.id.user_image);
        userName = (TextView) view.findViewById(R.id.user_name);
        buttonSubscribedOrEdit = (Button) view.findViewById(R.id.button_subscribe);
        numberFollowing = (TextView) view.findViewById(R.id.user_number_following);
        numberHosting = (TextView) view.findViewById(R.id.user_number_hosting);
        /*eventsHostingRV = (RecyclerView) view.findViewById(R.id.rv_events_hosting);
        eventsGoingRV = (RecyclerView) view.findViewById(R.id.rv_events_going);*/
        userDescription = (TextView) view.findViewById(R.id.user_description);

        // TODO (M): userImage

        userDescription.setText(user.getDescription());
        userName.setText(user.getName());
       /* Bitmap src = BitmapFactory.decodeResource(this.getResources(), R.drawable.christinecropped);
        RoundedBitmapDrawable dr =
                RoundedBitmapDrawableFactory.create(this.getResources(), src);
        dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);*/


        String imageFile = user.getPicture();

        if (imageFile != null) {
            Bitmap src = null;
            try {
                byte[] imageAsBytes = Base64.decode(imageFile, Base64.DEFAULT);
                src = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            } catch (OutOfMemoryError e) {
                System.err.println(e.toString());
            }

            if (src != null) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), src);
                circularBitmapDrawable.setCircular(true);
                circularBitmapDrawable.setAntiAlias(true);
                userImage.setImageDrawable(circularBitmapDrawable);
            } else {
                src = BitmapFactory.decodeResource(getResources(), R.drawable.profile_pic_icon);

                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), src);
                circularBitmapDrawable.setCircular(true);
                circularBitmapDrawable.setAntiAlias(true);
                userImage.setImageDrawable(circularBitmapDrawable);
            }
        } else {
            Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.profile_pic_icon);

            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(getResources(), src);
            circularBitmapDrawable.setCircular(true);
            circularBitmapDrawable.setAntiAlias(true);
            userImage.setImageDrawable(circularBitmapDrawable);
        }

        // Set image for button for subscribe or edit profile
        if (user.getUserId().equals(UserFirebase.uId)) {

            Log.v("ProfileFragment", "own profile!");

            buttonSubscribedOrEdit.setText("Edit Profile");

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

            buttonSubscribedOrEdit.setText("Subscribe");

            Log.v("ProfileFragment", "other not own profile!");

            buttonSubscribedOrEdit.setBackgroundTintList(getResources().getColorStateList(R.color.color_selected));


            new Thread(new Runnable() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    Log.d("idIsGoing2",String.valueOf(userFirebase.idIsSubscribed));
                    while (userFirebase.idIsSubscribed == 0) {
                        Log.d("profilehere", "profilehere");
                        try {
                            Thread.sleep(75);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                    Log.d("idsubProfileFragment", String.valueOf(userFirebase.idIsSubscribed));


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (userFirebase.idIsSubscribed == 1) {
                                user.setSubscribed(false);
                            } else {
                                user.setSubscribed(true);
                            }

                            if (user.isSubscribed()) {
                                buttonSubscribedOrEdit.setBackgroundTintList(getResources().getColorStateList(R.color.color_selected));
                            } else {
                                buttonSubscribedOrEdit.setBackgroundTintList(getResources().getColorStateList(R.color.color_unselected));
                            }

                            buttonSubscribedOrEdit.setOnClickListener(new View.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onClick(View v) {

                                    user.setSubscribed(!user.isSubscribed());
                                    Log.v("Profile Fragment", "subscribed = " + user.isSubscribed());

                                    UserFirebase userFirebase = new UserFirebase();
                                    userFirebase.subscribe(user.getUserId(), user.isSubscribed());
                                    // TODO (V): coloorzz
                                    if (user.isSubscribed())
                                        buttonSubscribedOrEdit.setBackgroundTintList(getResources().getColorStateList(R.color.color_selected));
                                    else
                                        buttonSubscribedOrEdit.setBackgroundTintList(getResources().getColorStateList(R.color.color_unselected));

                                }
                            });
                        }
                    });
                }
            }).start();


        }

        numberFollowing.setText(String.valueOf(user.getNumberFollowing()));
        numberHosting.setText(String.valueOf(user.getNumberHosting()));

        //setUpRecyclerViewsGoingAndHosting();

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
                "", categories, "UCSD"));
        eventsHosting.add(new Event("2", "2", "Foosh Show", "spr funny", "Muir", "04/20/2016|16:20", 51,
                "", categories, "Foosh Improv Comedy Club"));
        eventsHosting.add(new Event("2", "2", "Foosh Show", "spr funny", "Muir", "04/20/2016|16:20", 51,
                "", categories, "Foosh Improv Comedy Club"));
        eventsHosting.add(null);

        EventsFeedRVAdapter eventsFeedRVAdapterHosting = new EventsFeedRVAdapter(eventsHosting, this, getString(R.string.fragment_profile), getString(R.string.tab_hosting), user.getUserId());
        eventsHostingRV.setAdapter(eventsFeedRVAdapterHosting);


        // List for events hosting
        llm = new LinearLayoutManager(getContext());
        eventsGoingRV.setLayoutManager(llm);
        eventsGoingRV.setHasFixedSize(true);

        // TODO (M): Get first three events for events going
        eventsGoing = new ArrayList<>();


        eventsGoing.add(new Event("1", "2", "Sun God Festival", "spr lame", "RIMAC Field", "04/20/2016|16:20", 1054,
                "", categories, "UCSD"));
        eventsGoing.add(new Event("2", "2", "Foosh Show", "spr funny", "Muir", "04/20/2016|16:20", 51,
                "", categories, "Foosh Improv Comedy Club"));
        eventsGoing.add(new Event("2", "2", "Foosh Show", "spr funny", "Muir", "04/20/2016|16:20", 51,
                "", categories, "Foosh Improv Comedy Club"));
        eventsGoing.add(null);

        EventsFeedRVAdapter eventsFeedRVAdapterGoing = new EventsFeedRVAdapter(eventsGoing, this, getString(R.string.fragment_profile), getString(R.string.tab_going), user.getUserId());
        eventsGoingRV.setAdapter(eventsFeedRVAdapterGoing);
    }

    private void tabs(View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.profile_tabs);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);

        viewPager.setAdapter(profileViewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        final TabLayout.Tab going = tabLayout.newTab();
        final TabLayout.Tab hosting = tabLayout.newTab();

        going.setText("Going");
        hosting.setText("Hosting");

        tabLayout.addTab(going, 0);
        tabLayout.addTab(hosting, 1);


        //tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.tab_selector));
        //tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.indicator));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        profileViewPagerAdapter = new ProfileViewPagerAdapter(getChildFragmentManager(), this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView ) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "raleway-regular.ttf"));
            }
        }
        catch (Exception e) {
        }
    }
}
