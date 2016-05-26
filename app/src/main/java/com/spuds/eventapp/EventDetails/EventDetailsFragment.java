package com.spuds.eventapp.EventDetails;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.CreateComment.CreateCommentFragment;
import com.spuds.eventapp.EditEvent.EditEventFragment;
import com.spuds.eventapp.Firebase.EventsFirebase;
import com.spuds.eventapp.Firebase.UserFirebase;
import com.spuds.eventapp.InvitePeople.InvitePeopleFragment;
import com.spuds.eventapp.Profile.ProfileFragment;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.Comment;
import com.spuds.eventapp.Shared.Event;
import com.spuds.eventapp.Shared.MainActivity;

import java.util.ArrayList;
import java.util.List;
public class EventDetailsFragment extends Fragment {
    // Holds event details
    Event event;
    String eventId;
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
    // Reference to itself
    Fragment eventDetailsFragment;
    boolean going;
    // Comments
    RecyclerView rv;
    CommentsRVAdapter adapter;
    List<Comment> comments;
    boolean ownEvent;
    EventsFirebase eventsFirebase;


    public EventDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getArguments();
        event = (Event) extras.get(getString(R.string.event_details));
        if (event == null) {
            eventId = extras.getString(getString(R.string.event_id));
            // TODO: Fetch event using eventId
            EventsFirebase ef = new EventsFirebase();
            ef.getEventDetails(eventId);
        } else
            eventId = event.getEventId();

         eventsFirebase = new EventsFirebase();
        //eventsFirebase.goingToAnEvent(eventId);
        eventsFirebase.isGoing(eventId);

        eventDetailsFragment = this;

        if (event.getHostId().equals(UserFirebase.uId))
            ownEvent = true;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        overrideFonts(view.getContext(),view);

        Typeface raleway_medium = Typeface.createFromAsset(getActivity().getAssets(),  "Raleway-Medium.ttf");

        //title font
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

        setUpEventInformation(view);
        setupEditEvent();
        setUpComments(view);
        return view;
    }

    void setupEditEvent() {
        if (ownEvent) {
            buttonEditEvent.setVisibility(View.VISIBLE);

            buttonEditEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditEventFragment editEventFragment = new EditEventFragment();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(getString(R.string.event_details), event);
                    editEventFragment.setArguments(bundle);

                    ((MainActivity) getActivity()).removeSearchToolbar();
                    // Add Event Details Fragment to fragment manager
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_frame_layout, editEventFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack("Edit Event Fragment")
                            .commit();
                }
            });
        }
    }

    void setUpEventInformation(View view) {
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
        //TODO: picasso for event pic
        eventName.setText(event.getEventName());
        eventHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("eventsfeedrvadapter", "eventhostclicked");

                final UserFirebase userFirebase = new UserFirebase();

                userFirebase.getAnotherUser(event.getHostId());

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while (!userFirebase.threadCheckAnotherUser) {
                            try {
                                Thread.sleep(77);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                        Log.v("eventsfeedrvadapter", "returned from firebase");


                        startProfileFragment(userFirebase);

                    }
                }).start();
            }
        });
        eventLocation.setText(event.getLocation());

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
        //EventDate eD = new EventDate(event.getDate());
        eventDate.setText(swappedString);
        //eventTime.setText(eD.get12Time());
        eventAttendees.setText(String.valueOf(event.getAttendees()));
        eventHost.setText(event.getHostName());
        eventDescription.setText(event.getDescription());

        // Categories
        String categories = "";
        if(event.getCategories() != null) {
            for (int i = 0; i < event.getCategories().size() - 1; ++i) {
                //Log.v("chris", event.getCategories().get(i));
                categories += event.getCategories().get(i) + ", ";
            }
        }
        categories += event.getCategories().get(event.getCategories().size() - 1);

        eventCategories.setText(categories);

        // Click listener for the Add Comment button
        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creates Create Comment Fragment
                CreateCommentFragment createCommentFragment = new CreateCommentFragment();
                String createCommentFragmentTag = getString(R.string.fragment_create_comment);
                ((MainActivity) getActivity()).removeSearchToolbar();
                // Adds Create Comment Fragment to fragment manager
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame_layout, createCommentFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(createCommentFragmentTag)
                        .commit();
            }
        });
        invitePeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).addSearchToolbar();
                InvitePeopleFragment invitePeopleFragment = new InvitePeopleFragment();
                // Add Event Details Fragment to fragment manager
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame_layout, invitePeopleFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack("fragment_invite_people")
                        .commit();
            }
        });


        // TODO (M): Firebase call to get if you're GOING to an event

        /*if (going)
            buttonGoingOrEdit.setBackgroundColor(Color.parseColor("#5c8a8a"));
        else
            buttonGoingOrEdit.setBackgroundColor(Color.parseColor("#ffffff"));*/

        Log.d("check", "Checkpls");


        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("idIsGoing2",String.valueOf(eventsFirebase.idIsGoing));
                while (eventsFirebase.idIsGoing == 0) {
                    Log.d("areHere", "areHere");
                    try {
                        Thread.sleep(75);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("idIsGoing", String.valueOf(eventsFirebase.idIsGoing));

                }

                if (eventsFirebase.idIsGoing == 1) {
                    going = false;
                    Log.d("please", "pls");
                } else
                    going = true;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonGoingOrEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("Here1", "herepls");


                                if (going) {
                                    Log.d("edgoing", " true");
                                    //buttonGoingOrEdit.setImageResource(R.drawable.button_not_going);
                                    eventsFirebase.notGoingToAnEvent(eventId);
                                    eventsFirebase.deleteEventRegistration(eventId);

                                    going = false;
                                } else {
                                    Log.d("edgoing", " false");
                                    //buttonGoingOrEdit.setImageResource(R.drawable.button_going);

                                    eventsFirebase.goingToAnEvent(eventId);
                                    going = true;
                                }

                            }
                        });
                    }
                });
            }
        }).start();



        String imageFile = event.getPicture();

        Log.v("ag7", "imageFile = " + imageFile);

        if (imageFile != null && imageFile != "") {

            byte[] imageAsBytes = Base64.decode(imageFile, Base64.DEFAULT);
            Bitmap src = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);


            eventPic.setImageBitmap(src);
        }

    }
    void setUpComments(View view) {
        rv = (RecyclerView) view.findViewById(R.id.rv);
        //Set type of layout manager
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        // TODO (M): Get comments from database
        comments = new ArrayList<>();
        comments.add(new Comment("1", null, "Tina Nguyen", "tina.jpg", "05.06.16", "I'm the bestest", false));
        comments.add(new Comment("2", null, "Reggie Wu", "reggie.jpg", "05.06.16", "This event is fun!", false));
        comments.add(new Comment("1", null, "Tina Nguyen", "tina.jpg", "05.06.16", "I'm the bestest", false));
        comments.add(new Comment("2", null, "Reggie Wu", "reggie.jpg", "05.06.16", "This event is fun!", false));
        comments.add(new Comment("1", null, "Tina Nguyen", "tina.jpg", "05.06.16", "I'm the bestest", false));
        comments.add(new Comment("2", null, "Reggie Wu", "reggie.jpg", "05.06.16", "This event is fun!", false));
        // Create adapter for comments
        adapter = new CommentsRVAdapter(comments, this);
        // Attach adapter to RecyclerView
        rv.setAdapter(adapter);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void startProfileFragment(final UserFirebase userFirebase) {

        Fragment profileFragment = new ProfileFragment();

        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.profile_type),
                getString(R.string.profile_type_other));


        bundle.putSerializable(getString(R.string.user_details), userFirebase.anotherUser);

        profileFragment.setArguments(bundle);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)getActivity()).removeSearchToolbar();
            }
        });
        // Add Event Details Fragment to fragment manager
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame_layout, profileFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(getString(R.string.fragment_profile))
                .commit();



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