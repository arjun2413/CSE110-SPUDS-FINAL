package com.spuds.eventapp.Firebase;

/**
 * Created by Arjun on 5/5/16.
 */

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.spuds.eventapp.ChangePassword.ChangePasswordForm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arjun on 5/5/16.
 */
public class AccountFirebase {
    public void createAccount(String email, String password) {


        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>()

                {
                    @Override
                    public void onSuccess (Map < String, Object > result){
                        System.out.println("Successfully created user account with uid: " + result.get("uid"));
                    }

                    @Override
                    public void onError (FirebaseError firebaseError){
                        Log.v("AccountFirebase", "ERROR Creating an Account");
                    }
                }

        );
    }
    public boolean status = false;
    public void logIn(String email, String password) {

        final Firebase ref = new Firebase("https://eventory.firebaseio.com");

        ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                status = true;
                // Authentication just completed successfully smile emoticon
                Map<String, String> map = new HashMap<String, String>();
                map.put("provider", authData.getProvider());
                if (authData.getProviderData().containsKey("displayName")) {
                    map.put("displayName", authData.getProviderData().get("displayName").toString());
                }
                ref.child("users").child(authData.getUid()).setValue(map);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.v("AccountFirebase", "ERROR Logging In");
                status = false;
            }

        });
    }

    public void changePass(ChangePasswordForm form) {
        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.changePassword("bobtony@firebase.com", form.getCurrent(), form.getNext(), new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                // password changed
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // error encountered
            }
        });
    }
    void resetPass() {
        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.resetPassword("bobtony@firebase.com", new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                // password reset email sent
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // error encountered
            }
        });
    }
    public void removingAccount() {
        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.removeUser("bobtony@firebase.com", "correcthorsebatterystaple", new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                // user removed
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // error encountered
            }
        });
    }
}