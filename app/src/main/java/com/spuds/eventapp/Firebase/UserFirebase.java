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
import com.spuds.eventapp.Shared.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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


                Log.v("asdf","keykeykey" + snapshot.toString());

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

    public static String convert(Context context, Uri uri) {
        if (uri == null)
            return "";
        Bitmap bitmap = null;
        int bitmapWidth, bitmapHeight;

        double scale;

        try {

            InputStream stream = context.getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
            bitmap = BitmapFactory.decodeStream(stream);
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

    public void subscribe(final String otherUserid, boolean subscribe) {
        final String otherUid = otherUserid;
        final Firebase ref = new Firebase("https://eventory.firebaseio.com/user_following");

        if (subscribe) {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("user_id", uId);
            map.put("following_id", otherUid);

            //Query queryRef = ref.orderByChild("email").equalTo(email);
            ref.child(UserFirebase.uId).updateChildren(map);

            //update user table #subscribed

        } else {


            ref.addValueEventListener(new ValueEventListener() {

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
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }

            });


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
}

