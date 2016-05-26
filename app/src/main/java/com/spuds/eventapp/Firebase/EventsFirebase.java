package com.spuds.eventapp.Firebase;

import android.app.DownloadManager;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.spuds.eventapp.CreateEvent.CreateEventForm;
import com.spuds.eventapp.CreateEvent.CreateEventRVAdapter;
import com.spuds.eventapp.EditEvent.EditEventForm;
import com.spuds.eventapp.EditEvent.EditEventRVAdapter;
import com.spuds.eventapp.Shared.Event;
import com.spuds.eventapp.Shared.EventsFeedRVAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public static final String catCampus = "Student Orgs Category";


    ArrayList<Event> eventsList;
    ArrayList<String> a = new ArrayList<>();
    String tabFilter;
    String catFilter;
    String id;
    public int idIsGoing = 0;
    int loading;
    boolean isGoing;


    public EventsFirebase() {

    }

    public EventsFirebase(ArrayList<Event> eventsList, int loading, String tabFilter) {
        this.eventsList = eventsList;
        this.tabFilter = tabFilter;
        this.loading = loading;
    }

    public EventsFirebase(ArrayList<Event> eventsList, int loading, String tabFilter, String catFilter, EventsFeedRVAdapter adapter) {
        this.eventsList = eventsList;
        this.tabFilter = tabFilter;
        this.catFilter = catFilter;
        this.loading = loading;
    }

    ArrayList<String> categoryList;
    public void createEvent(CreateEventForm form, CreateEventRVAdapter adapter) {

        categoryList = adapter.getList();

        final Firebase ref = new Firebase("https://eventory.firebaseio.com");

        SimpleDateFormat df = new SimpleDateFormat("yy/MM/dd | HH:mm");
        Date dateobj = new Date();

        String originalString = form.getDate();

        char[] c = originalString.toCharArray();

        char temp = c[0];
        c[0] = c[6];
        c[6] = temp;

        char temp1 = c[1];
        c[1] = c[7];
        c[7] = temp1;

        char temp2 = c[3];
        c[3] = c[6];
        c[6] = temp2;

        char temp3 = c[4];
        c[4] = c[7];
        c[7] = temp3;
        String swappedString = new String(c);

        //change to 24 time

        String tempString = swappedString.substring(11, swappedString.length());
        int numb = 0;
        String sub = "";

        if(tempString.indexOf('A') == -1) {
            if(swappedString.indexOf(':') != -1) {
                sub = tempString.substring(0, tempString.indexOf(':'));
            }
            else{
                sub = tempString.substring(0, tempString.indexOf('P'));
            }

            numb = Integer.parseInt(sub);

            if(numb != 12) {
                numb += 12;
            }

        }
        else{
            if(swappedString.indexOf(':') != -1) {
                sub = tempString.substring(0, tempString.indexOf(':'));
            }
            else{
                sub = tempString.substring(0, tempString.indexOf('A'));
            }

            numb = Integer.parseInt(sub);

            if(numb == 12) {
                numb = 0;
            }
        }

        if(swappedString.indexOf(':') == -1) {
            swappedString = swappedString.substring(0, 11) + numb + ":00";
        }
        else {
            swappedString = swappedString.substring(0, 11) + numb + tempString.substring(tempString.indexOf(':'), tempString.length()-2);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("host_id", UserFirebase.uId);
        map.put("host_name", UserFirebase.thisUser.getName());
        map.put("event_name", form.getName());
        map.put("description", form.getDescription());
        map.put("location", form.getLocation());
        map.put("date", swappedString);
        map.put("number_going", 1);
        map.put("picture", form.getPicture());
        map.put("created_at", df.format(dateobj) );

        for(int i=0; i < categoryList.size(); i++) {
            if (categoryList.get(i) == "Academic") {
                map.put("catAcademic", "true");
            }
            if (categoryList.get(i) == "Campus Organizations") {
                map.put("catCampus", "true");
            }
            if (categoryList.get(i) == "Concerts") {
                map.put("catConcerts", "true");
            }
            if (categoryList.get(i) == "Food") {
                map.put("catFood", "true");
            }
            if (categoryList.get(i) == "Free") {
                map.put("catFree", "true");
            }
            if (categoryList.get(i) == "Social") {
                map.put("catSocial", "true");
            }
            if (categoryList.get(i) == "Sports") {
                map.put("catSports", "true");
            }
        }

            ref.child("events").push().setValue(map);

        UserFirebase userFirebase = new UserFirebase();
        userFirebase.updateNumberHosting();

    }

    public void editEvent() {

    }

    Event item;

    public Event createEL() {
        final Firebase myFirebaseRef = new Firebase("https://eventory.firebaseio.com");

        final Query[] queries = new Query[1];
        Query queryRef = myFirebaseRef.orderByKey();
        Query queryRef2 = myFirebaseRef.orderByKey();
        queries[0] = queryRef;

        switch (tabFilter) {
            case tabNew:
                queryRef = myFirebaseRef.child("events").orderByChild("created_at");
                break;
            case tabHot:
                queryRef = myFirebaseRef.child("events").orderByChild("number_going");
                break;
            case tabNow:
                queryRef = myFirebaseRef.child("events").orderByChild("date");
                break;
            case tabGoing:
                queryRef2 = myFirebaseRef.child("events_registrations").orderByChild("user_id").equalTo(UserFirebase.uId);
                break;
            case tabHosting:
                queryRef = myFirebaseRef.child("events").orderByChild("host_id").equalTo(UserFirebase.uId);
                //Log.v("uid", UserFirebase.uId);
                break;
        }

        if(catFilter != null) {
            switch (catFilter) {
                case catAcademic:
                    queryRef = myFirebaseRef.child("events").orderByChild("catAcademic").equalTo("true");
                    break;
                case catCampus:
                    queryRef = myFirebaseRef.child("events").orderByChild("catCampus").equalTo("true");
                    break;
                case catConcerts:
                    queryRef = myFirebaseRef.child("events").orderByChild("catConcerts").equalTo("true");
                    break;
                case catFood:
                    queryRef = myFirebaseRef.child("events").orderByChild("catFood").equalTo("true");
                    break;
                case catFree:
                    queryRef = myFirebaseRef.child("events").orderByChild("catFree").equalTo("true");
                    break;
                case catSocial:
                    queryRef = myFirebaseRef.child("events").orderByChild("catSocial").equalTo("true");
                    break;
                case catSports:
                    queryRef = myFirebaseRef.child("events").orderByChild("catSports").equalTo("true");
                    break;
            }
        }

        //System.out.println("metro " + tabFilter + " | " + tabGoing + " | " + tabFilter.equals(tabGoing));
        if(tabFilter.equals(tabGoing)){
            queryRef2.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        if(child.getKey().equals("event_id")) {
                            queries[0] = myFirebaseRef.child("events").orderByKey().equalTo(String.valueOf(child.getValue()));
                            queries[0].addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot snapshot, String previousChild) {

                                    Event newEvent  = new Event();

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
                                                newEvent.setAttendees(Integer.parseInt(String.valueOf(child.getValue())));
                                                break;
                                            case "picture":
                                                newEvent.setPicture(String.valueOf(child.getValue()));
                                                break;
                                            case "host_id":
                                                newEvent.setHostId(String.valueOf(child.getValue()));
                                                break;
                                            case "host_name":
                                                newEvent.setHostName(String.valueOf(child.getValue()));
                                                break;
                                            case "catAcademic":
                                                a.add("Academic");
                                                break;
                                            case "catCampus":
                                                a.add("Student Orgs");
                                                break;
                                            case "catConcerts":
                                                a.add("Concerts");
                                                break;
                                            case "catFood":
                                                a.add("Food");
                                                break;
                                            case "catFree":
                                                a.add("Free");
                                                break;
                                            case "catSocial":
                                                a.add("Social");
                                                break;
                                            case "catSports":
                                                a.add("Sports");
                                                break;
                                        }

                                        newEvent.setEventId(snapshot.getKey());
                                    }

                                   newEvent.setCategories(a);
                                    a = new ArrayList<String>();

                                    // used to get the current time
                                    String currentDate;
                                    SimpleDateFormat df = new SimpleDateFormat("yy/MM/dd | HH:mm");
                                    Date dateobj = new Date();
                                    currentDate = df.format(dateobj);


                                    if(tabFilter.equals(tabHot) || tabFilter.equals(tabNew)) {
                                        if (currentDate.compareTo(newEvent.getDate()) < 0){
                                            eventsList.add(0, newEvent);
                                        }
                                    }
                                    else {

                                        //current date and time is "earlier" than the event. Aka the event has not happened yet.
                                        if (currentDate.compareTo(newEvent.getDate()) < 0){
                                            eventsList.add(newEvent);
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
        }
        else {
            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChild) {

                    Event newEvent = new Event();

                    //Log.v("EventsFirebase jkl;", "" + snapshot.getKey());

                    for (DataSnapshot child : snapshot.getChildren()) {
                        //Log.d("lmao", String.valueOf(child));
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
                                newEvent.setAttendees(Integer.parseInt(String.valueOf(child.getValue())));
                                break;
                            case "picture":
                                newEvent.setPicture(String.valueOf(child.getValue()));
                                break;
                            case "host_id":
                                newEvent.setHostId(String.valueOf(child.getValue()));
                                break;
                            case "host_name":
                                newEvent.setHostName(String.valueOf(child.getValue()));
                                break;
                            case "catAcademic":
                                a.add("Academic");
                                break;
                            case "catCampus":
                                a.add("Student Orgs");
                                break;
                            case "catConcerts":
                                a.add("Concerts");
                                break;
                            case "catFood":
                                a.add("Food");
                                break;
                            case "catFree":
                                a.add("Free");
                                break;
                            case "catSocial":
                                a.add("Social");
                                break;
                            case "catSports":
                                a.add("Sports");
                                break;
                        }

                        //Log.d("eventsfbasdf", String.valueOf(snapshot.getKey()));
                        newEvent.setEventId(snapshot.getKey());
                    }

                    newEvent.setCategories(a);
                    a = new ArrayList<String>();


                    // used to get the current time
                    String currentDate;
                    SimpleDateFormat df = new SimpleDateFormat("yy/MM/dd | HH:mm");
                    Date dateobj = new Date();
                    currentDate = df.format(dateobj);


                    if (tabFilter.equals(tabHot) || tabFilter.equals(tabNew)) {
                        if (currentDate.compareTo(newEvent.getDate()) < 0) {
                            eventsList.add(0, newEvent);
                        }
                    } else {

                        //current date and time is "earlier" than the event. Aka the event has not happened yet.
                        if (currentDate.compareTo(newEvent.getDate()) < 0) {
                            eventsList.add(newEvent);
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
        }
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
                        newEvent.setPicture(String.valueOf(child.getValue()));
                        newEvent.setHostId(String.valueOf(child.getValue()));
                        newEvent.setCategories(a);
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

    int attendees = 0;

    /*Purpose: Increases the amount going by 1 */
    public void goingToAnEvent(final String eventId) {
        final Firebase myFirebaseRef = new Firebase("https://eventory.firebaseio.com/events");
        final Firebase ref = new Firebase("https://eventory.firebaseio.com");
        Query queryRef = myFirebaseRef.child(eventId);
        Log.d("Here6", "here");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("Here9", "here");
                //Checks if it is number_going that we are changing
                if (dataSnapshot.getKey().equals("number_going")) {
                    //gets the string and changes it to an int
                    attendees = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                    attendees++;
                    Log.d("Here", "here");
                    Log.d("how many1", String.valueOf(attendees));
                    myFirebaseRef.child(eventId).child("number_going").setValue(String.valueOf(attendees));
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("user_id", UserFirebase.uId);
                    map.put("event_id", eventId);
                    ref.child("events_registrations").push().setValue(map);
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

        Log.d("how many", String.valueOf(attendees));

    }

    public void notGoingToAnEvent(final String eventId) {
        final Firebase myFirebaseRef = new Firebase("https://eventory.firebaseio.com/events");
        Query queryRef = myFirebaseRef.child(eventId);
        Log.d("Here6", "here");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("Here9", "here");
                if (dataSnapshot.getKey().equals("number_going")) {
                    attendees = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                    attendees--;
                    Log.d("Here", "here");
                    Log.d("how many1", String.valueOf(attendees));
                    myFirebaseRef.child(eventId).child("number_going").setValue(String.valueOf(attendees));

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
        Log.d("how many", String.valueOf(attendees));
    }

    static boolean isGoingThreadCheck = false;

    public void isGoing(final String eventId) {
        final Firebase ref = new Firebase("https://eventory.firebaseio.com/events_registrations");
                final ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                HashMap<String, Object> values = (HashMap<String, Object>) snapshot.getValue();
                if (values != null) {
                    for (Map.Entry<String, Object> entry : values.entrySet()) {
                        Log.v("Userfirebase asdf", " key" + entry.getKey());

                        boolean first = false;
                        for (Map.Entry<String, Object> entry2 : ((HashMap<String, Object>) entry.getValue()).entrySet()) {

                            Log.v("Userfirebase asdf", " entry value key" + entry2.getKey());
                            Log.v("Userfirebase asdf", " entry value value" + entry2.getValue());

                            if (entry2.getKey().equals("event_id")) {
                                if (entry2.getValue().equals(eventId)) {
                                    first = true;
                                }
                            }


                            if (entry2.getKey().equals("user_id") && first) {
                                if (entry2.getValue().equals(UserFirebase.uId)) {
                                    id = entry.getKey();
                                }
                            }


                        }
                    }

                    Log.v("userfirebase test", "id: " + id);
                    if (id == null) {
                        idIsGoing = 1;
                    } else {
                        idIsGoing = 2;
                    }

                    isGoingThreadCheck = true;

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        };

        ref.addValueEventListener(valueEventListener);

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!isGoingThreadCheck) {
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                ref.removeEventListener(valueEventListener);
                isGoingThreadCheck = false;

            }
        }).start();



    }

    static boolean deleteThreadCheck = false;

    public void deleteEventRegistration(final String eventId){
        Log.v("Userfirebase entries", "eventId " + eventId);

        final Firebase ref = new Firebase("https://eventory.firebaseio.com/events_registrations");
        final ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                HashMap<String, Object> values = (HashMap<String, Object>) snapshot.getValue();
                if (values != null) {
                    for (Map.Entry<String, Object> entry : values.entrySet()) {
                        Log.v("Userfirebase asdf", " key" + entry.getKey());

                        boolean first = false;
                        for (Map.Entry<String, Object> entry2 : ((HashMap<String, Object>) entry.getValue()).entrySet()) {

                            Log.v("Userfirebase asdf", " entry value key" + entry2.getKey());
                            Log.v("Userfirebase asdf", " entry value value" + entry2.getValue());

                            if (entry2.getKey().equals("event_id")) {
                                if (entry2.getValue().equals(eventId)) {
                                    first = true;
                                    Log.v("Userfirebase entries", "first is true");
                                }
                            }


                            if (entry2.getKey().equals("user_id") && first) {
                                if (entry2.getValue().equals(UserFirebase.uId)) {
                                    id = entry.getKey();
                                }
                            }


                        }
                    }

                    Log.v("userfirebase test", "id: " + id);

                    if (id != null && id != "")
                        ref.child(id).removeValue();

                    deleteThreadCheck = true;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        };

        ref.addValueEventListener(valueEventListener);

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!deleteThreadCheck) {
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                ref.removeEventListener(valueEventListener);
                deleteThreadCheck = false;

            }
        }).start();

    }


    public void updateEvent(EditEventForm form, EditEventRVAdapter adapter) {
        if(a != null) {
            for (int i = 0; i < a.size(); i++) {
                Log.v("reg", a.get(i));
            }
        }

        categoryList = adapter.getList();
        //Log.d("fuck", String.valueOf(categoryList));

        final Firebase ref = new Firebase("https://eventory.firebaseio.com");

        SimpleDateFormat df = new SimpleDateFormat("yy/MM/dd | HH:mm");
        Date dateobj = new Date();

        String originalString = form.getDate();

        char[] c = originalString.toCharArray();

        char temp = c[0];
        c[0] = c[6];
        c[6] = temp;

        char temp1 = c[1];
        c[1] = c[7];
        c[7] = temp1;

        char temp2 = c[3];
        c[3] = c[6];
        c[6] = temp2;

        char temp3 = c[4];
        c[4] = c[7];
        c[7] = temp3;
        String swappedString = new String(c);

        //change to 24 time

        String tempString = swappedString.substring(11, swappedString.length());
        int numb = 0;
        String sub = "";

        if(tempString.indexOf('A') == -1) {
            if(swappedString.indexOf(':') != -1) {
                sub = tempString.substring(0, tempString.indexOf(':'));
            }
            else{
                sub = tempString.substring(0, tempString.indexOf('P'));
            }

            numb = Integer.parseInt(sub);

            if(numb != 12) {
                numb += 12;
            }

        }
        else{
            if(swappedString.indexOf(':') != -1) {
                sub = tempString.substring(0, tempString.indexOf(':'));
            }
            else{
                sub = tempString.substring(0, tempString.indexOf('A'));
            }

            numb = Integer.parseInt(sub);

            if(numb == 12) {
                numb = 0;
            }
        }

        if(swappedString.indexOf(':') == -1) {
            swappedString = swappedString.substring(0, 11) + numb + ":00";
        }
        else {
            swappedString = swappedString.substring(0, 11) + numb + tempString.substring(tempString.indexOf(':'), tempString.length()-2);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("host_id", UserFirebase.uId);
        map.put("host_name", UserFirebase.thisUser.getName());
        map.put("event_name", form.getName());
        map.put("description", form.getDescription());
        map.put("location", form.getLocation());
        map.put("date", swappedString);
        map.put("number_going", "1");
        map.put("picture", form.getPicture());
        map.put("created_at", df.format(dateobj) );

        for(int i=0; i < categoryList.size(); i++) {
            if (categoryList.get(i) == "Academic") {
                map.put("catAcademic", "true");
            }
            if (categoryList.get(i) == "Campus Organizations") {
                map.put("catCampus", "true");
            }
            if (categoryList.get(i) == "Concerts") {
                map.put("catConcerts", "true");
            }
            if (categoryList.get(i) == "Food") {
                map.put("catFood", "true");
            }
            if (categoryList.get(i) == "Free") {
                map.put("catFree", "true");
            }
            if (categoryList.get(i) == "Social") {
                map.put("catSocial", "true");
            }
            if (categoryList.get(i) == "Sports") {
                map.put("catSports", "true");
            }
        }

        ref.child("events").child(form.getEventId()).updateChildren(map);

        UserFirebase userFirebase = new UserFirebase();
        userFirebase.updateNumberHosting();

    }

}