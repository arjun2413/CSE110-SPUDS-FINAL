package com.spuds.eventapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.spuds.eventapp.EventDetails.CategoriesListFragment;
import com.spuds.eventapp.Login.LoginActivity;
import com.spuds.eventapp.NewsFeeds.MyEventsFragment;
import com.spuds.eventapp.NewsFeeds.MySubscriptionFeedFragment;
import com.spuds.eventapp.NewsFeeds.NotificationsFragment;
import com.spuds.eventapp.NewsFeeds.PublicFeedFragment;
import com.spuds.eventapp.People.FindPeopleFragment;
import com.spuds.eventapp.People.MySubscriptionsFragment;
import com.spuds.eventapp.People.ProfileFragment;
import com.spuds.eventapp.Settings.AboutFragment;
import com.spuds.eventapp.Settings.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment currentFragment;
    PublicFeedFragment publicFeedFragment;
    NotificationsFragment notificationsFragment;
    CategoriesListFragment categoriesListFragment;
    MyEventsFragment myEventsFragment;
    MySubscriptionsFragment mySubscriptionsFragment;
    FindPeopleFragment findPeopleFragment;
    MySubscriptionFeedFragment mySubscriptionFeedFragment;
    AboutFragment aboutFragment;
    SettingsFragment settingsFragment;

    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupFragments();
        setupToolbar();
        setupDrawer();

        //Firebase.setAndroidContext(this);
        // other setup code


    }

    void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    void setupDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.home);

        // Doesn't work for some reason
        /*ImageView header = (ImageView) findViewById(R.id.profilePic);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchTo(profileFragment, getString(R.string.profile));
                //TODO: uncheck fragment highlighted in  hamburger menu
            }
        });*/
    }

    void setupFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Checks if home fragment has been committed already
        currentFragment = fragmentManager.findFragmentByTag(getString(R.string.home));

        if (currentFragment == null) {
            publicFeedFragment = new PublicFeedFragment();
            notificationsFragment = new NotificationsFragment();
            categoriesListFragment = new CategoriesListFragment();
            myEventsFragment = new MyEventsFragment();
            mySubscriptionsFragment = new MySubscriptionsFragment();
            findPeopleFragment = new FindPeopleFragment();
            mySubscriptionFeedFragment = new MySubscriptionFeedFragment();
            aboutFragment = new AboutFragment();
            settingsFragment = new SettingsFragment();

            profileFragment = new ProfileFragment();

            fragmentTransaction
                    .add(R.id.fragment_frame_layout, settingsFragment, getString(R.string.settings))
                    .hide(settingsFragment)
                    .add(R.id.fragment_frame_layout, aboutFragment, getString(R.string.about))
                    .hide(aboutFragment)
                    .add(R.id.fragment_frame_layout, mySubscriptionFeedFragment, getString(R.string.subscriptionFeed))
                    .hide(mySubscriptionFeedFragment)
                    .add(R.id.fragment_frame_layout, findPeopleFragment, getString(R.string.findPeople))
                    .hide(findPeopleFragment)
                    .add(R.id.fragment_frame_layout, mySubscriptionsFragment, getString(R.string.subscriptionFeed))
                    .hide(mySubscriptionsFragment)
                    .add(R.id.fragment_frame_layout, myEventsFragment, getString(R.string.myEvents))
                    .hide(myEventsFragment)
                    .add(R.id.fragment_frame_layout, categoriesListFragment, getString(R.string.category))
                    .hide(categoriesListFragment)
                    .add(R.id.fragment_frame_layout, notificationsFragment, getString(R.string.notifications))
                    .hide(notificationsFragment)
                    .add(R.id.fragment_frame_layout, profileFragment, getString(R.string.profile))
                    .hide(profileFragment)
                    .add(R.id.fragment_frame_layout, publicFeedFragment, getString(R.string.home))
                    .commit();

            currentFragment = publicFeedFragment;
        } else {
            publicFeedFragment = (PublicFeedFragment) currentFragment;
            notificationsFragment = (NotificationsFragment) getSupportFragmentManager().
                    findFragmentByTag(getString(R.string.notifications));
            categoriesListFragment = (CategoriesListFragment) getSupportFragmentManager().
                    findFragmentByTag(getString(R.string.category));
            myEventsFragment = (MyEventsFragment) getSupportFragmentManager().
                    findFragmentByTag(getString(R.string.myEvents));
            mySubscriptionsFragment = (MySubscriptionsFragment) getSupportFragmentManager().
                    findFragmentByTag(getString(R.string.subscriptions));
            findPeopleFragment = (FindPeopleFragment) getSupportFragmentManager().
                    findFragmentByTag(getString(R.string.findPeople));
            mySubscriptionFeedFragment = (MySubscriptionFeedFragment) getSupportFragmentManager().
                    findFragmentByTag(getString(R.string.subscriptionFeed));
            aboutFragment = (AboutFragment) getSupportFragmentManager().
                    findFragmentByTag(getString(R.string.about));
            settingsFragment = (SettingsFragment) getSupportFragmentManager().
                    findFragmentByTag(getString(R.string.settings));

            profileFragment = (ProfileFragment) getSupportFragmentManager().
                    findFragmentByTag(getString(R.string.profile));
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String title = item.getTitle().toString();

        if (id == R.id.home) {

            switchTo(publicFeedFragment, title);

        } else if (id == R.id.notifications) {

            switchTo(notificationsFragment, title);

        } else if (id == R.id.category) {

            switchTo(categoriesListFragment, title);

        } else if (id == R.id.myEvents) {

            switchTo(myEventsFragment, title);

        } else if (id == R.id.subscriptions) {

            switchTo(mySubscriptionsFragment, title);

        } else if (id == R.id.find_people) {

            switchTo(findPeopleFragment, title);

        } else if (id == R.id.subscriptionFeed) {

            switchTo(mySubscriptionFeedFragment, title);

        } else if (id == R.id.about) {

            switchTo(aboutFragment, title);

        } else if (id == R.id.accMang) {

            switchTo(settingsFragment, title);

        } else if (id == R.id.logOut) {
            new AlertDialog.Builder(this)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    //.setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void switchTo (Fragment fragment, String name) {

        if (fragment.isVisible())
            return;

        if (fragment == null)
            return;

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);

        // Make sure the next view is below the current one
        fragment.getView().bringToFront ();
        // And bring the current one to the very top
        currentFragment.getView().bringToFront ();

        // Hide the current fragment
        t.hide (currentFragment);
        t.show(fragment);
        currentFragment = fragment;

        t.addToBackStack(null);
        t.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
