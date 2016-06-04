package com.spuds.eventapp.Shared;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jonathan on 5/13/16.
 */
/*---------------------------------------------------------------------------
   Class Name:                PushBuilder
   Description:               Class to make sending notifications easier
   ---------------------------------------------------------------------------*/
public class PushBuilder extends AppCompatActivity{

    private Bundle message;
    String SENDER_ID = "39404161192";
    GoogleCloudMessaging gcm;
    String regid;

    /*---------------------------------------------------------------------------
   Function Name:                PushBuilder
   Description:                  Constructor for a PushBuilder object for updating events
   Input:                        String pushType - the type of push
                                 String id_event - the Firebase id of the event
                                 String host_name - the String of owner of the event that's being updated
                                 String id_item - the Firebase id of the owner of the event
                                 GoogleCloudMessaging gcm_ - the Google Cloud Messaging object
   Output:                       None.
   ---------------------------------------------------------------------------*/
    public PushBuilder(String pushType, String id_event, String host_name, String id_item, GoogleCloudMessaging gcm_) {
        this.gcm = gcm_;
        this.message = new Bundle();
        this.message.putString("action", pushType);
        this.message.putString("event_id", id_event);
        this.message.putString("host", host_name);
        this.message.putString("item_id", id_item);
    }


    /*---------------------------------------------------------------------------
   Function Name:                PushBuilder
   Description:                  Constructor for a PushBuilder object for inviting users
   Input:                        ArrayList<String> ids - an array list of user ids
                                 String id - the Firebase id of the event
                                 String host_name - the String name of teh host of the event
                                 String id_host - the Firebase id of the inviter
                                 GoogleCloudMessaging gcm_ - the Google Cloud Messaging object
   Output:                       None.
   ---------------------------------------------------------------------------*/
    public PushBuilder(ArrayList<String> ids, String id, String host_name, String id_host, GoogleCloudMessaging gcm_) {
        this.gcm = gcm_;
        this.message = new Bundle();
        this.message.putString("action", "invite");
        this.message.putString("data", ids.toString());
        this.message.putString("event_id", id);
        this.message.putString("host", host_name);
        this.message.putString("host_id", id_host);
    }

    /*---------------------------------------------------------------------------
   Function Name:                sendNotification()
   Description:                  Sends a the bundle notification to the GCM service
   Input:                        None
   Output:                       None
   ---------------------------------------------------------------------------*/
    public void sendNotification() {
        this.contactGCM(this.message, this.gcm);
    }

    /*---------------------------------------------------------------------------
    Function Name:                conctactGCM()
    Description:                  The code that contacts the GCM service
    Input:                        final Bundle data - the bundle of info to be sent
                                  final GoogleCloudMessaging gcm - the Google Cloud Messaging object
    Output:                       None
    ---------------------------------------------------------------------------*/
    private void contactGCM(final Bundle data, final GoogleCloudMessaging gcm) {
        new AsyncTask<Void, Void, String>() {
            @Override
            // async task for the background
            protected String doInBackground(Void... params) {
                String result = "";
                if (gcm == null) {
                    // try to register the ID for sending
                    try {
                        regid = gcm.register(SENDER_ID);
                    } catch (IOException ex) {
                        return "Error :" + ex.getMessage();
                    }
                }
                // try to send
                try {
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