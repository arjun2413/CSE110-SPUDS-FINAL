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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
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
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
    ImageView profilePic;

    SearchBox search;
    boolean first = true;

    // notification stuff
    public String token;
    public GoogleCloudMessaging gcm;

    public String searchType;
    public Uri picture;
    ArrayList<SubEvent> testEventsList;
    ArrayList<SubUser> testUsersList;
    ArrayList <String> searchResult;
    NavigationView navigationView;
    View headerView;
    TextView name;
    AccountFirebase af;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        af = new AccountFirebase();
        //af.authCheck();

        //getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);

        /*  TextView title = (TextView) findViewById(R.id.tv_toolbar);
        title.setText("EVENTORY");*/

        setupNotifications(); // set up GCM values

        setupFragments();
        setupMainToolbar();
        setupSearchToolbar();
        setupDrawer();
        setupProfileDrawer();

        searchResult = new ArrayList<String>();
        testUsersList = UserFirebase.subUsers;
        testEventsList = EventsFirebase.subEvents;

    }

    // sets up GCM for the phone to use
    void setupNotifications() {
        if (gcm == null) {
            gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
            //("GCM", gcm.toString());
        }

        Intent i = new Intent(this, RegistrationService.class);
        startService(i);
        //("intent", i.toString());

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (RegistrationService.token == null) {
                    try {
                        Thread.sleep(75);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                token = RegistrationService.token;
                //("Token = ", token);


                af.pushRegistrationId(token);

            }
        }).start();
    }

    public void setupProfileDrawer() {

        if (first) {
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            overrideFonts(navigationView.getContext(), navigationView);
            headerView = navigationView.inflateHeaderView(R.layout.nav_header_profile);
            overrideFonts(headerView.getContext(), headerView);
            name = (TextView) headerView.findViewById(R.id.user_name);
            profilePic = (ImageView) headerView.findViewById(R.id.profile_pic);
            first = false;
        }

        if (name != null) {
            name.setText(UserFirebase.thisUser.getName());
        }

        //rounded photo, crashes when you re-run for some reason

        String imageFile = UserFirebase.thisUser.getPicture();

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
                profilePic.setImageDrawable(circularBitmapDrawable);
            } else {
                try {
                    src = BitmapFactory.decodeResource(getResources(), R.drawable.profile_pic_icon);

                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), src);
                    circularBitmapDrawable.setCircular(true);
                    circularBitmapDrawable.setAntiAlias(true);
                    profilePic.setImageDrawable(circularBitmapDrawable);
                } catch(OutOfMemoryError e) {
                    System.err.println(e.toString());
                }
            }
        } else {
            Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.profile_pic_icon);

            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(getResources(), src);
            circularBitmapDrawable.setCircular(true);
            circularBitmapDrawable.setAntiAlias(true);
            profilePic.setImageDrawable(circularBitmapDrawable);
        }
    }

    void setupMainToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       /* TextView title = (TextView) toolbar.findViewById(R.id.tv_toolbar);
        title.setText("EVENTORY");*/
        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayShowTitleEnabled(false);
        overrideFonts(toolbar.getContext(),toolbar);

    }

    void setupSearchToolbar() {
        search = (SearchBox) findViewById(R.id.searchbox);
        overrideFonts(search.getContext(),search);

        search.setLogoText("Search for Events");
        search.setLogoTextColor(Color.parseColor("#bfbfbf"));
        search.setDrawerLogo(R.drawable.search);

        final ArrayList<SearchResult> list = new ArrayList<SearchResult>();

        search.setSearchables(list);

        search.setSearchListener(new SearchBox.SearchListener() {



            @Override
            public void onSearch(final String searchTerm) {
                //Toast.makeText(MainActivity.this, searchTerm +" Searched", Toast.LENGTH_LONG).show();


                // TODO (C): Finish
                Log.v("searchtype", searchType);
                if (searchType.equals(getString(R.string.fragment_home_feed))) {
                    Log.v("wtrwtwa", searchType);

                    findAnyEvent(searchTerm);

                } else if (searchType.equals(getString(R.string.fragment_find_people)) || searchType.equals(getString(R.string.fragment_invite_people))) {
                    Log.v("wtrwtwa", searchType);
                    findAnyUser(searchTerm);



                } else if (searchType.equals(getString(R.string.fragment_my_events))) {

                } else if (searchType.equals(getString(R.string.fragment_my_sub_feed))) {

                } else if (searchType.equals(getString(R.string.fragment_my_sub))) {

                } else {
                    if (searchType.equals(getString(R.string.cat_academic))) {

                    } else if (searchType.equals(getString(R.string.cat_student_orgs))) {

                    } else if (searchType.equals(getString(R.string.cat_concerts))) {

                    } else if (searchType.equals(getString(R.string.cat_food))) {

                    } else if (searchType.equals(getString(R.string.cat_free))) {

                    } else if (searchType.equals(getString(R.string.cat_social))) {

                    } else if (searchType.equals(getString(R.string.cat_sports))) {

                    }
                }
            }

            @Override
            public void onResultClick(SearchResult result) {
                //React to a result being clicked
            }


            @Override
            public void onSearchOpened() {
                Log.v("onsearchopen", "here");
                UserFirebase userFirebase = new UserFirebase();
                userFirebase.getSubUserList();

                EventsFirebase eventsFirebase = new EventsFirebase();
                eventsFirebase.getSubEventList();
            }

            @Override
            public void onSearchCleared() {

            }

            @Override
            public void onSearchClosed() {

            }

            @Override
            public void onSearchTermChanged(String s) {

            }

        });
        params = (RelativeLayout.LayoutParams) search.getLayoutParams();

    }

    void findAnyUser(final String searchTerm) {
        Log.v("searchtype", "here");
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!UserFirebase.threadCheckSubUser) {
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                testUsersList = UserFirebase.subUsers;
                //check what users are there
                for(SubUser s : testUsersList){
                    Log.d("CreateTable",s.getUserId());
                    Log.d("CreateTable",s.getName());
                }


                final DatabaseTableSubUser databaseTable = new DatabaseTableSubUser(getApplicationContext(),testUsersList);
                Log.d("CreateTable","AFTER DB called");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(!DatabaseTableSubUser.threadDone){

                            try{
                                Log.d("ThreadDebug","try block");
                                Thread.sleep(750);
                            } catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }

                        Log.d("Search","Starting Search");
                        Cursor cursor = databaseTable.getUserNameMatches(searchTerm, null);
                        String retVal = "";
                        if (cursor != null && cursor.moveToFirst() ){
                            String[] columnNames = cursor.getColumnNames();
                            do {
                                //Searched results have been found
                                for (String name: columnNames) {
                                    //retVal += String.format("%s: %s\n", name, cursor.getString(cursor.getColumnIndex(name)));
                                    if(name.equals("USER_ID")){
                                        //Return to outside world
                                        if(cursor == null){
                                            Log.d("Search","Cursor is null");
                                        }
                                        Log.d("Search","Int is: "+cursor.getColumnIndex(name));
                                        searchResult.clear();
                                        searchResult.add(cursor.getString(cursor.getColumnIndex(name)));
                                        Log.v("searcherseultevent", "" + searchResult);

                                        char[] userIdCharArray = searchResult.toString().toCharArray();

                                        String userId = "";
                                        for (int i = 1; i < userIdCharArray.length - 1; i++) {
                                            userId += userIdCharArray[i];
                                        }
                                        Log.v("userId=", userId);
                                        SearchUsersFragment searchUsersFragment = new SearchUsersFragment();

                                        Bundle bundle = new Bundle();
                                        bundle.putString(getString(R.string.user_id), userId);

                                        searchUsersFragment.setArguments(bundle);

                                        // Add Event Details Fragment to fragment manager
                                        getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.fragment_frame_layout, searchUsersFragment)
                                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                                .addToBackStack("Search People Fragment")
                                                .commit();
                                    }

                                }
                                retVal += "\n";
                                Log.d("Search","Result found in Loop");
                            } while (cursor.moveToNext());
                        }
                        else{
                            Log.d("Search","Nothing found.");
                        }
                        Log.d("Search","RESULTS: "+retVal);
                        //System.err.println(retVal);
                        //Log.d("Search","RESULT: "+cursor.getString(cursor.getColumnIndex(cursor.getColumnNames()[1])));
                    }
                }).start();



            }
        }).start();
    }

    void findAnyEvent(final String searchTerm) {
        Log.v("inwhere", "infindanyevent");
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!EventsFirebase.threadCheckSubEvent) {
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                testEventsList = EventsFirebase.subEvents;


                //check what events are there
                for(SubEvent s : testEventsList){
                    Log.d("CreateTable",s.getEventId());
                    Log.d("CreateTable",s.getEventName());
                }


                final DatabaseTableSubEvent databaseTable = new DatabaseTableSubEvent(getApplicationContext(),testEventsList);
                Log.d("CreateTable","AFTER DB called");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(!DatabaseTableSubEvent.threadDone){

                            try{
                                Log.d("ThreadDebug","try block");
                                Thread.sleep(750);
                            } catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }

                        Log.d("Search","Starting Search");
                        Cursor cursor = databaseTable.getEventNameMatches(searchTerm, null);
                        String retVal = "";
                        if (cursor != null && cursor.moveToFirst() ){
                            String[] columnNames = cursor.getColumnNames();
                            do {
                                //Searched results have been found
                                for (String name: columnNames) {
                                    //retVal += String.format("%s: %s\n", name, cursor.getString(cursor.getColumnIndex(name)));
                                    if(name.equals("EVENT_ID")){
                                        //Return to outside world
                                        if(cursor == null){
                                            Log.d("Search","Cursor is null");
                                        }
                                        Log.d("Search","Int is: "+cursor.getColumnIndex(name));
                                        searchResult.add(cursor.getString(cursor.getColumnIndex(name)));

                                        Log.v("searcherseultevent", "" + searchResult);

                                        char[] eventIdCharArray = searchResult.toString().toCharArray();

                                        String eventId = "";
                                        for (int i = 1; i < eventIdCharArray.length - 1; i++) {
                                            eventId += eventIdCharArray[i];
                                        }

                                        SearchEventsFragment searchEventsFragment = new SearchEventsFragment();

                                        Bundle bundle = new Bundle();
                                        bundle.putString(getString(R.string.event_id), eventId);

                                        searchEventsFragment.setArguments(bundle);

                                        // Add Event Details Fragment to fragment manager
                                        getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.fragment_frame_layout, searchEventsFragment)
                                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                                .addToBackStack("Search Event Fragment")
                                                .commit();

                                    }

                                }
                                retVal += "\n";
                                Log.d("Search","Result found in Loop");
                            } while (cursor.moveToNext());
                        }
                        else{
                            Log.d("Search","Nothing found.");
                        }
                        Log.d("Search","RESULTS: "+retVal);
                        //System.err.println(retVal);
                        //Log.d("Search","RESULT: "+cursor.getString(cursor.getColumnIndex(cursor.getColumnNames()[1])));
                    }
                }).start();



            }
        }).start();
    }

    RelativeLayout.LayoutParams params;

    public void removeSearchToolbar() {
        Log.v("test", "heretest" + "removesearchtoolbar");

        if (findViewById(R.id.search) != null)
            ((ViewManager) search.getParent()).removeView(search);

    }

    public void addSearchToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (findViewById(R.id.search) == null)
            ((ViewManager) toolbar.getParent()).addView(search, params);

        search.setLogoText("Search for Events");
        overrideFonts(toolbar.getContext(),toolbar);

    }

    void setupDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        overrideFonts(toolbar.getContext(),toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        overrideFonts(navigationView.getContext(),navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.home);

    }

    void setupFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Checks if home fragment has been committed already
        currentFragment = fragmentManager.findFragmentByTag(getString(R.string.home));

        if (currentFragment == null) {

            // TODO (M): POST request to get account information
            // ex: who subscribing to, first 10 for each feed, profile information
            // sending information to respective fragments
            // talk about what to load first, what to load later

            homeFeedTabsFragment = new HomeFeedTabsFragment();
            notificationsFragment = new NotificationsFragment();
            categoriesListFragment = new CategoriesListFragment();
            myEventsTabsFragment = new MyEventsTabsFragment();
            subscriptionsListFragment = new SubscriptionsListFragment();
            findPeopleFragment = new FindPeopleFragment();
            subscriptionFeedFragment = new SubscriptionFeedFragment();
            aboutFragment = new AboutFragment();
            settingsFragment = new SettingsFragment();

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

            fragmentManager.beginTransaction()
                    .add(R.id.fragment_frame_layout, homeFeedTabsFragment, getString(R.string.home))
                    .addToBackStack(getString(R.string.fragment_profile))
                    .commit();

            currentFragment = homeFeedTabsFragment;
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        //overrideFonts(coordinatorLayout.getContext(),coordinatorLayout);

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String title = item.getTitle().toString();

        Fragment newFragment = null;
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

        } else if (id == R.id.logOut) {
            new AlertDialog.Builder(this)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            af.logout();
                            Intent i = new Intent(MainActivity.this, LoginActivity.class);
                            // set the new task and clear flags
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            //startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            //getFragmentManager().popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    //.setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        if (newFragment != null) {
            //("MainActivity", "newFragment != null switching to " + title);
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

        overrideFonts(drawer.getContext(),drawer);

        return true;
    }

    public void goToProfile(View view) {
        Fragment profileFragment = new ProfileFragment();

        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.profile_type), getString(R.string.profile_type_owner));

        profileFragment.setArguments(bundle);

        removeSearchToolbar();
        // Add Event Details Fragment to fragment manager
        getSupportFragmentManager().beginTransaction()
                .show(profileFragment)
                .replace(R.id.fragment_frame_layout, profileFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(getString(R.string.fragment_profile))
                .commit();

        // Close drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        overrideFonts(drawer.getContext(),drawer);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

                super.onBackPressed();

        }
        //removeSearchToolbar();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_create_event) {
            CreateEventFragment createEventFragment = new CreateEventFragment();

            removeSearchToolbar();
            // Add Event Details Fragment to fragment manager
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

    // for pictures
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    Uri fileUri;

    public boolean useCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Eventory");


        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        fileUri = Uri.fromFile(mediaFile);


        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        return true;
    }

    private static final int REQUEST_CODE = 1;

    private boolean square;
    public boolean pickImage(boolean square) {

        this.square = square;
        Crop.pickImage(this);


        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }


        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                ////(TAG, "Image saved to:\n" + fileUri);

                Uri uri = result.getData();

                //("square", String.valueOf(square));
                if (square)
                    Crop.of(uri, picture).asSquare().start(this);
                else
                    Crop.of(uri, picture).withAspect(2, 1).start(this);


            } else if (resultCode == RESULT_CANCELED) {
                View view = findViewById(android.R.id.content);
                Snackbar.make(view, "Image capture failed", Snackbar.LENGTH_LONG).show();
            } else {
                View view = findViewById(android.R.id.content);
                Snackbar.make(view, "Image capture failed", Snackbar.LENGTH_LONG).show();
            }


        }

        // w/o crop
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Uri uri = result.getData();

            picture = uri;
        }

    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));

        if (square)
            Crop.of(source, destination).asSquare().start(this);
        else
            Crop.of(source, destination).withAspect(2, 1).start(this);
    }

    private void handleCrop(int resultCode, Intent result) {

        if (resultCode == RESULT_OK) {
            picture = Crop.getOutput(result);

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void pickImageWithoutCrop() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Medium.ttf"));
            }
        }
        catch (Exception e) {
        }
    }


  /*  public void setActionBarTitle(String title){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       /* setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        toolbar.setTitle(title);
        //getActionBar().setTitle(title);
    }*/

}
