package com.spuds.eventapp.Firebase;

/**
 * Created by Arjun on 5/5/16.
 */

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
/*---------------------------------------------------------------------------
   Class Name: AccountFirebase
   Description: Contains all methods that interact with the database, and
                that have to do with account functionality.
   ---------------------------------------------------------------------------*/
public class AccountFirebase {
    public static final String PROVIDER_TYPE_PASS = "password";
    public static final String MULTIPLE_ATTEMPTS = "FirebaseError: Due to another authentication attempt, this authentication attempt was aborted before it could complete.";
    private int threadCheck = 0;
    public int status = 0;
    String token;

    //empty constructor
    public AccountFirebase() {}

    /*---------------------------------------------------------------------------
     Function Name: createAccount
     Description: Creates an account, and pushes the data to the users table
                in the database.
     Input: String email - the email of the account to create
            String password - the password of the account to create
            String name - the name of the user to create
     Output: None
    ---------------------------------------------------------------------------*/
    public void createAccount(final String email, String password, final String name) {
        final Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess (Map < String, Object > result){
                Map<String, String> map = new HashMap<String, String>();
                //provider: email+password
                map.put("provider", PROVIDER_TYPE_PASS);
                map.put("name", name);
                map.put("email", email);
                map.put("description", "");
                map.put("notification_toggle", "true");
                map.put("number_following", "0");
                map.put("number_hosting", "0");
                map.put("picture", "");
                map.put("registration_id", token);

                ref.child("users").child(String.valueOf(result.get("uid"))).setValue(map);
                UserFirebase.uId = String.valueOf(result.get("uid"));
            }

            @Override
            public void onError(FirebaseError firebaseError) {
            }
        }
        );
    }

    /*---------------------------------------------------------------------------
     Function Name: logIn
     Description: Checks the login information of the user
     Input: String email - the email of the account to log in
            String password - the password of the account to log in
     Output: None
    ---------------------------------------------------------------------------*/
    public void logIn(final String email, String password) {
        //status used in login to check when method finishes;
        status = 0;
        final Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                UserFirebase.uId = authData.getUid();
                status = 1;
            }

            //https://www.firebase.com/docs/java-api/javadoc/com/firebase/client/FirebaseError.html
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                if (firebaseError.getCode() == -24) {
                    status = 3;
                } else if (!firebaseError.toString().equals(MULTIPLE_ATTEMPTS)) {
                    status = 2;
                }
            }
        });
    }

    /*---------------------------------------------------------------------------
     Function Name: logout
     Description: Logs the user out by calling Firebase unauth
     Input: None
     Output: None
    ---------------------------------------------------------------------------*/
    public void logout() {
        final Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.unauth();
    }

    /*---------------------------------------------------------------------------
     Function Name: changePass
     Description: Changes the user's password using Firebase API
     Input: ChangePasswordForm form - a form containing the change password fields
     Output: None
    ---------------------------------------------------------------------------*/
    public void changePass(ChangePasswordForm form) {
        threadCheck = 0;
        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.changePassword(form.getEmail(), form.getCurrent(), form.getNext(), new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                // password changed
                threadCheck = 1;
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // error encountered
                threadCheck = 2;
            }
        });
    }
    /*---------------------------------------------------------------------------
     Function Name: resetPass
     Description: Calls the reset password from Firebase API, which sends an
                email to the user's email
     Input: String email - the email of the account
     Output: None
    ---------------------------------------------------------------------------*/
    public void resetPass(String email) {
        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.resetPassword(email, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                // password reset email sent
            }

            @Override
            public void onError(FirebaseError firebaseError) {
            }
        });
    }

    /*  unimplemented delete account functionality
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
    }*/

    /*---------------------------------------------------------------------------
     Function Name: getUserEmail
     Description: Checks the email of the current logged in user.
     Input: None
     Output: String - email of the user currently logged in
    ---------------------------------------------------------------------------*/
    public String getUserEmail(){
        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        return (String) ref.getAuth().getProviderData().get("email");
    }

    /* unimplemented authorization check
    public void authCheck() {
        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    // user is logged in
                    System.out.println("LOGGED_IN_AUTHCHEKC");
                } else {
                    // user is not logged in
                    System.out.println("LOGGED_OUT_AUTHCHECK");

                }
            }
        });
    } */

    /*---------------------------------------------------------------------------
     Function Name: checkEmail
     Description: Queries the firebase users table, checking if the email is
                already signed up
     Input: String email - the email to check in the database
          TextView[] tArray - an array containing the TextView that will hold the
            error message
          String error - the error message to print on failure
          boolean errorOnExist - true if the error is printed when email
            exists in database, false otherwise
     Output: None - threadCheck will set to 1 if found, and set to 2 if not found.
    ---------------------------------------------------------------------------*/
    public void checkEmail(final String email, final TextView[] tArray, final String error, final boolean errorOnExist) {
        //to be used in Activity files to check if query is finished
        threadCheck = 0;
        Firebase ref = new Firebase("https://eventory.firebaseio.com/users");

        //creates a query that checks if email exitst
        Query queryRef = ref.orderByChild("email").startAt(email).endAt(email);

        //https://www.firebase.com/docs/android/guide/retrieving-data.html
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    threadCheck = 1;
                    if (tArray[0] != null && errorOnExist) {
                        tArray[0].setText(error);
                    }
                }
                else {
                    threadCheck = 2;
                    if (tArray[0] != null && !errorOnExist) {
                        tArray[0].setText(error);
                    }
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    /*---------------------------------------------------------------------------
     Function Name: getThreadCheck
     Description: Returns the value of threadCheck, used to determine if a
                query is finished querying
     Input: None
     Output: int - value of threadCheck, usually 0 when query still in progress
    ---------------------------------------------------------------------------*/
    public int getThreadCheck() {
        return threadCheck;
    }

    /*---------------------------------------------------------------------------
     Function Name: pushRegistrationId
     Description: pushes a registration id for a user, for use with notifications
     Input: String token - registration id to be pushed
     Output: None
    ---------------------------------------------------------------------------*/
    public void pushRegistrationId(String token) {
        final Firebase ref = new Firebase("https://eventory.firebaseio.com/users");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("registration_id", token);

        ref.child(UserFirebase.uId).updateChildren(map);
    }
}