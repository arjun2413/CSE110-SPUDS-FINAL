package com.spuds.eventapp.Shared;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.soundcloud.android.crop.Crop;
import com.spuds.eventapp.About.AboutFragment;
import com.spuds.eventapp.CategoriesList.CategoriesListFragment;
import com.spuds.eventapp.CreateEvent.CreateEventFragment;
import com.spuds.eventapp.FindPeople.FindPeopleFragment;
import com.spuds.eventapp.Firebase.AccountFirebase;
import com.spuds.eventapp.Firebase.EventsFirebase;
import com.spuds.eventapp.Firebase.UserFirebase;
import com.spuds.eventapp.HomeFeed.HomeFeedTabsFragment;
import com.spuds.eventapp.Login.LoginActivity;
import com.spuds.eventapp.MyEvents.MyEventsTabsFragment;
import com.spuds.eventapp.Notifications.NotificationsFragment;
import com.spuds.eventapp.Profile.ProfileFragment;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Search.SearchEventsFragment;
import com.spuds.eventapp.Search.SearchUsersFragment;
import com.spuds.eventapp.Settings.SettingsFragment;
import com.spuds.eventapp.SubscriptionFeed.SubscriptionFeedFragment;
import com.spuds.eventapp.SubscriptionsList.SubscriptionsListFragment;

import java.io.File;
import java.util.ArrayList;

/*---------------------------------------------------------------------------
Class Name:                MainActivity
Description:               Initializes and controls all the fragments,
                           takes care of image picking, search, navigation
                           drawer
---------------------------------------------------------------------------*/
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Reference to all fragments having to do with navigation drawer
    Fragment currentFragment;
    HomeFeedTabsFragment homeFeedTabsFragment;
    NotificationsFragment notificationsFragment;
    CategoriesListFragment categoriesListFragment;
    MyEventsTabsFragment myEventsTabsFragment;
    SubscriptionsListFragment subscriptionsListFragment;
    FindPeopleFragment findPeopleFragment;
    SubscriptionFeedFragment subscriptionFeedFragment;
    AboutFragment aboutFragment;
    SettingsFragment settingsFragment;

    // Navigtion drawer: Profile
    View headerView;
    NavigationView navigationView;
    TextView name;
    public Uri picture;
    ImageView profilePic;

    // Search
    SearchBox search;
    public String searchType;
    ArrayList<SubEvent> testEventsList;
    ArrayList<SubUser> testUsersList;
    ArrayList <String> searchResult;
    ArrayList <SubUser> subscriptions;
    // Reference to the view's object information for search
    RelativeLayout.LayoutParams params;

    // Boolean checking if this is created for the first time
    boolean first = true;

    // Notifications
    public String token;
    public GoogleCloudMessaging gcm;

    // Reference to backend get account information
    AccountFirebase af;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        af = new AccountFirebase();

        setupNotifications(); // set up GCM values

        setupFragments();
        setupMainToolbar();
        setupSearchToolbar();
        setupDrawer();
        setupProfileDrawer();

        // Initialize array lists for search
        searchResult = new ArrayList<>();
        testUsersList = UserFirebase.subUsers;
        testEventsList = EventsFirebase.subEvents;

    }

    /*---------------------------------------------------------------------------
    Function Name:                setupNotifications()
    Description:                  Sets up GCM for the phone to use
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    void setupNotifications() {
        // If gcm has not been initialized yet, initialize it
        if (gcm == null) {
            gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
        }

        // Create a new intent service for registration service and start it
        Intent i = new Intent(this, RegistrationService.class);
        startService(i);

        // Checks if registration service token has been initailized
        new Thread(new Runnable() {

            @Override
            public void run() {
                // If not intialized it, wait
                while (RegistrationService.token == null) {
                    try {
                        Thread.sleep(75);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Set instance variable for the token
                token = RegistrationService.token;

                // Push the token to backend
                af.pushRegistrationId(token);

            }
        }).start();
    }

    /*---------------------------------------------------------------------------
    Function Name:                setupProfileDrawer()
    Description:                  Sets up the user information in the drawer
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public void setupProfileDrawer() {

        // If this is run for the first time
        if (first) {

            // Get view objects having to do with user info in drawer
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            headerView = navigationView.inflateHeaderView(R.layout.nav_header_profile);

            // Set the fonts for all views in the drawer
            overrideFonts(navigationView.getContext(), navigationView);
            overrideFonts(headerView.getContext(), headerView);

            // Initialize view objects having to do with the profile
            name = (TextView) headerView.findViewById(R.id.user_name);
            profilePic = (ImageView) headerView.findViewById(R.id.profile_pic);

            // Reset first inst. variable
            first = false;
        }

        // If the name exists, set the name in the navigation drawer
        if (name != null) {
            name.setText(UserFirebase.thisUser.getName());
        }

        // Reference to the string user image
        String imageFile = UserFirebase.thisUser.getPicture();

        // If the image exists
        if (imageFile != null) {

            // Try changing the picture into a bitmap
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
                profilePic.setImageDrawable(circularBitmapDrawable);
            // If the bitmap was not created successfully
            } else {

                // try to get bitmap from default profile pic icon
                try {
                    src = BitmapFactory.decodeResource(getResources(), R.drawable.profile_pic_icon);

                    // Create a circle from previous bitmap
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), src);
                    circularBitmapDrawable.setCircular(true);
                    circularBitmapDrawable.setAntiAlias(true);

                    // Load the stock profile pic icon circle image to the picture view for the user
                    profilePic.setImageDrawable(circularBitmapDrawable);
                } catch(OutOfMemoryError e) {
                    System.err.println(e.toString());
                }
            }
        // If the user does not have a profile picture
        } else {
            // Get bitmap from default profile pic icon
            Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.profile_pic_icon);

            // Create a circle from previous bitmap
            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(getResources(), src);
            circularBitmapDrawable.setCircular(true);
            circularBitmapDrawable.setAntiAlias(true);

            // Load the stock profile pic icon circle image to the picture view for the user
            profilePic.setImageDrawable(circularBitmapDrawable);
        }
    }

    /*---------------------------------------------------------------------------
    Function Name:                setupMainToolbar()
    Description:                  Sets up toolbar as action bar and set font
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    void setupMainToolbar() {

        // Create reference to toolbar setting it as default action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Change font of action bar
        overrideFonts(toolbar.getContext(),toolbar);

    }

    /*---------------------------------------------------------------------------
    Function Name:                setupSearchToolbar()
    Description:                  Takes care of search depending on what fragment
                                  currently in
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    void setupSearchToolbar() {

        // Initialize search object
        search = (SearchBox) findViewById(R.id.searchbox);

        // Set fonts for all objects having to do with search
        overrideFonts(search.getContext(),search);

        // Set text, color, and log for the search bar
        search.setLogoText("Search for Events");
        search.setLogoTextColor(Color.parseColor("#bfbfbf"));
        search.setDrawerLogo(R.drawable.search);

        // Initialize search result searchResultList
        final ArrayList<SearchResult> searchResultList = new ArrayList<SearchResult>();

        // Set the searchables arraylist
        search.setSearchables(searchResultList);

        // Set up the search listener
        search.setSearchListener(new SearchBox.SearchListener() {

            // When the user clicks the "enter" or "search" button on their keybaord to search
            @Override
            public void onSearch(final String searchTerm) {

                // Depending on the search type; call the corresponding method for search
                if (searchType.equals(getString(R.string.fragment_home_feed))) {
                    findAnyEvent(searchTerm);
                } else if (searchType.equals(getString(R.string.fragment_find_people)) || searchType.equals(getString(R.string.fragment_invite_people))) {
                    findAnyUser(searchTerm);
                } else if (searchType.equals(getString(R.string.fragment_my_events))) {
                    findMyEventsEvent(searchTerm);
                } else if (searchType.equals(getString(R.string.fragment_my_sub_feed))) {
                    findMySubFeedEvent(searchTerm);
                } else if (searchType.equals(getString(R.string.fragment_my_sub))) {
                    findSubUsers(searchTerm);
                } else {
                    findCatFeedEvent(searchTerm);
                }
            }

            //React to a result being clicked
            @Override
            public void onResultClick(SearchResult result) {
            }

            // When a person clicks the search bar, the search opens
            @Override
            public void onSearchOpened() {
                // Depending on the fragment currently in/search type, call the
                // corresponding model method to get the information in an arraylist
                if (searchType.equals(getString(R.string.fragment_home_feed))) {

                    EventsFirebase eventsFirebase = new EventsFirebase();
                    eventsFirebase.getSubEventList();

                } else if (searchType.equals(getString(R.string.fragment_find_people)) || searchType.equals(getString(R.string.fragment_invite_people))) {

                    UserFirebase userFirebase = new UserFirebase();
                    userFirebase.getSubUserList();

                } else if (searchType.equals(getString(R.string.fragment_my_events))) {

                    EventsFirebase eventsFirebase = new EventsFirebase();
                    eventsFirebase.getMyEventsList();

                } else if (searchType.equals(getString(R.string.fragment_my_sub_feed))) {

                    EventsFirebase eventsFirebase = new EventsFirebase();
                    eventsFirebase.createSearchSubFeedList();

                } else if (searchType.equals(getString(R.string.fragment_my_sub))) {

                    subscriptions = new ArrayList<SubUser>();
                    UserFirebase userFirebase = new UserFirebase();
                    userFirebase.getSearchSubs(subscriptions);

                } else {

                    EventsFirebase eventsFirebase = new EventsFirebase();
                    eventsFirebase.getSubEventCatList(searchType);

                }

            }

            // When a person clears the search bar
            @Override
            public void onSearchCleared() {

            }

            // When a person closes the search bar
            @Override
            public void onSearchClosed() {

            }

            // When a person changes the input inside the searchbar
            @Override
            public void onSearchTermChanged(String s) {

            }

        });

        // Get the information about the actual layout/view object for search
        params = (RelativeLayout.LayoutParams) search.getLayoutParams();

    }

    /*---------------------------------------------------------------------------
    Function Name:                findCatFeedEvent()
    Description:                  Gets the information for the category feed
    Input:                        final String searchTerm - term searched by the user
    Output:                       None.
    ---------------------------------------------------------------------------*/
    void findCatFeedEvent(final String searchTerm) {

        // Checks if firebase is done getting the events
        new Thread(new Runnable() {

            @Override
            public void run() {

                //If the category feed list is empty, wait
                while (EventsFirebase.subEventCatList.size() == 0) {
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Assign the list of events from database to testEventsList
                testEventsList = EventsFirebase.subEventCatList;

                // Actually get search results for with the data from the database
                searchEvent(searchTerm);

            }
        }).start();
    }

    /*---------------------------------------------------------------------------
    Function Name:                findCatFeedEvent()
    Description:                  Gets the information for the my subscriptions feed
    Input:                        final String searchTerm - term searched by the user
    Output:                       None.
    ---------------------------------------------------------------------------*/
    void findMySubFeedEvent(final String searchTerm) {

        // Checks if firebase is done getting the events
        new Thread(new Runnable() {

            @Override
            public void run() {
                //If the subscriptions feed list is empty, wait
                while (EventsFirebase.searchSubFeedList.size() == 0) {
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Assign the list of events from database to testEventsList
                testEventsList = EventsFirebase.searchSubFeedList;

                // Actually get search results for with the data from the database
                searchEvent(searchTerm);

            }
        }).start();
    }

    /*---------------------------------------------------------------------------
    Function Name:                findMyEventsEvent()
    Description:                  Gets the information for the my events feed
    Input:                        final String searchTerm - term searched by the user
    Output:                       None.
    ---------------------------------------------------------------------------*/
    void findMyEventsEvent(final String searchTerm) {

        // Checks if firebase is done getting the events
        new Thread(new Runnable() {

            @Override
            public void run() {
                //If the my events feed list is empty, wait
                while (EventsFirebase.myEventsSubEventList.size() == 0) {
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Assign the list of events from database to testEventsList
                testEventsList = EventsFirebase.myEventsSubEventList;

                // Actually get search results for with the data from the database
                searchEvent(searchTerm);
            }
        }).start();
    }

    /*---------------------------------------------------------------------------
    Function Name:                findAnyEvent()
    Description:                  Gets the information for all events
    Input:                        final String searchTerm - term searched by the user
    Output:                       None.
    ---------------------------------------------------------------------------*/
    void findAnyEvent(final String searchTerm) {

        // Checks if firebase is done getting the events
        new Thread(new Runnable() {

            @Override
            public void run() {
                //If the my events list is empty, wait
                while (!EventsFirebase.threadCheckSubEvent) {
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Assign the list of events from database to testEventsList
                testEventsList = EventsFirebase.subEvents;

                // Actually get search results for with the data from the database
                searchEvent(searchTerm);

            }
        }).start();
    }

    /*---------------------------------------------------------------------------
    Function Name:                findSubUsers()
    Description:                  Gets the information for users subscribed to in teh database
    Input:                        final String searchTerm - term searched by the user
    Output:                       None.
    ---------------------------------------------------------------------------*/
    void findSubUsers(final String searchTerm) {

        // Checks if firebase is done getting the users
        new Thread(new Runnable() {
            @Override
            public void run() {

                // While the backend/model is still getting all the users
                while (UserFirebase.numSearchSubs > subscriptions.size() || !UserFirebase.getSearchSubsThreadCheck) {

                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Assign the list of users subscribed to from database to testUsersList
                testUsersList = subscriptions;

                // Actually get search results for with the data from the database
                searchUser(searchTerm);
            }
        }).start();
    }

    /*---------------------------------------------------------------------------
    Function Name:                findAnyUser()
    Description:                  Gets the information for any user to in the database
    Input:                        final String searchTerm - term searched by the user
    Output:                       None.
    ---------------------------------------------------------------------------*/
    void findAnyUser(final String searchTerm) {

        // Checks if firebase is done getting the users
        new Thread(new Runnable() {

            @Override
            public void run() {
                // While the backend/model is still getting all the users
                while (!UserFirebase.threadCheckSubUser) {
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Assign the list of users subscribed to from database to testUsersList
                testUsersList = UserFirebase.subUsers;

                // Actually get search results for with the data from the database
                searchUser(searchTerm);

            }
        }).start();
    }

    /*---------------------------------------------------------------------------
    Function Name:                searchUser()
    Description:                  Adds the user information to a virtual table and
                                  gets one search result
    Input:                        final String searchTerm - term searched by the user
    Output:                       None.
    ---------------------------------------------------------------------------*/
    void searchUser(final String searchTerm) {

        final DatabaseTableSubUser databaseTable = new DatabaseTableSubUser(getApplicationContext(),testUsersList);

        // Checks if database table is done setting up the database table
        new Thread(new Runnable() {
            @Override
            public void run() {

                // While setting up the virtual table is not done, wait
                while(!DatabaseTableSubUser.threadDone){
                    try{
                        Thread.sleep(750);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

                // Find a user that matches the search term
                Cursor cursor = databaseTable.getUserNameMatches(searchTerm, null);

                if (cursor != null && cursor.moveToFirst() ){

                    String[] columnNames = cursor.getColumnNames();

                    do {

                        // Search through all the columns
                        for (String name: columnNames) {

                            // If is the user id column
                            if(name.equals("USER_ID")){

                                // Clear the previous searches, add the user id to the search result
                                searchResult.clear();
                                searchResult.add(cursor.getString(cursor.getColumnIndex(name)));

                                // Parse the string to get everything except the brackets
                                char[] userIdCharArray = searchResult.toString().toCharArray();
                                String userId = "";
                                for (int i = 1; i < userIdCharArray.length - 1; i++) {
                                    userId += userIdCharArray[i];
                                }

                                // Create search users fragment
                                SearchUsersFragment searchUsersFragment = new SearchUsersFragment();

                                // Pass the user id to the search users fragment
                                Bundle bundle = new Bundle();
                                bundle.putString(getString(R.string.user_id), userId);

                                searchUsersFragment.setArguments(bundle);

                                // Change the screen to search users fragment
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_frame_layout, searchUsersFragment)
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .addToBackStack("Search People Fragment")
                                        .commit();
                            }

                        }
                    } while (cursor.moveToNext());
                }
                // No search results found
                else{

                    // Change the screen to an empty search users fragment
                    SearchUsersFragment searchUsersFragment = new SearchUsersFragment();

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_frame_layout, searchUsersFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack("Search People Fragment")
                            .commit();
                }

            }
        }).start();

    }

    /*---------------------------------------------------------------------------
    Function Name:                searchEvent()
    Description:                  Adds the user information to a virtual table and
                                  gets one search result
    Input:                        final String searchTerm - term searched by the user
    Output:                       None.
    ---------------------------------------------------------------------------*/
    void searchEvent(final String searchTerm) {

        final DatabaseTableSubEvent databaseTable = new DatabaseTableSubEvent(getApplicationContext(),testEventsList);

        // Checks if database table is done setting up the database table
        new Thread(new Runnable() {
            @Override
            public void run() {

                // While setting up the virtual table is not done, wait
                while(!DatabaseTableSubEvent.threadDone){
                    try{
                        Thread.sleep(750);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

                // Find an event that matches the search term
                Cursor cursor = databaseTable.getEventNameMatches(searchTerm, null);

                // If an event has been found that matches the search term
                if (cursor != null && cursor.moveToFirst() ){
                    String[] columnNames = cursor.getColumnNames();
                    do {

                        // Search through all the columns
                        for (String name: columnNames) {

                            // If the column is equal to the event id
                            if(name.equals("EVENT_ID")){

                                // Clear the previous searches, add the user id to the search result
                                searchResult.clear();
                                searchResult.add(cursor.getString(cursor.getColumnIndex(name)));

                                // Parse the string to get everything except the brackets
                                char[] eventIdCharArray = searchResult.toString().toCharArray();
                                String eventId = "";
                                for (int i = 1; i < eventIdCharArray.length - 1; i++) {
                                    eventId += eventIdCharArray[i];
                                }

                                // Create search events fragment
                                SearchEventsFragment searchEventsFragment = new SearchEventsFragment();

                                // Pass the user id to the search events fragment
                                Bundle bundle = new Bundle();
                                bundle.putString(getString(R.string.event_id), eventId);

                                searchEventsFragment.setArguments(bundle);

                                // Change the screen to search events fragment
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_frame_layout, searchEventsFragment)
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .addToBackStack("Search Event Fragment")
                                        .commit();
                            }

                        }
                    } while (cursor.moveToNext());
                }
                // If an event has not been found that matches the search term
                else{

                    SearchEventsFragment searchEventsFragment = new SearchEventsFragment();

                    // Change the screen to an empty search events fragment
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_frame_layout, searchEventsFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack("Search Event Fragment")
                            .commit();

                }
            }
        }).start();
    }


    /*---------------------------------------------------------------------------
    Function Name:                removeSearchToolbar()
    Description:                  Removes the search bar
    Input:                        final String searchTerm - term searched by the user
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public void removeSearchToolbar() {

        // If the search view exists remove the search bar
        if (findViewById(R.id.search) != null)
            ((ViewManager) search.getParent()).removeView(search);

    }

    /*---------------------------------------------------------------------------
    Function Name:                addSearchToolbar()
    Description:                  Adds the search bar
    Input:                        final String searchTerm - term searched by the user
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public void addSearchToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // If the search view exists add the search bar
        if (findViewById(R.id.search) == null)
            ((ViewManager) toolbar.getParent()).addView(search, params);

        // TODO: NEW RESET LOGO TEXT DEPENDING IF SEARCH EVENT OR USER
        // Set the text for the search bar
        search.setLogoText("Search for Events");

        // Set custom fonts for all the textviews
        overrideFonts(toolbar.getContext(),toolbar);

    }

    /*---------------------------------------------------------------------------
    Function Name:                setupDrawer()
    Description:                  Sets up the drawer
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    void setupDrawer() {
        // Find the views required to set up the drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Init action bar toggle for nav. drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        // Connects the action bar toggle to the drawer
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Set custom fonts for all the textviews
        overrideFonts(navigationView.getContext(),navigationView);

        // Sets the listener for navigation drawer to this
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.home);

        // Set custom fonts for all the textviews
        overrideFonts(toolbar.getContext(),toolbar);
    }

    /*---------------------------------------------------------------------------
    Function Name:                setupFragments()
    Description:                  Sets up the fragments for the drawer and home
                                  fragment to show up
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    void setupFragments() {

        // Have references to the fragment manager and transactions
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Checks if home fragment has been committed already
        currentFragment = fragmentManager.findFragmentByTag(getString(R.string.home));

        // If fragments have not been created yet
        if (currentFragment == null) {

            // Initialize all fragments having to do with the navigation bar
            homeFeedTabsFragment = new HomeFeedTabsFragment();
            notificationsFragment = new NotificationsFragment();
            categoriesListFragment = new CategoriesListFragment();
            myEventsTabsFragment = new MyEventsTabsFragment();
            subscriptionsListFragment = new SubscriptionsListFragment();
            findPeopleFragment = new FindPeopleFragment();
            subscriptionFeedFragment = new SubscriptionFeedFragment();
            aboutFragment = new AboutFragment();
            settingsFragment = new SettingsFragment();

            // Add all the fragments to the fragment transaction to be added, hidden,
            // except the home feed
            fragmentTransaction
                    .add(R.id.fragment_frame_layout, settingsFragment, getString(R.string.settings))
                    .hide(settingsFragment)
                    .add(R.id.fragment_frame_layout, aboutFragment, getString(R.string.about))
                    .hide(aboutFragment)
                    .add(R.id.fragment_frame_layout, subscriptionFeedFragment, getString(R.string.subscriptionFeed))
                    .hide(subscriptionFeedFragment)
                    .add(R.id.fragment_frame_layout, findPeopleFragment, getString(R.string.findPeople))
                    .hide(findPeopleFragment)
                    .add(R.id.fragment_frame_layout, subscriptionsListFragment, getString(R.string.subscriptions))
                    .hide(subscriptionsListFragment)
                    .add(R.id.fragment_frame_layout, myEventsTabsFragment, getString(R.string.my_events))
                    .hide(myEventsTabsFragment)
                    .add(R.id.fragment_frame_layout, categoriesListFragment, getString(R.string.category))
                    .hide(categoriesListFragment)
                    .add(R.id.fragment_frame_layout, notificationsFragment, getString(R.string.notifications))
                    .hide(notificationsFragment)
                    .commit();

            // Add the home feed and commit to make the home feed show up to the user
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_frame_layout, homeFeedTabsFragment, getString(R.string.home))
                    .addToBackStack(getString(R.string.fragment_profile))
                    .commit();

            // Reset current fragment
            currentFragment = homeFeedTabsFragment;

        // If fragments have not been created already
        } else {
            homeFeedTabsFragment = (HomeFeedTabsFragment) currentFragment;
            notificationsFragment = (NotificationsFragment) getSupportFragmentManager().
                    findFragmentByTag(getString(R.string.notifications));
            categoriesListFragment = (CategoriesListFragment) getSupportFragmentManager().
                    findFragmentByTag(getString(R.string.category));
            myEventsTabsFragment = (MyEventsTabsFragment) getSupportFragmentManager().
                    findFragmentByTag(getString(R.string.my_events));
            subscriptionsListFragment = (SubscriptionsListFragment) getSupportFragmentManager().
                    findFragmentByTag(getString(R.string.subscriptions));
            findPeopleFragment = (FindPeopleFragment) getSupportFragmentManager().
                    findFragmentByTag(getString(R.string.findPeople));
            subscriptionFeedFragment = (SubscriptionFeedFragment) getSupportFragmentManager().
                    findFragmentByTag(getString(R.string.subscriptionFeed));
            aboutFragment = (AboutFragment) getSupportFragmentManager().
                    findFragmentByTag(getString(R.string.about));
            settingsFragment = (SettingsFragment) getSupportFragmentManager().
                    findFragmentByTag(getString(R.string.settings));

            // Hide all the fragments from view of the user
            fragmentTransaction
                    .hide(notificationsFragment)
                    .hide(categoriesListFragment)
                    .hide(myEventsTabsFragment)
                    .hide(subscriptionsListFragment)
                    .hide(findPeopleFragment)
                    .hide(subscriptionFeedFragment)
                    .hide(aboutFragment)
                    .hide(settingsFragment)
                    .commit();
        }
    }

    /*---------------------------------------------------------------------------
    Function Name:                onNavigationItemSelected()
    Description:                  Sets up the fragments for the drawer and home
                                  fragment to show up
    Input:                        MenuItem item
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String title = item.getTitle().toString();

        //Reference to new fragment to be shown to the user
        Fragment newFragment = null;

        // Depending on the id, add or remove the search toolbar,
        // update the search type, and update the newFragment variable
        if (id == R.id.home) {

            addSearchToolbar();
            searchType = getString(R.string.fragment_home_feed);
            newFragment = homeFeedTabsFragment;

        } else if (id == R.id.notifications) {

            removeSearchToolbar();
            newFragment = notificationsFragment;

        } else if (id == R.id.category) {

            removeSearchToolbar();
            newFragment = categoriesListFragment;

        } else if (id == R.id.myEvents) {

            addSearchToolbar();
            searchType = getString(R.string.fragment_my_events);
            newFragment = myEventsTabsFragment;

        } else if (id == R.id.subscriptions) {

            addSearchToolbar();
            searchType = getString(R.string.fragment_my_sub);
            search.setLogoText("Search for Subscriptions");
            newFragment = subscriptionsListFragment;

        } else if (id == R.id.find_people) {

            addSearchToolbar();
            searchType = getString(R.string.fragment_find_people);
            search.setLogoText("Search for People");
            newFragment = findPeopleFragment;

        } else if (id == R.id.subscriptionFeed) {

            addSearchToolbar();
            searchType = getString(R.string.fragment_my_sub_feed);
            newFragment = subscriptionFeedFragment;

        } else if (id == R.id.about) {

            removeSearchToolbar();
            newFragment = aboutFragment;

        } else if (id == R.id.accMang) {

            removeSearchToolbar();
            newFragment = settingsFragment;

        // If the user chooses logout
        } else if (id == R.id.logOut) {

            // Create a new alert dialog for logout
            new AlertDialog.Builder(this)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            // Call logout call to account ifrebase
                            af.logout();

                            // Start the intent to go to the main activity
                            Intent i = new Intent(MainActivity.this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }

        // If a person has selected to go to a new fragment
        if (newFragment != null) {

            // Switch to the new fragment/show it to user
            getSupportFragmentManager().beginTransaction()
                    .show(newFragment)
                    .replace(R.id.fragment_frame_layout, newFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(title)
                    .commit();
        }

        // Close drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        // Change all the textviews to the custom font
        overrideFonts(drawer.getContext(),drawer);

        return true;
    }

    /*---------------------------------------------------------------------------
    Function Name:                goToProfile()
    Description:                  Allows the user to go to the profile
    Input:                        View view
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public void goToProfile(View view) {

        // Create the profile fragment
        Fragment profileFragment = new ProfileFragment();

        // Create a new bundle to send profile type to the profile fragment
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.profile_type), getString(R.string.profile_type_owner));
        profileFragment.setArguments(bundle);

        // Remove the search toolbar for the next fragmnet
        removeSearchToolbar();

        // Switch to the profile fragment
        getSupportFragmentManager().beginTransaction()
                .show(profileFragment)
                .replace(R.id.fragment_frame_layout, profileFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(getString(R.string.fragment_profile))
                .commit();

        // Close drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        // Change all the textviews to the custom font
        overrideFonts(drawer.getContext(),drawer);


    }

    /*---------------------------------------------------------------------------
    Function Name:                onBackPressed()
    Description:                  When the back button is pressed, make sure
                                  the navigation drawer is closed
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // If the drawer is open, close it
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreateOptionsMenu()
    Description:                  Inflate the menu with the create event plus button
    Input:                        Menu menu
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu that has the create event plus button
        getMenuInflater().inflate(R.menu.action_bar_main, menu);
        return true;
    }

    /*---------------------------------------------------------------------------
    Function Name:                onOptionsItemSelected()
    Description:                  Inflate the menu with the create event plus button
    Input:                        MenuItem item
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_create_event) {
            // Create new create event fragment
            CreateEventFragment createEventFragment = new CreateEventFragment();

            removeSearchToolbar();

            // Add create event fragment to fragment manager
            this.getSupportFragmentManager().beginTransaction()
                    .show(createEventFragment)
                    .replace(R.id.fragment_frame_layout, createEventFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(getString(R.string.fragment_create_event))
                    .commit();
        }

        // Must be left empty so onOptionsItemSelected will be called in subsequent fragments
        return super.onOptionsItemSelected(item);

    }

    // For pictures
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int REQUEST_CODE = 1;
    private boolean square; // if the picture should be square

    /*---------------------------------------------------------------------------
    Function Name:                pickImage()
    Description:                  Inflate the menu with the create event plus button
    Input:                        boolean square
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public boolean pickImage(boolean square) {

        // Update square boolean
        this.square = square;

        // Start picking an image
        Crop.pickImage(this);

        return true;
    }

    /*---------------------------------------------------------------------------
    Function Name:                onActivityResult()
    Description:                  After another activity has started and has finished
    Input:                        int requestCode
                                  int resultCode
                                  Intent result
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {

        // If the activity resulted is picked and was successful
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {

            beginCrop(result.getData());

        } else if (requestCode == Crop.REQUEST_CROP) {

            handleCrop(resultCode, result);

        }

        // If the activity that just ended captured an image
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            // If the user picked an image successfully
            if (resultCode == RESULT_OK) {

                Uri uri = result.getData();

                // Crop the picture as a square for the circle, otherwise crop a rectangle
                if (square)
                    Crop.of(uri, picture).asSquare().start(this);
                else
                    Crop.of(uri, picture).withAspect(2, 1).start(this);

            // If the user has cancelled
            } else if (resultCode == RESULT_CANCELED) {
                View view = findViewById(android.R.id.content);
                Snackbar.make(view, "Image capture failed", Snackbar.LENGTH_LONG).show();
            } else {
                View view = findViewById(android.R.id.content);
                Snackbar.make(view, "Image capture failed", Snackbar.LENGTH_LONG).show();
            }


        }

        // Get a picture without crop and was succesfsul update picture varaible
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            picture = result.getData();
        }

    }

    /*---------------------------------------------------------------------------
    Function Name:                beginCrop()
    Description:                  Start cropping
    Input:                        Uri source
    Output:                       None.
    ---------------------------------------------------------------------------*/
    private void beginCrop(Uri source) {

        // Get the destination uri
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));

        // Crop the picture as a square for the circle, otherwise crop a rectangle
        if (square)
            Crop.of(source, destination).asSquare().start(this);
        else
            Crop.of(source, destination).withAspect(2, 1).start(this);
    }

    /*---------------------------------------------------------------------------
    Function Name:                handleCrop()
    Description:                  Get the output from cropping
    Input:                        int resultCode
                                  Intent result
    Output:                       None.
    ---------------------------------------------------------------------------*/
    private void handleCrop(int resultCode, Intent result) {

        // If the result is ok, update picture; otherwise make a toast there was an error
        if (resultCode == RESULT_OK) {
            picture = Crop.getOutput(result);

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
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

}
