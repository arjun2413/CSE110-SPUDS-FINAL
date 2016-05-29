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
public class UserFirebase {

    public boolean threadCheck = false;
    public boolean threadCheckAnotherUser = false;

    public static String uId;
    public static User thisUser = new User();
    public User anotherUser;


    public UserFirebase() {
        anotherUser = new User();
    }

    public void getMyAccountDetails() {
        threadCheck = false;

        final Firebase ref = new Firebase("https://eventory.firebaseio.com/users");
        Log.v("asdfuhoh", uId);
        Query queryRef = ref.child(uId);

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Log.v("asdf test", "snapshot" + snapshot.getValue());
                Log.v("asdf test", "snapshot" + snapshot.getKey());

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
                    default:
                        Log.d("asdf", "userfirebasedefault " + snapshot.getKey());

                }

                thisUser.setUserId(uId);

                threadCheck = true;

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

    public static void updateUser(User user) {

        final Firebase ref = new Firebase("https://eventory.firebaseio.com/users/");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("provider", ref.getAuth().getProvider());
        map.put("name", user.getName());
        map.put("description", user.getDescription());
        if (user.getPicture() != null && user.getPicture() != "")
            map.put("picture", user.getPicture());

        //Query queryRef = ref.orderByChild("email").equalTo(email);
        ref.child(UserFirebase.uId).updateChildren(map);
    }


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

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
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





    public static String convert(Context context, Uri uri) {
        if (uri == null)
            return "";
        Bitmap bitmap = null;
        int bitmapWidth, bitmapHeight;//whatdoidowithmylife

        double scale;

        try {

            Log.v("look!", "before decoding");

            InputStream stream = context.getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeStream(stream, null, options);


            options.inSampleSize = calculateInSampleSize(options, 200, 200);

            Log.v("look!", "sample size: " + options.inSampleSize);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            InputStream stream1 = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(stream1, null, options);

            Log.v("look!", "after decoding");



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

       /* ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        byte[] byte_arr = stream.toByteArray();
        return Base64.encodeBytes(byte_arr);*/

        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bYtE);
        bitmap.recycle();
        byte[] byteArray = bYtE.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);

        // TODO (M): Upload picture
    }

    String id = null;
    public static boolean subscribeThreadCheck = false;

    public void subscribe(final String otherUserid, final boolean subscribe) {
        final String otherUid = otherUserid;

        if (subscribe) {
            final Firebase ref = new Firebase("https://eventory.firebaseio.com/user_following");


            Map<String, Object> map = new HashMap<String, Object>();
            map.put("user_id", uId);
            map.put("following_id", otherUid);

            //Query queryRef = ref.orderByChild("email").equalTo(email);
            ref.push().setValue(map);

            //update user table #subscribed

        } else {
            final Firebase ref = new Firebase("https://eventory.firebaseio.com/user_following");



            final ValueEventListener valueEventListener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    HashMap<String, Object> values = (HashMap<String, Object>) snapshot.getValue();
                    if (values != null) {
                        for (Map.Entry<String, Object> entry : values.entrySet()) {
                            Log.v("Userfirebase asdf", " key" + entry.getKey());

                            boolean first = false;
                            for (Map.Entry<String, Object> entry2 : ((HashMap<String, Object>) entry.getValue()).entrySet()) {

                                Log.v("Userfirebase asdf", " entry value key" + entry2.getKey());
                                Log.v("Userfirebase asdf", " entry value value" + entry2.getValue());

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

                        Log.v("userfirebase test", "id: " + id);

                        ref.child(id).removeValue();
                        subscribeThreadCheck = true;
                    }
                }

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
                    //subscribeThreadCheck = false;

                }
            }).start();


        }


    }

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





    public static boolean isSubscribedThreadCheck = false;
    public static int idIsSubscribed = 0;

    public void isSubscribed(final String userId) {
        final Firebase ref = new Firebase("https://eventory.firebaseio.com/user_following");
        final ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String id = "";
                HashMap<String, Object> values = (HashMap<String, Object>) snapshot.getValue();
                if (values != null) {
                    boolean first = false;
                    boolean second = false;

                    for (Map.Entry<String, Object> entry : values.entrySet()) {
                        Log.v("Userfirebase asdf", " key" + entry.getKey());


                        for (Map.Entry<String, Object> entry2 : ((HashMap<String, Object>) entry.getValue()).entrySet()) {

                            Log.v("Userfirebase asdf", " entry value key" + entry2.getKey());
                            Log.v("Userfirebase asdf", " entry value value" + entry2.getValue());


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


                        }

                    }

                    if (second) {
                        idIsSubscribed = 2;
                    } else {
                        idIsSubscribed = 1;
                    }

                    isSubscribedThreadCheck = true;

                }
            }

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
    public void getSubscriptions(final ArrayList<Subscription> subscriptions) {
        getSubscriptionsThreadCheck = false;
        numSubscriptions = 0;
        final Firebase ref = new Firebase("https://eventory.firebaseio.com/user_following");

        final ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String id = "";
                HashMap<String, Object> values = (HashMap<String, Object>) snapshot.getValue();
                if (values != null) {

                    ArrayList<String> users = new ArrayList<>();
                    for (Map.Entry<String, Object> entry : values.entrySet()) {
                        Log.v("subsfirebase", " key" + entry.getKey());


                        String followingId = "";
                        boolean following = false;
                        for (Map.Entry<String, Object> entry2 : ((HashMap<String, Object>) entry.getValue()).entrySet()) {

                            Log.v("subsfirebase", " entry value key" + entry2.getKey());
                            Log.v("subsfirebase", " entry value value" + entry2.getValue());


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
                    /*for (String userId: users) {
                        getAnotherUser(userId);
                        Log.v("userfirebasesubs", "numsubs " + numSubscriptions);
                        Log.v("userfirebasesubs", "followingId " + followingId);


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


                                subscriptions.add(new Subscription(anotherUser.getUserId(),
                                        anotherUser.getName(), anotherUser.getPicture(),
                                        true));
                                Log.v("userfirebasesubs", anotherUser.getName() + " was added");

                            }
                        }).start();
                    }*/

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        };

        ref.addValueEventListener(valueEventListener);
    }

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
                        Thread.sleep(77);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }


                subscriptions.add(new Subscription(anotherUser.getUserId(),
                        anotherUser.getName(), anotherUser.getPicture(),
                        true));
                Log.v("userfirebasesubs", anotherUser.getName() + " was added");

                users.remove(0);

                recTest(users, subscriptions);


            }
        }).start();

    }

    /*public static boolean threadCheckSubUser;
    public void getSubUserList(final ArrayList<SubUser> subUsers) {
        threadCheckSubUser = false;
        final Firebase myFirebaseRef = new Firebase("https://eventory.firebaseio.com/users");
        Query queryRef = myFirebaseRef.orderByKey();

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {

                SubUser subUser  = new SubUser();
                Log.v("asdfjkl;", snapshot.getKey());

                subUser.setUserId(snapshot.getKey());

                for (DataSnapshot child : snapshot.getChildren()) {
                    //Log.d("lmao", String.valueOf(child));
                    switch (child.getKey()) {
                        case "name":
                            subUser.setUserName(String.valueOf(child.getValue()));
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
    }*/


}

