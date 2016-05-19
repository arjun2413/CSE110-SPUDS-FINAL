package com.spuds.eventapp.Firebase;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.spuds.eventapp.CreateEvent.CreateEventForm;
import com.spuds.eventapp.Shared.Event;

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
        ref.child("events").push().setValue(map);
    }
    public void editEvent(){

    }

    Event item;

    public Event createEL(){
        final Firebase myFirebaseRef = new Firebase("https://eventory.firebaseio.com/events");
        Query queryRef = myFirebaseRef.orderByKey();
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                int i = 0;
                for(DataSnapshot child: snapshot.getChildren()) {
                    Log.d("asdf", String.valueOf(child.getValue()));
                    //item = snapshot.getValue(Event.class);
                    i++;
                    Log.d("asd", String.valueOf(i));
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
