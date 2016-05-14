package com.spuds.eventapp.Shared;

/**
 * Created by tina on 5/13/16.
 */
public class Date {

    String date;

    public Date(String date) {
        this.date = date;
    }

    public String getTime() {
        // TODO

        return "12:00 AM";
    }

    public String getDate() {
        // TODO

        return "May 12";
    }

    public String getMonth(boolean upperCase) {
        // TODO

        if (!upperCase)
            return "May";
        else
            return "MAY";
    }

    public String getDay() {
        // TODO

        return "12";
    }
}
