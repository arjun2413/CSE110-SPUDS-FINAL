package com.spuds.eventapp.Firebase;

import android.text.format.Time;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.spuds.eventapp.CreateEvent.CreateEventForm;
import com.spuds.eventapp.Shared.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tina on 5/2/16.
 */
public class EventsFirebase {

    public static final String tabNew = "New";
    public static final String tabHot = "Hot";
    public static final String tabNow = "Now";
    public static final String tabGoing = "Going";
    public static final String tabHosting = "Hosting";

    public static final String catAcademic = "Academic Category";
    public static final String catSports = "Sports Category";
    public static final String catSocial = "Social Category";
    public static final String catFree = "Free Category";
    public static final String catFood = "Food Category";
    public static final String catConcerts = "Concerts Category";
    public static final String catCampus = "Campus Category";


    ArrayList<Event> eventsList;
    String filter;
    int loading;

    public EventsFirebase() {

    }


    public EventsFirebase(ArrayList<Event> eventsList, int loading, String filter) {
        this.eventsList = eventsList;
        this.filter = filter;
        this.loading = loading;
    }

    public void createEvent(CreateEventForm form) {
        final Firebase ref = new Firebase("https://eventory.firebaseio.com");
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", ref.getAuth().getUid());
        map.put("event_name", form.getName());
        map.put("description", form.getDescription());
        map.put("location", form.getLocation());
        map.put("date", form.getDate());
        map.put("number_going", "1");
        map.put("picture_file_name", "event.jpg");
        map.put("created_at", "8:00pm");
        ref.child("events").push().setValue(map);
    }

    public void editEvent() {

    }

    Event item;

    public Event createEL() {
        final Firebase myFirebaseRef = new Firebase("https://eventory.firebaseio.com/events");
        Query queryRef = myFirebaseRef.orderByKey();

        switch (filter) {
            case tabNew:
                //queryRef = myFirebaseRef.orderByChild("create_at");
                break;
            case tabHot:
                queryRef = myFirebaseRef.orderByChild("number_going");
                break;
            case tabNow:
                // TODO
                break;
            case tabGoing:
                break;
            case tabHosting:
                break;
            case catAcademic:
                break;
            case catCampus:
                break;
            case catConcerts:
                break;
            case catFood:
                break;
            case catFree:
                break;
            case catSocial:
                break;
            case catSports:
                break;
        }

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Event newEvent = new Event();
                for (DataSnapshot child : snapshot.getChildren()) {
                    switch (child.getKey()) {
                        case "date":
                            newEvent.setDate(String.valueOf(child.getValue()));
                            break;
                        case "description":
                            newEvent.setDescription(String.valueOf(child.getValue()));
                            break;
                        case "event_name":
                            newEvent.setEventName(String.valueOf(child.getValue()));
                            break;
                        case "location":
                            newEvent.setLocation(String.valueOf(child.getValue()));
                            break;
                        case "number_going":
                            newEvent.setAttendees(Integer.parseInt((String) child.getValue()));
                            break;
                        case "picture_file_name":
                            newEvent.setPicFileName(String.valueOf(child.getValue()));
                            break;
                        case "host_id":
                            newEvent.setHostId(String.valueOf(child.getValue()));
                            break;
                    }

                    Log.d("asdf", String.valueOf(snapshot.getKey()));

                }

                ArrayList<String> categories = new ArrayList<>();
                categories.add("Social");
                categories.add("Concert");

                newEvent.setCategories(categories);


                eventsList.add(newEvent);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return item;
    }

    public Event getEventDetails(final String eventID) {
        final Firebase myFirebaseRef = new Firebase("https://eventory.firebaseio.com/events");
        Query queryRef = myFirebaseRef.orderByKey();
        queryRef.addChildEventListener(new ChildEventListener() {
            Event newEvent = new Event();

            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.getValue() == eventID) {
                        newEvent.setDate(String.valueOf(child.getValue()));
                        newEvent.setDescription(String.valueOf(child.getValue()));
                        newEvent.setEventName(String.valueOf(child.getValue()));
                        newEvent.setLocation(String.valueOf(child.getValue()));
                        newEvent.setAttendees(Integer.parseInt((String) child.getValue()));
                        newEvent.setPicFileName(String.valueOf(child.getValue()));
                        newEvent.setHostId(String.valueOf(child.getValue()));
                        item = newEvent;
                        break;
                    }

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
        return item;
    }
}
