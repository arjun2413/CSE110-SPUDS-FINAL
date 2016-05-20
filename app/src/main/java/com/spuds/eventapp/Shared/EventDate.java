package com.spuds.eventapp.Shared;

import java.util.GregorianCalendar;

/**
 * Created by tina on 5/13/16.
 */
public class EventDate {

    GregorianCalendar gregorianCalendar;
    boolean valid = false;
    int year,month,day,hour,minute;
    private String[] months = {"January","February","March","April","May","June","July","August",
                               "September","October","November","December"};


    //real quick: add option for users to enter 12-hour time
    //bro arvind

    //ctor
    public EventDate(String date) {

        //Check for proper date form
        if(validateString(date)) {

            //Use substring to parse each snippet of string.
            this.year = Integer.parseInt(date.substring(6,10));
            this.month = Integer.parseInt(date.substring(0,2));
            this.day = Integer.parseInt(date.substring(3,5));
            this.hour = Integer.parseInt(date.substring(11,13));
            this.minute = Integer.parseInt(date.substring(13,15));

            //Check that dates are in reasonable range
            if(     this.year >= 2016 &&
                    this.month >= 1 && this.month <= 12 &&
                    this.day >= 1   && this.day <= 31 &&
                    this.hour >= 0 && this.hour <= 12 &&
                    this.minute >= 0 && this.minute <= 59) {

                gregorianCalendar = new GregorianCalendar(this.year,this.month,this.day,
                                                          this.hour,this.minute);
            }
        }
    }

    public String get24Time(){
        return this.hour + ":" + this.minute;
    }

    public String get12Time() {

        if(this.hour < 12 ){
            return this.hour + ":" + this.minute + " AM";
        }
        else{
            int a = this.hour - 12;
            return a + ":" + this.minute + " PM";
        }

    }


    public String getDate() {
        int a = this.month - 1;
        return months[a] + " " + this.day;
    }

    public String getMonth(boolean upperCase) {
        //return lower case month
        if (!upperCase) {
            return months[this.month - 1];
        }
        //return upper case month
        else {
            return months[this.month - 1].toUpperCase();
        }
    }

    public String getDay() {
        return Integer.toString(this.day);
    }

    private boolean validateString(String s){
        //use RegEx to ensure correct format. If it is in correct form, return true.
        if(s.matches("\\d{2}/\\d{2}/\\d{4}\\|\\d{4}")){
            return true;
        }
        //if bad form return false;
        return false;

    }
}
