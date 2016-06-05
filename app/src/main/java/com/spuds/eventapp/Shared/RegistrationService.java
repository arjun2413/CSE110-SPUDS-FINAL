package com.spuds.eventapp.Shared;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.spuds.eventapp.R;

import java.io.IOException;

/**
 * Created by Jonathan on 5/7/16.
 */

/*---------------------------------------------------------------------------
   Class Name:                RegistrationService
   Description:               Class to register the token
   ---------------------------------------------------------------------------*/
public class RegistrationService extends IntentService {

    public static String token;


    /*---------------------------------------------------------------------------
   Function Name:                About Fragment
   Description:                      Required default no-argument constructor
   Input:                                 None.
   Output:                              None.
   ---------------------------------------------------------------------------*/
    public RegistrationService() {
        super("RegistrationService");
    }

    /*---------------------------------------------------------------------------
   Function Name:                onHandleIntent()
   Description:                  Handles the token registration
   Input:                        Intent intent - the intent that we get an instance of for registration
   Output:                       None.
   ---------------------------------------------------------------------------*/
    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID myID = InstanceID.getInstance(this);
        // try to register
        try {
            String registrationToken = myID.getToken(
                    getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                    null
            );

            token = registrationToken;

            // set up the subscription and send to a topic
            GcmPubSub subscription = GcmPubSub.getInstance(this);
            subscription.subscribe(registrationToken, "/topics/my_little_topic", null);

        }catch(Exception e){
            Log.e("REGISTRATION ERROR", e.toString());
        }
    }

}