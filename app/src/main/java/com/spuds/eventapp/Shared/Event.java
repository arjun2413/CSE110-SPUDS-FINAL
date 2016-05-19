package com.spuds.eventapp.Shared;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tina on 4/16/16.
 */
public class Event implements Serializable {
    public String eventId;
    public String userId;
    public String eventName;
    public String description;
    public String location;
    public String date;
    public int attendees;
    public String picFileName;
    public ArrayList<String> categories;
    public String hostName;

     public Event(String eventId, String userId, String eventName, String description,
                  String location, String date, int attendees, String picFileName,
                  ArrayList<String>  categories, String hostName) {
         this.eventId = eventId;
         this.userId = userId;
         this.eventName = eventName;
         this.description = description;
         this.location = location;
         this.date = date;
         this.attendees = attendees;
         this.picFileName = picFileName;
         this.categories = categories;
         this.hostName = hostName;
    }
}
