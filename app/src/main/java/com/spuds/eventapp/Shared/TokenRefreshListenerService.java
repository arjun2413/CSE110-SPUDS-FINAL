package com.spuds.eventapp.Shared;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Jonathan on 5/7/16.
 */

public class TokenRefreshListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        Intent i = new Intent(this, RegistrationService.class);
        startService(i);
    }

}