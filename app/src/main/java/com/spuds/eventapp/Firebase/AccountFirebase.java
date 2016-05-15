package com.spuds.eventapp.Firebase;

/**
 * Created by Arjun on 5/5/16.
 */

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.spuds.eventapp.ChangePassword.ChangePasswordForm;

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
                        Log.v("AccountFirebase", firebaseError.getMessage());
                    }
                }

        );
    }
    public void logIn() {
        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.authWithPassword("bobtony@firebase.com", "correcthorsebatterystaple", new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
                Log.v("AccountFirebase:LI",firebaseError.getMessage() );
            }
        });
    }

    public void changePass(ChangePasswordForm form) {
        Log.v("email: ", form.getEmail());
        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.changePassword(form.getEmail(), form.getCurrent(), form.getNext(), new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                // password changed
                System.out.println("Password Changed");
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // error encountered
                Log.v("AccountFirebase: CP", firebaseError.getMessage());
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

    public String getEmail(){

        //TODO: make it so we can retreive the email somehow
        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        String data = (String)ref.getAuth().getProviderData().get("email");
        return data;
    }

    public String getUserEmail(){
        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        String data = (String) ref.getAuth().getProviderData().get("email");
        return data;
    }
}
