package com.spuds.eventapp.Firebase;

/**
 * Created by Arjun on 5/5/16.
 */

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.spuds.eventapp.ChangePassword.ChangePasswordForm;
import com.spuds.eventapp.Shared.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arjun on 5/5/16.
 */
public class AccountFirebase {
    private boolean check;

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
    public boolean status = true;
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
                //TODO insert emails into database
                //ref.child("users").child((String) authData.getProviderData().get("email"));
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
    public void resetPass(String email) {
        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.resetPassword(email, new Firebase.ResultHandler()
        {
            @Override
            public void onSuccess() {
                // password reset email sent
                System.out.println("Successfully sent email");
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // error encountered
                Log.v("AccountFirebase", "ERROR Resetting Password");
            }
        });
    }
    public String getUserEmail(){
        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        String data = (String) ref.getAuth().getProviderData().get("email");
        return data;
    }

    public boolean checkEmail(final String email) {
        Firebase ref = new Firebase("https://eventory.firebaseio.com/users");
        Query queryRef = ref.orderByKey();
//        Firebase ref = new Firebase("https://dinosaur-facts.firebaseio.com/dinosaurs");
//        Query queryRef = ref.orderByKey();
        System.out.println("CHECK+");
        check = false;

        //https://www.firebase.com/docs/android/guide/saving-data.html
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    //checks each value to see if email is in database
                    if (email == snapshot.getKey()) {
                        check = true;
                    }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                System.out.println("CHECK3");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                System.out.println("CHECK4");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                System.out.println("CHECK5");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("WHY" + firebaseError.getMessage());
            }
            // ....
        });
        return check;
    }
}