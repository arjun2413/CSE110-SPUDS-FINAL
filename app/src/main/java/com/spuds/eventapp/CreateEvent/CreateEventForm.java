package com.spuds.eventapp.CreateEvent;

import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by qtmluong on 5/15/2016.
 */

/*---------------------------------------------------------------------------
   Class Name: CreateEventForm
   Description: contains the user's inputs in the fields
---------------------------------------------------------------------------*/
public class CreateEventForm {
    private String name;
    private String date;
    private String location;
    private String description;
    private String picture;

    //constructor setting all variables
    public CreateEventForm(EditText event_name, EditText event_date, EditText event_time, Spinner spinner, EditText event_location, EditText event_description, String picture){
        name = event_name.getText().toString();
        date = event_date.getText().toString() + " | " + event_time.getText().toString() + spinner.getSelectedItem().toString();;
        location = event_location.getText().toString();
        description = event_description.getText().toString();
        this.picture = picture;
    }

    //getter methods
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
    public String getPicture() {
        return picture;
    }

    /*---------------------------------------------------------------------------
         Function Name: allFilled
         Description: checks if all fields are filled
         Input: none
         Output: true if all fields are filled
    ---------------------------------------------------------------------------*/
    public boolean allFilled(){
        return (name.length()>0 && date.length()>0 && location.length()>0);
    }

    /*---------------------------------------------------------------------------
         Function Name: correctDate
         Description: checks if the date is correct format and exists
                    see createEventFragment for magic number error explanations
         Input: none
         Output: 0 on success, >0 depending on the type of error
    ---------------------------------------------------------------------------*/
    public int correctDate() {
        int month, day;

        if (date.length() < 14) { //magic number : 'MM/DD/YY | 7AM' is 14 chars (not counting quotes)
            return 1;
        }

        //format check, see above comment
        if (date.indexOf('|') != 9) {
            return 1;
        }

        //get the MM/DD/YY form
        String tempDate = date.substring(0,8);

        //check for integers and mm/dd/yy format
        if (tempDate.length() != 8) { //length of mm/dd/yy
            return 1;
        }
        else if (tempDate.charAt(2) != '/' || tempDate.charAt(5) != '/') { //check if there are '/'
            return 1;
        }
        else if (!isInteger(tempDate.substring(0,2)) || !isInteger(tempDate.substring(3,5)) || !isInteger(tempDate.substring(7)) ){
            //check if mm, dd, yy are all ints
            return 2;
        }
        else {
            month = Integer.parseInt(tempDate.substring(0,2));
            day = Integer.parseInt(tempDate.substring(3,5));
            //check ranges
            if (month < 1 || month > 12 || day == 0) { //month btwn 1,12 and day is not 0
                return 3;
            }
            if (month == 2 && day > 28) {   //february check
                //if we wanna check leap years we do that here
                return 3;
            }
            else if (has31(month) && day > 31) {    //31day month checks
                return 3;
            }
            else if (has30(month) && day > 30) {    //30day month checks
                return 3;
            }
        }
        //return success if passed all the tests
        return 0;
    }

    /* TIME SHOULD BE EITHER:
     * 1. 1 through 12 (no colon)
     * 2. 8:00 (with colon)
     *      with hour 1-12, minute 00-59
     */

    /*---------------------------------------------------------------------------
         Function Name: correctTime
         Description: checks if the time is correct format and exists
                    see createEventFragment for magic number explanations
         Input: none
         Output: 0 on success, >0 depending on the type of error
    ---------------------------------------------------------------------------*/
    public int correctTime() {
        if (date.length() < 14) {   //see correctDate for magic number
            return 1;
        }
        int index = date.indexOf('|');
        int timeEnd = date.length();
        int colon, intHour, intMin;
        String hour;
        String minute;
        String time;

        //gets time without the AM or PM
        if (index != -1) {
            time = date.substring(index+2, timeEnd - 2);
        }
        else return 1;

        //index of ':'
        colon = time.indexOf(':');
        if (colon > 0 && colon != time.length()-1) {    //this check is for if there's a :
            hour = time.substring(0, colon);    //hour part
            minute = time.substring(colon+1);     //minute part
            if (minute.length() != 2){  //minute must be in MM format
                return 4;
            }
            if (!isInteger(hour) || !isInteger(minute)) {   //check if int
                return 5;
            }
            else {
                intHour = Integer.parseInt(hour);
                intMin = Integer.parseInt(minute);
                if (intHour > 12 || intMin > 59 || intHour == 0) {  //range checks
                    return 6;
                }
            }
        }
        else if (colon == -1){  //if theres no :
            if (isInteger(time)) {
                intHour = Integer.parseInt(time);
            }
            else return 5;
            if (intHour < 1 || intHour > 12) {
                return 6;
            }
        }
        else return 4;

        //return success if passed all tests
        return 0;
    }

    /*---------------------------------------------------------------------------
         Function Name: has31
         Description: helper method for months with 31 days
         Input: int m - integer corresponding to month
         Output: true if month # has 31 days
    ---------------------------------------------------------------------------*/
    private boolean has31(int m) {
        return (m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12);
    }

    /*---------------------------------------------------------------------------
         Function Name: has30
         Description: helper method for months with 30 days
         Input: int m - integer corresponding to month
         Output: true if month # has 30 days
    ---------------------------------------------------------------------------*/
    private boolean has30(int m) {
        return (m == 4 || m == 6 || m == 9 || m == 11);
    }

    /*---------------------------------------------------------------------------
         Function Name: isInteger
         Description: checks if a string is an integer
         Input: String s - the string to check
         Output: true if the string is an integer
    ---------------------------------------------------------------------------*/
    private boolean isInteger(String s) {
        if (s == null) {
            return false;
        }
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}
