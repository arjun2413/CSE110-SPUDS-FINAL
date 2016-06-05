package com.spuds.eventapp.Firebase;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.SubUser;
import com.spuds.eventapp.Shared.Subscription;
import com.spuds.eventapp.Shared.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tina on 5/2/16.
 */
/*---------------------------------------------------------------------------
Class Name:             UserFirebase
Description:            Contains relevant methods for interacting with User
                        information with Firebase
---------------------------------------------------------------------------*/
public class UserFirebase {

    //declare and instantiate variables
    public boolean threadCheck = false;
    public boolean threadCheckAnotherUser = false;
    public static String uId;
    public static User thisUser = new User();
    public User anotherUser;

    /*---------------------------------------------------------------------------
    Constructor Name:           UserFirebase
    Description:                Create a new User
    ---------------------------------------------------------------------------*/
    public UserFirebase() {
        anotherUser = new User();
    }

    /*---------------------------------------------------------------------------
    Function Name:                getMyAccountDetails
    Description:                  Fetch information from Firebase
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public void getMyAccountDetails() {
        //Thread flag
        threadCheck = false;

        //declare and initialize Firebase object (custom URL) and Query object
        final Firebase ref = new Firebase("https://eventory.firebaseio.com/users");
        Query queryRef = ref.child(uId);

        //add child event listener
        queryRef.addChildEventListener(new ChildEventListener() {
            /*---------------------------------------------------------------------------
            Function Name:                  onChildAdded()
            Description:                    for each child, extract user details
            Input:                          DataSnapshot snapshot - snapshot of data from Firebase
                                            String previousChild - string of previous child object
            Output:                         None.
            ---------------------------------------------------------------------------*/
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {

                //switch case for name, desc, notifications, number_following, number_hosting, picture info retrieval
                switch (snapshot.getKey()) {

                    case "name":
                        thisUser.setName(String.valueOf(snapshot.getValue()));
                        break;
                    case "description":
                        thisUser.setDescription(String.valueOf(snapshot.getValue()));
                        break;
                    case "notification_toggle":
                        thisUser.setNotificationToggle(Boolean.valueOf(String.valueOf(snapshot.getValue())));
                        break;
                    case "number_following":
                        thisUser.setNumberFollowing(Integer.valueOf(String.valueOf(snapshot.getValue())));
                        break;
                    case "number_hosting":
                        thisUser.setNumberHosting(Integer.parseInt((String) snapshot.getValue()));
                        break;
                    case "picture":
                        thisUser.setPicture(String.valueOf(snapshot.getValue()));
                        break;
                }

                thisUser.setUserId(uId);

                threadCheck = true; //set threadcheck flag

            }

            /*---------------------------------------------------------------------------
            Function Name:              onChildChanged
            Description:                Just an unused overriden method
            Input:                      DataSnapshot, String
            Output:                     None.
            ---------------------------------------------------------------------------*/
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
            /*---------------------------------------------------------------------------
            Function Name:              onChildRemoved
            Description:                Just an unused overriden method
            Input:                      DataSnapshot, String
            Output:                     None.
            ---------------------------------------------------------------------------*/
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }
            /*---------------------------------------------------------------------------
            Function Name:              onChildMoved
            Description:                Just an unused overriden method
            Input:                      DataSnapshot, String
            Output:                     None.
            ---------------------------------------------------------------------------*/
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            /*---------------------------------------------------------------------------
            Function Name:              onCancelled
            Description:                Just an unused overriden method
            Input:                      DataSnapshot, String
            Output:                     None.
            ---------------------------------------------------------------------------*/
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /*---------------------------------------------------------------------------
    Function Name:              updateUser
    Description:                updates User information
    Input:                      User user - user to update
    Output:                     None.
    ---------------------------------------------------------------------------*/
    public static void updateUser(User user) {

        //initialize new Firebase obj for query
        final Firebase ref = new Firebase("https://eventory.firebaseio.com/users/");

        //Map to hold provider, name, description in a sort of tuple
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("provider", ref.getAuth().getProvider());
        map.put("name", user.getName());
        map.put("description", user.getDescription());
        //get picture info
        if (user.getPicture() != null && user.getPicture() != "")
            map.put("picture", user.getPicture());

        ref.child(UserFirebase.uId).updateChildren(map);
    }

    /*---------------------------------------------------------------------------
    Function Name:              updateNotificationToggle
    Description:                updates Notificaiton toggle in Firebase
    Input:                      boolean - which way to toggle
    Output:                     None.
    ---------------------------------------------------------------------------*/
    public static void updateNotificationToggle(boolean toggle) {
        final Firebase ref = new Firebase("https://eventory.firebaseio.com/users/");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("notification_toggle", String.valueOf(toggle));
        ref.child(UserFirebase.uId).updateChildren(map);
    }

    /*---------------------------------------------------------------------------
    Function Name:              getAnotherUser
    Description:                retrieve another user from Firebase
    Input:                      String userId - user ID of user to retrieve
    Output:                     None.
    ---------------------------------------------------------------------------*/
    public void getAnotherUser(String userId) {

        final String finalUserId = userId;
        threadCheckAnotherUser = false;

        final Firebase ref = new Firebase("https://eventory.firebaseio.com/users");
        Query queryRef = ref.child(userId);

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {


                switch (snapshot.getKey()) {
                    case "name":
                        anotherUser.setName(String.valueOf(snapshot.getValue()));
                        break;
                    case "description":
                        anotherUser.setDescription(String.valueOf(snapshot.getValue()));
                        break;
                    case "number_following":
                        anotherUser.setNumberFollowing(Integer.valueOf(String.valueOf(snapshot.getValue())));
                        break;
                    case "number_hosting":
                        anotherUser.setNumberHosting(Integer.parseInt((String) snapshot.getValue()));
                        break;
                    case "picture":
                        anotherUser.setPicture(String.valueOf(snapshot.getValue()));
                        break;
                    default:
                        Log.d("asdf", "userfirebasedefault " + snapshot.getKey());

                }

                anotherUser.setUserId(finalUserId);

                threadCheckAnotherUser = true;

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    /*---------------------------------------------------------------------------
    Function Name:              calculateInSampleSize
    Description:                updates User information
    Input:                      User user - user to update
    Output:                     None.
    ---------------------------------------------------------------------------*/
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /*---------------------------------------------------------------------------
    Function Name:              convert
    Description:                convert picture
    Input:                      Context - where input is
                                URI - URI reference
    Output:                     String
    ---------------------------------------------------------------------------*/
    public static String convert(Context context, Uri uri) {
        if (uri == null)
            return "";
        Bitmap bitmap = null;
        int bitmapWidth, bitmapHeight;

        double scale;

        try {

            InputStream stream = context.getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeStream(stream, null, options);

            options.inSampleSize = calculateInSampleSize(options, 200, 200);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            InputStream stream1 = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(stream1, null, options);

            stream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels/2;
        int width = displaymetrics.widthPixels/2;

        // Get the correct orientation uploaded
        String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
        Cursor cur = ((Activity) context).managedQuery(uri, orientationColumn, null, null, null);
        int orientation = -1;
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(orientation);

        bitmapHeight = bitmap.getHeight();
        bitmapWidth = bitmap.getWidth();

        if (orientation == 90 || orientation == 270) {
            if (bitmapHeight > bitmapWidth) {
                scale = ((double) width) / bitmapWidth;

                bitmap = Bitmap.createScaledBitmap(bitmap, width, (int) (scale * bitmapHeight), true);
            } else {
                scale = ((double) height) / bitmapHeight;

                bitmap = Bitmap.createScaledBitmap(bitmap, (int) (scale * bitmapWidth), height, true);
            }
        } else {
            if (bitmapHeight < bitmapWidth) {
                scale = ((double) width) / bitmapWidth;

                bitmap = Bitmap.createScaledBitmap(bitmap, width, (int) (scale * bitmapHeight), true);
            } else {
                scale = ((double) height) / bitmapHeight;

                bitmap = Bitmap.createScaledBitmap(bitmap, (int) (scale * bitmapWidth), height, true);
            }
        }

        // Correct bitmap to be uploaded
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bYtE);
        bitmap.recycle();
        String result;

        try {
            byte[] byteArray = bYtE.toByteArray();
            result = Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (OutOfMemoryError e ){
            System.err.println(e.toString());
            result = null;
        }
        return result;

    }

    String id = null;
    public static boolean subscribeThreadCheck = false;
    public boolean firebaseProblem;

    public void subscribe(final String otherUserid, final boolean subscribe) {
        final String otherUid = otherUserid;
        subscribeThreadCheck = false;


        if (subscribe) {
            final Firebase ref = new Firebase("https://eventory.firebaseio.com/user_following");


            Map<String, Object> map = new HashMap<String, Object>();
            map.put("user_id", uId);
            map.put("following_id", otherUid);

            ref.push().setValue(map);

            increaseAmtFollowers(otherUserid);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(!increaseNumFollowThreadCheck) {
                        try {
                            Thread.sleep(75);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //update user table #subscribed
                    subscribeThreadCheck = true;

                }
            }).start();

        } else {
            final Firebase ref = new Firebase("https://eventory.firebaseio.com/user_following");

            firebaseProblem = false;

            final ValueEventListener valueEventListener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (!firebaseProblem) {
                        firebaseProblem = true;
                        HashMap<String, Object> values = (HashMap<String, Object>) snapshot.getValue();
                        if (values != null) {
                            for (Map.Entry<String, Object> entry : values.entrySet()) {

                                boolean first = false;
                                for (Map.Entry<String, Object> entry2 : ((HashMap<String, Object>) entry.getValue()).entrySet()) {

                                    if (entry2.getKey().equals("following_id")) {
                                        if (entry2.getValue().equals(otherUserid)) {
                                            first = true;
                                        }
                                    }


                                    if (entry2.getKey().equals("user_id") && first) {
                                        if (entry2.getValue().equals(uId)) {
                                            id = entry.getKey();
                                        }
                                    }


                                }
                            }
                            decNumFollowing(otherUserid);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (!decNumFollowingThreadCheck) {
                                        try {
                                            Thread.sleep(75);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    //update user table #subscribed
                                    if (id != null)
                                        ref.child(id).removeValue();
                                    subscribeThreadCheck = true;

                                }
                            }).start();
                        }
                    }
                }

                /*---------------------------------------------------------------------------
                Function Name:              onCancelled
                Description:                Unused Overridden method
                Input:                      FirebaseError
                Output:                     None.
                ---------------------------------------------------------------------------*/
                @Override
                public void onCancelled (FirebaseError firebaseError){
                }
            };


            ref.addValueEventListener(valueEventListener);


            new Thread(new Runnable() {

                @Override
                public void run() {
                    while (!subscribeThreadCheck) {
                        try {
                            Thread.sleep(70);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    ref.removeEventListener(valueEventListener);
                }
            }).start();
        }
    }

    /*---------------------------------------------------------------------------
    Function Name:              updateNumberHosting
    Description:                update number hosting
    Input:                      none
    Output:                     None.
    ---------------------------------------------------------------------------*/
    public void updateNumberHosting() {
        final Firebase ref = new Firebase("https://eventory.firebaseio.com/users");

        Query queryRef = ref.child(uId);

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {

                if (snapshot.getKey().equals("number_hosting")) {
                    Firebase refUser = ref.child(uId);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("number_hosting", String.valueOf(Integer.parseInt((String) snapshot.getValue()) + 1));
                    refUser.updateChildren(map);
                }

            }

            /*---------------------------------------------------------------------------
            Function Name:              onChildChanged
            Description:                unused overriden method
            Input:                      DataSnapshot, String
            Output:                     None.
            ---------------------------------------------------------------------------*/
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
            /*---------------------------------------------------------------------------
            Function Name:              onChildRemoved
            Description:                unused overriden method
            Input:                      DataSnapshot, String
            Output:                     None.
            ---------------------------------------------------------------------------*/
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }
            /*---------------------------------------------------------------------------
            Function Name:              onChildMoved
            Description:                unused overriden method
            Input:                      DataSnapshot, String
            Output:                     None.
            ---------------------------------------------------------------------------*/
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            /*---------------------------------------------------------------------------
            Function Name:              onCancelled
            Description:                unused overriden method
            Input:                      DataSnapshot, String
            Output:                     None.
            ---------------------------------------------------------------------------*/
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public static boolean isSubscribedThreadCheck = false;
    public static int idIsSubscribed = 0;

    /*---------------------------------------------------------------------------
    Function Name:              isSubscribed
    Description:                checks if user is subscribed to user
    Input:                      String userId - userId to check
    Output:                     None.
    ---------------------------------------------------------------------------*/
    public void isSubscribed(final String userId) {
        idIsSubscribed = 0;
        final Firebase ref = new Firebase("https://eventory.firebaseio.com/user_following");
        final ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String id = "";
                HashMap<String, Object> values = (HashMap<String, Object>) snapshot.getValue();
                if (values != null) {

                    for (Map.Entry<String, Object> entry : values.entrySet()) {

                        boolean first = false;
                        for (Map.Entry<String, Object> entry2 : ((HashMap<String, Object>) entry.getValue()).entrySet()) {

                            boolean second = false;

                            if (entry2.getKey().equals("following_id")) {
                                if (entry2.getValue().equals(userId)) {
                                    first = true;
                                }
                            }

                            if (entry2.getKey().equals("user_id") && first) {
                                if (entry2.getValue().equals(UserFirebase.uId)) {
                                    second = true;
                                }
                            }

                            if (second) {
                                idIsSubscribed = 2;
                            }

                        }

                    }

                    if (idIsSubscribed != 2) {
                        idIsSubscribed = 1;
                    }

                    isSubscribedThreadCheck = true;

                }
            }

            /*---------------------------------------------------------------------------
            Function Name:              onCancelled
            Description:                unused overriden method
            Input:                      DataSnapshot, String
            Output:                     None.
            ---------------------------------------------------------------------------*/
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        };

        ref.addValueEventListener(valueEventListener);

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!isSubscribedThreadCheck) {
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                ref.removeEventListener(valueEventListener);
                isSubscribedThreadCheck = false;

            }
        }).start();

    }

    public static boolean getSubscriptionsThreadCheck;
    public int numSubscriptions;
    /*---------------------------------------------------------------------------
    Function Name:              getSubscriptions
    Description:                get subscriptions list
    Input:                      ArrayList<Subscription>
    Output:                     None.
    ---------------------------------------------------------------------------*/
    public void getSubscriptions(final ArrayList<Subscription> subscriptions) {
        getSubscriptionsThreadCheck = false;
        numSubscriptions = 0;
        final Firebase ref = new Firebase("https://eventory.firebaseio.com/user_following");

        final ValueEventListener valueEventListener = new ValueEventListener() {

            /*---------------------------------------------------------------------------
            Function Name:              onDataChange
            Description:                detects if something changed
            Input:                      DataSnapshot - snapshot of data from Fb
            Output:                     None.
            ---------------------------------------------------------------------------*/
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String id = "";
                HashMap<String, Object> values = (HashMap<String, Object>) snapshot.getValue();
                if (values != null) {

                    ArrayList<String> users = new ArrayList<>();
                    for (Map.Entry<String, Object> entry : values.entrySet()) {

                        String followingId = "";
                        boolean following = false;
                        for (Map.Entry<String, Object> entry2 : ((HashMap<String, Object>) entry.getValue()).entrySet()) {

                            if (entry2.getKey().equals("following_id")) {
                                followingId = String.valueOf(entry2.getValue());
                            }

                            if (entry2.getKey().equals("user_id")) {
                                if (entry2.getValue().equals(UserFirebase.uId)) {
                                    following = true;
                                }
                            }


                        }

                        if (following) {

                            ++numSubscriptions;
                            users.add(followingId);

                        }


                    }

                    recTest(users, subscriptions);

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        };

        ref.addValueEventListener(valueEventListener);
    }

    /*---------------------------------------------------------------------------
    Function Name:              recTest
    Description:                test recreation
    Input:                      ArrayList<String>, ArrayList<Subs>
    Output:                     None.
    ---------------------------------------------------------------------------*/
    void recTest(final ArrayList<String> users, final ArrayList<Subscription> subscriptions) {
        if (users.size() == 0) {
            getSubscriptionsThreadCheck = true;
            return;
        }

        getAnotherUser(users.get(0));

        //asynchronous
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!threadCheckAnotherUser) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }


                subscriptions.add(new Subscription(anotherUser.getUserId(),
                        anotherUser.getName(), anotherUser.getPicture(),
                        true));

                users.remove(0);

                recTest(users, subscriptions);


            }
        }).start();

    }

    public static boolean threadCheckSubUser;
    public static ArrayList<SubUser> subUsers;
    /*---------------------------------------------------------------------------
    Function Name:              getSubUserList
    Description:                get list of sub users
    Input:                      None
    Output:                     None.
    ---------------------------------------------------------------------------*/
    public void getSubUserList() {
        subUsers = new ArrayList<>();
        threadCheckSubUser = false;
        final Firebase myFirebaseRef = new Firebase("https://eventory.firebaseio.com/users");
        Query queryRef = myFirebaseRef.orderByKey();

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {

                SubUser subUser  = new SubUser();

                subUser.setUserId(snapshot.getKey());

                for (DataSnapshot child : snapshot.getChildren()) {
                    switch (child.getKey()) {
                        case "name":
                            subUser.setName(String.valueOf(child.getValue()));
                            break;
                    }

                }
                subUsers.add(subUser);

                threadCheckSubUser = true;

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    public static boolean increaseNumFollowThreadCheck;
    public static int numFollowing;
    /*---------------------------------------------------------------------------
    Function Name:              increaseAmtFollowers
    Description:                Purpose: Increases the amount of followers by 1
    Input:                      String userId
    Output:                     None.
    ---------------------------------------------------------------------------*/
    public void increaseAmtFollowers(final String userId) {
        increaseNumFollowThreadCheck = false;
        final Firebase myFirebaseRef = new Firebase("https://eventory.firebaseio.com/users");
        Query queryRef = myFirebaseRef.child(userId);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Checks if it is number_going that we are changing
                if (dataSnapshot.getKey().equals("number_following")) {
                    //gets the string and changes it to an int
                    numFollowing = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                    myFirebaseRef.child(userId).child("number_following").setValue(String.valueOf(++numFollowing));
                }

                increaseNumFollowThreadCheck = true;

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Log.d("how many", String.valueOf(numFollowing));
    }

    public static boolean decNumFollowingThreadCheck;
    /*---------------------------------------------------------------------------
    Function Name:              decNumFollowing
    Description:                get num of ppl following
    Input:                      String userId
    Output:                     None.
    ---------------------------------------------------------------------------*/
    public void decNumFollowing(final String userId) {
        decNumFollowingThreadCheck = false;
        final Firebase myFirebaseRef = new Firebase("https://eventory.firebaseio.com/users");
        Query queryRef = myFirebaseRef.child(userId);

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.getKey().equals("number_following")) {
                    numFollowing = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                    myFirebaseRef.child(userId).child("number_following").setValue(String.valueOf(--numFollowing));
                }

                decNumFollowingThreadCheck = true;

            }

            //unused overridden methods
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    public static boolean getSearchSubsThreadCheck;
    public static int numSearchSubs;
    /*---------------------------------------------------------------------------
    Function Name:              getSearchSubs
    Description:                get searched subs
    Input:                      ArrayList<SubUser>
    Output:                     None.
    ---------------------------------------------------------------------------*/
    public void getSearchSubs(final ArrayList<SubUser> subscriptions) {
        getSearchSubsThreadCheck = false;
        numSearchSubs = 0;
        final Firebase ref = new Firebase("https://eventory.firebaseio.com/user_following");

        final ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String id = "";
                HashMap<String, Object> values = (HashMap<String, Object>) snapshot.getValue();
                if (values != null) {

                    ArrayList<String> users = new ArrayList<>();
                    for (Map.Entry<String, Object> entry : values.entrySet()) {

                        String followingId = "";
                        boolean following = false;
                        for (Map.Entry<String, Object> entry2 : ((HashMap<String, Object>) entry.getValue()).entrySet()) {

                            if (entry2.getKey().equals("following_id")) {
                                followingId = String.valueOf(entry2.getValue());
                            }

                            if (entry2.getKey().equals("user_id")) {
                                if (entry2.getValue().equals(UserFirebase.uId)) {
                                    following = true;
                                }
                            }

                        }

                        if (following) {
                            ++numSearchSubs;
                            users.add(followingId);

                        }

                    }

                    recTestSubUser(users, subscriptions);

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        };

        ref.addValueEventListener(valueEventListener);
    }

    /*---------------------------------------------------------------------------
    Function Name:              recTestSubUser
    Description:                tester method
    Input:                      ArrayList<String>, ArrayList<SubUser>
    Output:                     None.
    ---------------------------------------------------------------------------*/
    void recTestSubUser(final ArrayList<String> users, final ArrayList<SubUser> subscriptions) {
        if (users.size() == 0) {
            getSearchSubsThreadCheck = true;
            return;
        }

        getAnotherUser(users.get(0));

        //asynchronous
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!threadCheckAnotherUser) {
                    try {
                        Thread.sleep(77);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }


                subscriptions.add(new SubUser(anotherUser.getUserId(),
                        anotherUser.getName()));

                users.remove(0);

                recTestSubUser(users, subscriptions);


            }
        }).start();

    }


}

