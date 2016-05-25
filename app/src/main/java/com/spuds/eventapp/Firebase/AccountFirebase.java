package com.spuds.eventapp.Firebase;

/**
 * Created by Arjun on 5/5/16.
 */

import android.util.Log;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.spuds.eventapp.ChangePassword.ChangePasswordForm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arjun on 5/5/16.
 */
public class AccountFirebase {
    private int threadCheck = 0;
    //private Query queryRef;
    public void createAccount(final String email, String password, final String name) {


        final Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>()

                {
                    @Override
                    public void onSuccess (Map < String, Object > result){
                        System.out.println("Successfully created user account with uid: " + result.get("uid"));
                        // Authentication just completed successfully smile emoticon
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("provider", ref.getAuth().getProvider());
                        map.put("name", name);
                        map.put("email", email);
                        map.put("description", "");
                        map.put("notification_toggle", "true");
                        map.put("number_following", "0");
                        map.put("number_hosting", "0");
                        map.put("picture", "");
                        ref.child("users").child(String.valueOf(result.get("uid"))).setValue(map);

                        UserFirebase.uId = String.valueOf(result.get("uid"));
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // error encountered
                        Log.v("AccountFirebase:CA:", firebaseError.getMessage());
                    }

                }


        );
    }
    public int status = 0;
    public void logIn(final String email, String password) {
        status = 0;
        final Firebase ref = new Firebase("https://eventory.firebaseio.com");

        ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.v("AccountFirebase", "Successful Log In");

                UserFirebase.uId = authData.getUid();

                status = 1;


                // Authentication just completed successfully smile emoticon

            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.v("AccountFirebase", "ERROR Logging In");
                Log.v("accountfirebase", firebaseError.toString());
                status = 2;
            }

        });
    }

    public void changePass(ChangePasswordForm form) {
        threadCheck = 0;
        Log.v("email", form.getEmail());
        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.changePassword(form.getEmail(), form.getCurrent(), form.getNext(), new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                // password changed
                threadCheck = 1;
                System.out.println("Password Changed");
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // error encountered
                threadCheck = 2;
                Log.v("AccountFirebase:CP:", firebaseError.getMessage());
            }
        });
    }
    public void resetPass(String email) {
        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.resetPassword(email, new Firebase.ResultHandler() {
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

    public String getUserEmail(){
        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        String data = (String) ref.getAuth().getProviderData().get("email");
        return data;
    }

    /*
     * FUNCTION NAME: checkEmail
     *
     * PARAMETERS:
     * String email - the email we are checking in the database
     * TextView[] tArray - put your errorMessage textview into an array of size 1, and pass that in. If no error messages, just pass in null
     * String error - the error message you want to print on failure
     * boolean errorOnExist - pass true if the error is printed when email exists in database, false otherwise
     *
     * RESULT:
     * threadCheck will set to 1 if found, and set to 2 if not found. call getThreadCheck() to check
     */
    public void checkEmail(final String email, final TextView[] tArray, final String error, final boolean errorOnExist) {
        threadCheck = 0;
        Firebase ref = new Firebase("https://eventory.firebaseio.com/users");
        Query queryRef = ref.orderByChild("email").startAt(email).endAt(email);
        System.out.println(queryRef);
//        Firebase ref = new Firebase("https://dinosaur-facts.firebaseio.com/dinosaurs");
//        Query queryRef = ref.orderByKey();

        //https://www.firebase.com/docs/android/guide/retrieving-data.html
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    threadCheck = 1;
                    if (tArray[0] != null && errorOnExist) {
                        tArray[0].setText(error);
                    }
                    System.out.println("FOUND");
                }
                else {
                    threadCheck = 2;
                    if (tArray[0] != null && !errorOnExist) {
                        tArray[0].setText(error);
                    }
                    System.out.println("NOTFOUND");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }


            // ....
        });


        //System.out.println(status + " | " + threadCheck);
        //return threadCheck;
    }
    public int getThreadCheck() {
        return threadCheck;
    }

}
