package com.spuds.eventapp.Shared;

import android.util.Log;

import java.util.GregorianCalendar;

/**
 * Created by tina on 5/13/16.
 */

/*---------------------------------------------------------------------------
   Class Name: EventDate
   Description: Class that converts date for use in forms
---------------------------------------------------------------------------*/
public class EventDate {

    GregorianCalendar gregorianCalendar;
    boolean valid = false;
    public int year,month,day,hour;
    String minute;
    private String[] months = {"January","February","March","April","May","June","July","August",
                               "September","October","November","December"};
    //Constructor that converts the string date into EventDate
    public EventDate(String date) {
        if(validateString(date)) {
            //Use substring to parse each snippet of string.
            this.year = Integer.parseInt(date.substring(0,2));
            this.month = Integer.parseInt(date.substring(3,5));
            this.day = Integer.parseInt(date.substring(6,8));
            this.hour = Integer.parseInt(date.substring(11,13));
            this.minute = date.substring(14,16);

            //Check that dates are in reasonable range
            if(     this.year >= 2016 &&
                    this.month >= 1 && this.month <= 12 &&
                    this.day >= 1   && this.day <= 31 &&
                    this.hour >= 0 && this.hour <= 12 &&
                    Integer.parseInt(this.minute) >= 0 && Integer.parseInt(this.minute) <= 59) {
                gregorianCalendar = new GregorianCalendar(this.year,this.month,this.day,
                                                          this.hour,Integer.parseInt(this.minute));
            }

            if (String.valueOf(this.minute).length() == 1)
                this.minute += "0";
        }
    }

    /*---------------------------------------------------------------------------
     Function Name: get12Time
     Description: Converts the time to 12 hour time AM or PM
     Input: None
     Output: String - the 12 hour time
    ---------------------------------------------------------------------------*/
    public String get12Time() {
        if(this.hour < 12 ){
            return this.hour + ":" + this.minute + " AM";
        }
        else{
            int a = this.hour - 12;
            return a + ":" + this.minute + " PM";
        }
    }

    /*---------------------------------------------------------------------------
     Function Name: getAMPM
     Description: Determines if time is AM or PM
     Input: None
     Output: String - AM or PM
    ---------------------------------------------------------------------------*/
    public String getAMPM() {
        if(this.hour < 12 ){
            return "AM";
        }
        else{
            return "PM";
        }
    }

    /*---------------------------------------------------------------------------
     Function Name: getDate
     Description: getter method for the date
     Input: None
     Output: String - date in month day year format
    ---------------------------------------------------------------------------*/
    public String getDate() {
        int a = this.month;
        return months[a] + " " + this.day + ", " + this.year;
    }

    /*---------------------------------------------------------------------------
     Function Name: getMonth
     Description: returns the month in either uppercase or lowercase
     Input: boolean upperCase - true if uppercase
     Output: String - month in either uppercase or lowercase
    ---------------------------------------------------------------------------*/
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

    /*---------------------------------------------------------------------------
     Function Name: getDay
     Description: getter method for day
     Input: None
     Output: String - day
    ---------------------------------------------------------------------------*/
    public String getDay() {
        return Integer.toString(this.day);
    }

    /*---------------------------------------------------------------------------
     Function Name: validateString
     Description: checks the format of the string date
     Input: String s - string to check
     Output: boolean - true if valid, false if not
    ---------------------------------------------------------------------------*/
    private boolean validateString(String s){
        //use RegEx to ensure correct format. If it is in correct form, return true.
        if(s.matches("\\d{2}/\\d{2}/\\d{2} \\| \\d{2}:\\d{2}")){
            return true;
        }
        //if bad form return false;
        return false;

    }
}
