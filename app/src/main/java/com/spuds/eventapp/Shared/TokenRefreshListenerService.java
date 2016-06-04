package com.spuds.eventapp.Shared;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Jonathan on 5/7/16.
 */

/*---------------------------------------------------------------------------
   Class Name:                TokenRefreshListenerService
   Description:               Class to handle the instance listener
   ---------------------------------------------------------------------------*/
public class TokenRefreshListenerService extends InstanceIDListenerService {

    /*---------------------------------------------------------------------------
   Function Name:                onTokenRefresh()
   Description:                  Start the service for when the token refreshes
   Input:                        None.
   Output:                       None.
   ---------------------------------------------------------------------------*/
    @Override
    public void onTokenRefresh() {
        Intent i = new Intent(this, RegistrationService.class);
        startService(i);
    }

}