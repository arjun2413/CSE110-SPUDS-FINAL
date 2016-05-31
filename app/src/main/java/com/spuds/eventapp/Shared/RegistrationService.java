package com.spuds.eventapp.Shared;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by Jonathan on 5/7/16.
 */

public class RegistrationService extends IntentService {

    public static String token;

    public RegistrationService() {
        super("RegistrationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID myID = InstanceID.getInstance(this);
        //("Tryna", "handle intent");
        try {
            String registrationToken = myID.getToken(
                    String.valueOf(Integer.parseInt("2131165253")),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                    null
            );

            //("Registration Token", registrationToken);
            token = registrationToken;

            GcmPubSub subscription = GcmPubSub.getInstance(this);
            subscription.subscribe(registrationToken, "/topics/my_little_topic", null);

        }catch(Exception e){
            Log.e("REGISTRATION ERROR", e.toString());
        }
    }

    protected void setToken(String regToken) {
        this.token = regToken;
    }
}