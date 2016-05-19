package com.spuds.eventapp.CreateEvent;

import android.widget.EditText;

/**
 * Created by qtmluong on 5/15/2016.
 */
public class CreateEventForm {
    private String name;
    private String date;
    private String location;
    private String description;

    public CreateEventForm(EditText event_name, EditText event_date, EditText event_location, EditText event_description){
        name = event_name.getText().toString();
        date = event_date.getText().toString();
        location = event_location.getText().toString();
        description = event_description.getText().toString();
    }

    public String getName(){
        return name;
    }

    public String getDate(){
        return date;
    }

    public String getLocation(){
        return location;
    }

    public String getDescription(){
        return description;
    }

    public boolean allFilled(){
        if(name.length()>0 && date.length()>0 && location.length()>0 && description.length()>0){
            return true;
        }
        else{
            return false;
        }
    }
}
