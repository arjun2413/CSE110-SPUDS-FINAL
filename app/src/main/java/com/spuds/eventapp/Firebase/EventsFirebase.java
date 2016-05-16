package com.spuds.eventapp.Firebase;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tina on 5/2/16.
 */
public class EventsFirebase {
    public void createEvent(String name, String date, String location, String description){
        final Firebase ref = new Firebase("https://eventory.firebaseio.com");
        Map<String, String> map = new HashMap<String, String>();
        map.put("Name", name);
        map.put("date", date);
        map.put("location", location);
        map.put("description", description);
        ref.child("events").child(name).setValue(map);
    }

}
