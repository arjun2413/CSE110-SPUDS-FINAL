package com.spuds.eventapp.Firebase;

import com.firebase.client.Firebase;
import com.spuds.eventapp.CreateEvent.CreateEventForm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tina on 5/2/16.
 */
public class EventsFirebase {
    public void createEvent(CreateEventForm form){
        final Firebase ref = new Firebase("https://eventory.firebaseio.com");
        Map<String, String> map = new HashMap<String, String>();
        map.put("Name", form.getName());
        map.put("date", form.getDate());
        map.put("location", form.getLocation());
        map.put("description", form.getDescription());
        map.put("createrID", ref.getAuth().getUid());
        map.put("number_going", "1");
        ref.child("events").child(form.getName()).setValue(map);
    }

}
