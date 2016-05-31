package com.spuds.eventapp.Shared;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jonathan on 5/13/16.
 */
public class PushBuilder extends AppCompatActivity{

    private Bundle message;

    String SENDER_ID = "39404161192";
    GoogleCloudMessaging gcm;
    String regid;

    /*
        Constructor for both Reply and Update Notifications
        REPLY Params:
            pushType - string of the type of action, in this case "reply"
            id_event - the Firebase id of the event
            host_name - the String of the host name of the replier
            id_item - the Firebase ID of the comment
            gcm - the global GCM
        UPDATE Params:
            pushType - string of the type of action, in this case "update"
            id_event - the Firebase id of the event that's being updated
            host_name - the String of the owner of the event that's being updated
            id_item - the Firebase ID of the owner of the event
            gcm - the global GCM
     */
    public PushBuilder(String pushType, String id_event, String host_name, String id_item, GoogleCloudMessaging gcm_) {
        //("Building", "building the " + pushType);
        this.gcm = gcm_;
        this.message = new Bundle();
        this.message.putString("action", pushType);
        this.message.putString("event_id", id_event);
        this.message.putString("host", host_name);
        this.message.putString("item_id", id_item);
    }

    /*
        Constructor for invites
        Params:
            ids - an Array list of user ids
            id - the Firebase id of the event
            host_name - the String name of the host of the event
            id_host - the Firebase id of the inviter
            gcm - global GCM
    */
    public PushBuilder(ArrayList<String> ids, String id, String host_name, String id_host, GoogleCloudMessaging gcm_) {
        //("Building", "building the invite");
        this.gcm = gcm_;
        this.message = new Bundle();
        this.message.putString("action", "invite");
        this.message.putString("data", ids.toString());
        this.message.putString("event_id", id);
        this.message.putString("host", host_name);
        this.message.putString("host_id", id_host);
    }

    public void sendNotification() {
        //("Send Notification", "trying to send");
        //("Bundle values", this.message.toString());
        this.contactGCM(this.message, this.gcm);
    }


    private void contactGCM(final Bundle data, final GoogleCloudMessaging gcm) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result = "";
                if (gcm == null) {
                    try {
                        regid = gcm.register(SENDER_ID);
                        //("regid = ", regid.toString());
                    } catch (IOException ex) {
                        //("ERRORWITHGCM", "gcm");
                        return "Error :" + ex.getMessage();
                    }
                }
                // try to send
                try {
                    //("gggcm", gcm.toString());
                    regid = gcm.register(SENDER_ID);
                    //("REEED", regid.toString());
                    gcm.send(SENDER_ID + "@gcm.googleapis.com", "messageID", data);
                }
                catch (IOException e) {
                    e.printStackTrace();
                    result = "Error : " + e.getMessage();
                }
                return result;
            }
        }.execute();
    }
}