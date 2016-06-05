package com.spuds.eventapp.EventDetails;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.EditEvent.EditEventFragment;
import com.spuds.eventapp.Firebase.EventsFirebase;
import com.spuds.eventapp.Firebase.UserFirebase;
import com.spuds.eventapp.InvitePeople.InvitePeopleFragment;
import com.spuds.eventapp.Profile.ProfileFragment;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.Event;
import com.spuds.eventapp.Shared.MainActivity;

/*---------------------------------------------------------------------------
Class Name:                EventDetailsFragment
Description:               Sets up screen for the user to see event details
---------------------------------------------------------------------------*/
public class EventDetailsFragment extends Fragment {

    // Holds event details
    Event event;
    String eventId;
    boolean ownEvent;
    boolean going;

    // Views for event details
    ImageView eventPic;
    TextView eventName;
    TextView eventLocation;
    TextView eventDate;
    TextView eventAttendees;
    TextView eventCategories;
    TextView eventHost;
    TextView eventDescription;
    Button addComment;
    Button invitePeople;
    Button buttonGoingOrEdit;
    TextView eventTime;
    ImageButton buttonEditEvent;
    SwipeRefreshLayout mySwipeRefreshLayout;
    SwipeRefreshLayout.OnRefreshListener refreshListener;

    // Reference to itself
    Fragment eventDetailsFragment;


    // Checks if first time creating event details fragment
    boolean first = true;

    // Reference to backend
    EventsFirebase eventsFirebase;

    // Prevent user from spamming going utton
    boolean canClickGoing = true;
    boolean allowRefresh = false;

    /*---------------------------------------------------------------------------
    Function Name:                EventDetailsFragment
    Description:                  Required default no-argument constructor
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public EventDetailsFragment() {}

    /*---------------------------------------------------------------------------
    Function Name:                onCreate()
    Description:                  Called each time fragment is created
    Input:                        Bundle savedInstanceState
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Attempt to get events from previous fragment
        Bundle extras = getArguments();
        event = (Event) extras.getSerializable(getString(R.string.event_details));

        // If unsuccessful getting event details
        if (event == null) {

            // Get the event id from the previous fragment
            eventId = extras.getString(getString(R.string.event_id));

            //Fetch event using eventId
            eventsFirebase = new EventsFirebase();
            event = eventsFirebase.getEventDetails(eventId);
        // If successfull set event id
        } else
            eventId = event.getEventId();

        // Start call to see if the user is going to this event
        eventsFirebase = new EventsFirebase();
        eventsFirebase.isGoing(eventId);

        // Set instance variables
        if (event.getHostId().equals(UserFirebase.uId))
            ownEvent = true;
        eventDetailsFragment = this;

    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreateView()
    Description:                  Inflates View layout and sets fonts programmatically
                                  Sets up click listeners for possible user input
    Input:                        LayoutInflater inflater - inflates layout
                                  ViewGroup container - parent view group
                                  Bundle savedInstanceState
    Output:                       View to be inflated
    ---------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflates layout into view for the user
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        // Set all fonts for all textviews
        overrideFonts(view.getContext(),view);

        // Typeface/font for the textviews
        Typeface raleway_medium = Typeface.createFromAsset(getActivity().getAssets(),  "Raleway-Medium.ttf");

        // Sets the following text views to the specified font in the layout
        TextView name = (TextView) view.findViewById(R.id.event_name);
        name.setTypeface(raleway_medium);

        TextView location = (TextView) view.findViewById(R.id.label_location);
        location.setTypeface(raleway_medium);

        TextView description = (TextView) view.findViewById(R.id.text_description);
        description.setTypeface(raleway_medium);

        TextView categories = (TextView) view.findViewById(R.id.text_categories);
        categories.setTypeface(raleway_medium);

        TextView comments = (TextView) view.findViewById(R.id.comments_text);
        comments.setTypeface(raleway_medium);

        Button going = (Button) view.findViewById(R.id.button_going);
        going.setTypeface(raleway_medium);

        Button invite = (Button) view.findViewById(R.id.button_invite_people);
        invite.setTypeface(raleway_medium);

        // Setup view objects on this layout to be manipulated
        eventPic = (ImageView) view.findViewById(R.id.event_pic);
        eventName = (TextView) view.findViewById(R.id.event_name);
        eventLocation = (TextView) view.findViewById(R.id.event_loc);
        eventDate = (TextView) view.findViewById(R.id.event_date);
        eventTime = (TextView) view.findViewById(R.id.event_time);
        eventAttendees = (TextView) view.findViewById(R.id.event_attendees);
        eventCategories = (TextView) view.findViewById(R.id.event_categories);
        eventHost = (TextView) view.findViewById(R.id.event_host);
        eventDescription = (TextView) view.findViewById(R.id.event_description);
        addComment = (Button) view.findViewById(R.id.button_add_comment);
        invitePeople = (Button) view.findViewById(R.id.button_invite_people);
        buttonGoingOrEdit = (Button) view.findViewById(R.id.button_going);
        buttonEditEvent = (ImageButton) view.findViewById(R.id.button_edit_event);

        // Sets the title bar to the event name
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(event.getEventName());

        setUpEventInformation();
        setupEditEvent();
        setupRefresh(view);

        return view;
    }

    /*---------------------------------------------------------------------------
    Function Name:                setupRefresh()
    Description:                  Sets up allowing the user to refresh
    Input:                        final View view
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public void setupRefresh(final View view) {

        // Refresh layout object initialized
        mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        // On refresh listener - if the user refreshes
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                // Get the event details for the event again from the database
                EventsFirebase ef = new EventsFirebase();
                EventsFirebase.detailsThreadCheck = false;
                ef.getEventDetails(eventId);

                // A thread to check if the data has been pulled to the database
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        // While method to get event details is being executed wait for it to finish
                        while (!EventsFirebase.detailsThreadCheck) {
                            try {
                                Thread.sleep(70);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        // Change the views on the ui thread
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                // Update the event object with new event details
                                event = EventsFirebase.eventDetailsEvent;

                                // Update/setup again the event details and edit event
                                setUpEventInformation();
                                setupEditEvent();

                                // Stop the refresh icon
                                mySwipeRefreshLayout.setRefreshing(false);
                            }
                        });

                    }
                }).start();
            }
        };


        // Set the refresh listener to the refresh layout
        mySwipeRefreshLayout.setOnRefreshListener(refreshListener);

    }

    /*---------------------------------------------------------------------------
    Function Name:                setupEditEvet()
    Description:                  Sets up allowing the user (owner) to edit the event
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    void setupEditEvent() {

        // Only if the event is the user's
        if (ownEvent) {

            // Make the edit event visible
            buttonEditEvent.setVisibility(View.VISIBLE);

            // If the user clicks on the edit event button, direct user to edit event fragment
            buttonEditEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditEventFragment editEventFragment = new EditEventFragment();

                    // Send the event details to the edit event fragment
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(getString(R.string.event_details), event);
                    editEventFragment.setArguments(bundle);

                    // Remove the search bar for edit event
                    ((MainActivity) getActivity()).removeSearchToolbar();

                    // Start the edit event fragment
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_frame_layout, editEventFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack("Edit Event Fragment")
                            .commit();
                            allowRefresh = true;
                }
            });
        }
    }

    /*---------------------------------------------------------------------------
    Function Name:                setUpEventInformation()
    Description:                  Sets up allowing the user (owner) to edit the event
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void setUpEventInformation() {

        // Set up event name and location for the event
        eventName.setText(event.getEventName());
        eventLocation.setText(event.getLocation());

        // When host of the event is clicked
        eventHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the user information from the database
                final UserFirebase userFirebase = new UserFirebase();
                userFirebase.getAnotherUser(event.getHostId());

                // A thread to check if the data has been pulled to the database
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // While method to get user details is being executed wait for it to finish
                        while (!userFirebase.threadCheckAnotherUser) {
                            try {
                                Thread.sleep(77);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }

                        // Switch to the profile fragment
                        startProfileFragment(userFirebase);

                    }
                }).start();
            }
        });

        // TODO COMMENT
        String originalString = event.getDate();
        char[] c = originalString.toCharArray();
        char temp = c[0];
        c[0] = c[6];
        c[6] = temp;
        char temp1 = c[1];
        c[1] = c[7];
        c[7] = temp1;
        char temp2 = c[0];
        c[0] = c[3];
        c[3] = temp2;
        char temp3 = c[1];
        c[1] = c[4];
        c[4] = temp3;
        String swappedString = new String(c);
        String tempString = swappedString.substring(11, swappedString.length());
        String sub = tempString.substring(0, tempString.indexOf(':'));
        String col = tempString.substring(tempString.indexOf(':'), tempString.length());
        int numb = Integer.parseInt(sub);
        //PM
        if(Integer.parseInt(sub) >= 12 && Integer.parseInt(sub) < 24) {
            if(numb != 12) {
                numb -= 12;
            }
            sub = numb + col + "PM";
        }
        //AM
        else{
            if(numb == 0) {
                numb += 12;
            }
            sub = numb + col + "AM";
        }
        swappedString = swappedString.substring(0, 11) + sub;
        System.out.println("FINAL STRING IS: " + swappedString);
        //format:
        //     09/09/16 | 3:00PM
        String tempMonth = "";
        //change swappedString to Month, Day Year format
        String tempTime = swappedString.substring(8);
        switch (swappedString.substring(0,2)) {
            case "01":
                tempMonth = "January ";
                break;
            case "02":
                tempMonth = "February ";
                break;
            case "03":
                tempMonth = "March ";
                break;
            case "04":
                tempMonth = "April ";
                break;
            case "05":
                tempMonth = "May ";
                break;
            case "06":
                tempMonth = "June ";
                break;
            case "07":
                tempMonth = "July ";
                break;
            case "08":
                tempMonth = "August ";
                break;
            case "09":
                tempMonth = "September ";
                break;
            case "10":
                tempMonth = "October ";
                break;
            case "11":
                tempMonth = "November ";
                break;
            case "12":
                tempMonth = "December ";
                break;
        }
        String tempDay = "";
        if (swappedString.substring(3,4).equals("0"))
            tempDay = swappedString.substring(4,5); //if day is like 09, we only want 9
        else tempDay = swappedString.substring(3,5); // if its like 12, we want both digits
        //comma!
        tempDay = tempDay + ", ";
        String tempYear = "20" + swappedString.substring(6,8);
        swappedString = tempMonth + tempDay + tempYear + tempTime;

        // Set text views for the following event information
        eventDate.setText(swappedString);
        eventAttendees.setText(String.valueOf(event.getAttendees()));
        eventHost.setText(event.getHostName());
        eventDescription.setText(event.getDescription());

        // TODO COMMETN Categories
        String categories = "";
        System.out.println("size" + event.getCategories().size());
        if(event.getCategories() != null && event.getCategories().size() != 0) {
            for (int i = 0; i < event.getCategories().size() - 1; ++i) {

                categories += event.getCategories().get(i) + ", ";
            }
            categories += event.getCategories().get(event.getCategories().size() - 1);
        }
        // TODO END OF COMMENT TODO

        // Set text views for the following event information: categories
        eventCategories.setText(categories);

        // On click listener for the invite people button
        invitePeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Add the search bar into view
                ((MainActivity) getActivity()).addSearchToolbar();

                // Initialize invite people fragment
                InvitePeopleFragment invitePeopleFragment = new InvitePeopleFragment();

                // Sending the event id to the invite people fragment
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.event_id), eventId);
                invitePeopleFragment.setArguments(bundle);

                // Switch to the invite people fragment into view for the user
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame_layout, invitePeopleFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack("fragment_invite_people")
                        .commit();
            }
        });

        // Check if the user is going to the event by making a GET request to the database
        eventsFirebase.isGoing(eventId);

        // A thread to check if the data has been pulled from the database
        new Thread(new Runnable() {
            @Override
            public void run() {

                // While the get request is getting if the user is going to this event
                while (eventsFirebase.idIsGoing == 0) {
                    try {
                        Thread.sleep(75);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Set the going variable according to the databse
                if (eventsFirebase.idIsGoing == 1) {
                    going = false;
                } else {
                    going = true;
                }

                // Based if the user is going to the event or not, update views on the ui thread
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Setup color of going button
                        if (going)
                            buttonGoingOrEdit.setBackgroundTintList(getResources().getColorStateList(R.color.color_selected));
                        else
                            buttonGoingOrEdit.setBackgroundTintList(getResources().getColorStateList(R.color.color_unselected));

                        // If the going button is clicked
                        buttonGoingOrEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                // Stop user from clicking
                                if (canClickGoing) {
                                    canClickGoing = false;

                                    // If the user is going to the event
                                    if (going) {

                                        // Reset thread check bool variables having to do with going
                                        eventsFirebase.notGoingThreadCheck = false;
                                        eventsFirebase.deleteThreadCheck = false;

                                        // Update database user is not going to event
                                        eventsFirebase.notGoingToAnEvent(eventId);
                                        eventsFirebase.deleteEventRegistration(eventId);

                                        // A thread to check if the data has been pushed to the database
                                        new Thread(new Runnable() {

                                            @Override
                                            public void run() {

                                                // While the post request is processing
                                                while (!eventsFirebase.notGoingThreadCheck || !eventsFirebase.deleteThreadCheck) {
                                                    try {
                                                        Thread.sleep(77);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }

                                                }

                                                // Reset instance variables having to do with going
                                                canClickGoing = true;
                                                going = false;

                                                // Refresh the event
                                                mySwipeRefreshLayout.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        refreshListener.onRefresh();
                                                    }

                                                });

                                            }
                                        }).start();
                                    // If the user is going to the event
                                    } else {
                                        // Reset thread check bool variables having to do with going
                                        eventsFirebase.notGoingThreadCheck = false;
                                        eventsFirebase.goingToEventThreadCheck = false;

                                        // Update database user is going to event
                                        eventsFirebase.notGoingToAnEvent(eventId);
                                        eventsFirebase.goingToAnEvent(eventId);

                                        // A thread to check if the data has been pushed to the database
                                        new Thread(new Runnable() {

                                            @Override
                                            public void run() {

                                                // While the post request is processing wait
                                                while (!eventsFirebase.notGoingThreadCheck || !eventsFirebase.goingToEventThreadCheck) {
                                                    try {
                                                        Thread.sleep(Integer.parseInt(getString(R.string.sleepTime)));
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                                // Reset instance variables having to do with going
                                                canClickGoing = true;
                                                going = true;

                                                // Refresh the event
                                                mySwipeRefreshLayout.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //Log.v("EDF", "swiperefresh2");
                                                        // directly call onRefresh() method
                                                        refreshListener.onRefresh();
                                                    }
                                                });

                                            }
                                        }).start();

                                    }
                                }
                            }
                        });
                    }
                });
            }
        }).start();

        // Get the image string from event details
        String imageFile = event.getPicture();

        // If the image file exists
        if (imageFile != null && imageFile != "") {

            // Attempt to get bitmap from imagefile
            Bitmap src = null;
            try {
                byte[] imageAsBytes = Base64.decode(imageFile, Base64.DEFAULT);
                src = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            } catch(OutOfMemoryError e) {
                System.err.println(e.toString());
            }

            // If successful, set the event picture view to the bitmap
            if (src != null)
                eventPic.setImageBitmap(src);
        }

    }

    /*---------------------------------------------------------------------------
    Function Name:                onResume()
    Description:                  Every time the this fragment comes into view
                                  remove the search toolbar and refresh the page
    Input:                        None.
    Output:                       None.
   ---------------------------------------------------------------------------*/
    @Override
    public void onResume(){
        super.onResume();

        // Remove the search bar
        ((MainActivity)getActivity()).removeSearchToolbar();

        // Only if it isn't the first time this fragment is being created
        if (!first) {

            // Refresh this fragment/update event details
            mySwipeRefreshLayout.post(new Runnable() {
                @Override public void run() {
                    refreshListener.onRefresh();
                }
            });

        // Set first to false after this method is run once
        } else
            first = false;

    }

    /*---------------------------------------------------------------------------
    Function Name:                startProfileFragment()
    Description:                  Switches the view to the profile fragment
                                  passing in the required fields
    Input:                        final UserFirebase userFirebase
    Output:                       None.
    ---------------------------------------------------------------------------*/
    private void startProfileFragment(final UserFirebase userFirebase) {

        // Create a new profile fragment
        Fragment profileFragment = new ProfileFragment();

        // Pass in the type of the profile
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.profile_type),
                getString(R.string.profile_type_other));
        // Pass in details of the user to profile
        bundle.putSerializable(getString(R.string.user_details), userFirebase.anotherUser);
        profileFragment.setArguments(bundle);

        // Remove the search bar for profile
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).removeSearchToolbar();
            }
        });

        // Bring the profile into view for the user
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame_layout, profileFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(getString(R.string.fragment_profile))
                .commit();
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