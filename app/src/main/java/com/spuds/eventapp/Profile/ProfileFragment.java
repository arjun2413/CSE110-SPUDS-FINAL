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
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.EditProfile.EditProfileFragment;
import com.spuds.eventapp.Firebase.UserFirebase;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.Shared.User;

/*---------------------------------------------------------------------------
Class Name:                ProfileFragment
Description:               Sets up screen for showing correct profile details
---------------------------------------------------------------------------*/
public class ProfileFragment extends Fragment {

    // Reference to this fragment
    ProfileFragment profileFragment;

    // Reference to profile type: own or other
    String profileType;

    // View objects to be manipulated
    ImageView userImage;
    TextView userName;
    TextView userDescription;
    Button buttonSubscribedOrEdit;
    TextView numberFollowing;
    TextView numberHosting;

    // Holds all user information
    User user;

    UserFirebase userFirebase;

    // Prevents multiple clicks
    boolean canClickSubscribe = true;


    // View pager/tab layout adapter for profile going/hosting
    ProfileViewPagerAdapter profileViewPagerAdapter;

    /*---------------------------------------------------------------------------
    Function Name:                MyEventsFeedFragment
    Description:                  Required default no-argument constructor
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public ProfileFragment() {
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreate()
    Description:                  Called each time fragment is created; gets
                                  information about the user passed to the

    Input:                        Bundle savedInstanceState
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get information about profile type
        Bundle extras = getArguments();
        profileType = extras.getString(getString(R.string.profile_type));

        // If the profile is nto your own get user information
        if (profileType.equals(getString(R.string.profile_type_other))) {

            user = (User) extras.getSerializable(getString(R.string.user_details));

        } else {
            // Otherwise get user information from userfireabse
            user = UserFirebase.thisUser;
        }

        // Init reference to call backend methods
        userFirebase = new UserFirebase();

        // Init reference to this fragment
        profileFragment = this;

    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreateView()
    Description:                  Inflates View layout and sets fonts programmatically
                                  and setting up fonts
    Input:                        LayoutInflater inflater - inflates layout
                                  ViewGroup container - parent view group
                                  Bundle savedInstanceState
    Output:                       View to be inflated
    ---------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflates profile fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Set the title of the action bar based on the user's name
        if (profileType.equals(getString(R.string.profile_type_other))) {

            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(user.getName());

        } else {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(UserFirebase.thisUser.getName());
        }

        // Update the fonts for all textviews
        overrideFonts(view.getContext(),view);

        // Have a reference to the custom fonts
        Typeface raleway_medium = Typeface.createFromAsset(getActivity().getAssets(),  "Raleway-Medium.ttf");

        // Change the fonts for the following views
        TextView name = (TextView) view.findViewById(R.id.user_name);
        name.setTypeface(raleway_medium);

        Button subscribe = (Button) view.findViewById(R.id.button_subscribe);
        subscribe.setTypeface(raleway_medium);

        setUpProfileDetails(view);
        setupTabs(view);

        return view;
    }

    /*---------------------------------------------------------------------------
    Function Name:                setUpProfileDetails()
    Description:                  Sets up the going and hosting tabs
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpProfileDetails(View view) {

        // Reference to view objects to later be implemented
        userImage = (ImageView) view.findViewById(R.id.user_image);
        userName = (TextView) view.findViewById(R.id.user_name);
        buttonSubscribedOrEdit = (Button) view.findViewById(R.id.button_subscribe);
        numberFollowing = (TextView) view.findViewById(R.id.user_number_following);
        numberHosting = (TextView) view.findViewById(R.id.user_number_hosting);
        userDescription = (TextView) view.findViewById(R.id.user_description);

        // Set the user information to the text views
        userDescription.setText(user.getDescription());
        userName.setText(user.getName());

        // Keeps track of the user's picture
        String imageFile = user.getPicture();

        // If the user has a profile picture
        if (imageFile != null) {

            //Try changing the picture into a bitmap
            Bitmap src = null;
            try {
                byte[] imageAsBytes = Base64.decode(imageFile, Base64.DEFAULT);
                src = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            } catch (OutOfMemoryError e) {
                System.err.println(e.toString());
            }

            // If the bitmap was created successfully
            if (src != null) {

                // Change the bitmap to a circle
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), src);
                circularBitmapDrawable.setCircular(true);
                circularBitmapDrawable.setAntiAlias(true);

                // Load the circle image to the picture view for the user
                userImage.setImageDrawable(circularBitmapDrawable);
            // If the bitmap was not created successfully
            } else {

                // Create a bitmap from default profile pic icon
                src = BitmapFactory.decodeResource(getResources(), R.drawable.profile_pic_icon);

                // Change the bitmap to a circle
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), src);
                circularBitmapDrawable.setCircular(true);
                circularBitmapDrawable.setAntiAlias(true);

                // Load the stock profile pic icon circle image to the user picture view for the user
                userImage.setImageDrawable(circularBitmapDrawable);
            }
        // If the user does not have a profile picture
        } else {
            Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.profile_pic_icon);

            // Change the bitmap to a circle
            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(getResources(), src);
            circularBitmapDrawable.setCircular(true);
            circularBitmapDrawable.setAntiAlias(true);

            // Load the stock profile pic icon circle image to the user picture view for the user
            userImage.setImageDrawable(circularBitmapDrawable);
        }

        // If this is the user's own profile
        if (user.getUserId().equals(UserFirebase.uId)) {

            // Set text for button edit profile
            buttonSubscribedOrEdit.setText("Edit Profile");

            // Set image for button for subscribe or edit profile
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
        // If this is another user's profile
        } else {

            // Check if the user has been subscribed
            userFirebase.isSubscribed(user.getUserId());

            // Set the text of the button to subscribe
            buttonSubscribedOrEdit.setText("Subscribe");

            // Set up a default color for the subscribe button
            buttonSubscribedOrEdit.setBackgroundTintList(getResources().getColorStateList(R.color.color_unselected));

            // Wait for the backend to check if the current user is subscribed or not
            new Thread(new Runnable() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    while (userFirebase.idIsSubscribed == 0) {
                        try {
                            Thread.sleep(75);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // If the user is not subscribed (1) update user subscribe boolean to false
                            if (userFirebase.idIsSubscribed == 1) {
                                user.setSubscribed(false);
                            // If the user is not subscribed (2) update user subscribe boolean to true
                            } else {
                                user.setSubscribed(true);
                            }

                            // Whether the user is subscribed or not, change the color of the subscribe button
                            if (user.isSubscribed()) {
                                buttonSubscribedOrEdit.setBackgroundTintList(getResources().getColorStateList(R.color.color_selected));
                            } else {
                                buttonSubscribedOrEdit.setBackgroundTintList(getResources().getColorStateList(R.color.color_unselected));
                            }

                            // Set up the on click listener for subscribe button
                            buttonSubscribedOrEdit.setOnClickListener(new View.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onClick(View v) {

                                    // If you are not spamming the subscribe button
                                    if (canClickSubscribe) {

                                        // Update subscribe variables
                                        user.setSubscribed(!user.isSubscribed());
                                        canClickSubscribe = false;

                                        // Init reference to backend
                                        userFirebase = new UserFirebase();

                                        // Make a call to firebase to update subscribing
                                        userFirebase.subscribe(user.getUserId(), user.isSubscribed());

                                        // Whether the user is subscribed or not, change the color of the subscribe button
                                        if (user.isSubscribed())
                                            buttonSubscribedOrEdit.setBackgroundTintList(getResources().getColorStateList(R.color.color_selected));
                                        else
                                            buttonSubscribedOrEdit.setBackgroundTintList(getResources().getColorStateList(R.color.color_unselected));


                                        new Thread(new Runnable() {

                                            @Override
                                            public void run() {
                                                // If updating to the firebase about subscriptions, sleep
                                                while (!userFirebase.subscribeThreadCheck) {
                                                    try {
                                                        Thread.sleep(70);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                // When done, allow the user to click the button again
                                                canClickSubscribe = true;
                                            }
                                        }).start();
                                    }
                                }
                            });
                        }
                    });


                }
            }).start();


        }

        // Update the following views with the correct user information
        numberFollowing.setText(String.valueOf(user.getNumberFollowing()));
        numberHosting.setText(String.valueOf(user.getNumberHosting()));

    }

    /*---------------------------------------------------------------------------
    Function Name:                setupTabs()
    Description:                  Sets up the going and hosting tabs
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    private void setupTabs(View view) {
        // Layout view for the tabs
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.profile_tabs);

        // Page view for the tabs
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);

        // Sets an adapter for the page viewer limiting number of tabs to 3
        viewPager.setAdapter(profileViewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        // Creates new tabs for the tablayout
        final TabLayout.Tab going = tabLayout.newTab();
        final TabLayout.Tab hosting = tabLayout.newTab();

        // Sets text for the tabs
        going.setText("Going");
        hosting.setText("Hosting");

        // Add tabs to the tablayout
        tabLayout.addTab(going, 0);
        tabLayout.addTab(hosting, 1);

        // Connect the tab layout to the view pager for swiping
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // Creates a tab listener for the tab layout to change to different view pages [new hot now]
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

    /*---------------------------------------------------------------------------
    Function Name:                onAttach()
    Description:                  Every time this fragment is attached then create
                                  a new view pager adapter
    Input:                        Context context
    Output:                       None.
   ---------------------------------------------------------------------------*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Reference to the fragment
        final Fragment fragment = this;

        // Create a new thread checking if the user is not null
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Wait if the user is null
                while(user == null){
                    try {
                        Thread.sleep(75);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // If hte user is no longer null, create a new view pager adapter
                profileViewPagerAdapter = new ProfileViewPagerAdapter(getChildFragmentManager(), fragment, user.getUserId());

            }
        }).start();
    }


    /*---------------------------------------------------------------------------
    Function Name:                overrideFonts()
    Description:                  Sets fonts for all TextViews
    Input:                        final Context context
                                  final View v
    Output:                       View to be inflated
    ---------------------------------------------------------------------------*/
    private void overrideFonts(final Context context, final View v) {
        try {

            // If the view is a ViewGroup
            if (v instanceof ViewGroup) {

                ViewGroup vg = (ViewGroup) v;

                // Iterate through ViewGroup children
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);

                    // Call method again for each child
                    overrideFonts(context, child);
                }

                // If the view is a TextView set the font
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "raleway-regular.ttf"));
            }

        }
        catch (Exception e) {
            // Print out error if one is encountered
            System.err.println(e.toString());
        }
    }

    /*---------------------------------------------------------------------------
    Function Name:                onResume()
    Description:                  Every time the this fragment comes into view
                                  add the search toolbar
    Input:                        None.
    Output:                       None.
   ---------------------------------------------------------------------------*/
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).removeSearchToolbar();
    }

}
