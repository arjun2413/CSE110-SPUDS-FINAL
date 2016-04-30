package com.spuds.eventapp.Adapters;

/**
 * Created by tina on 4/16/16.
 */
public class Event {
    public String id;
    public String name;
    public String picFileName;
    public String location;
    public String date;
    public int attendees;
    public String categOne;
    public String categTwo;
    public String host;

    public Event(String id, String picFileName, String name, String location, String date, int attendees,
                 String categOne, String categTwo, String host) {
        this.id = id;
        this.name = name;
        this.picFileName = picFileName;
        this.location = location;
        this.date = date;
        this.attendees = attendees;
        this.categOne = categOne;
        this.categTwo = categTwo;
        this.host = host;
    }
}
