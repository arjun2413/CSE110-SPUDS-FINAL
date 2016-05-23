package com.spuds.eventapp.Firebase;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.utilities.Base64;
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

    public static String uId;
    public static User thisUser;







    public UserFirebase() {}

    public void getMyAccountDetails() {

        final Firebase ref = new Firebase("https://eventory.firebaseio.com/users");
        Log.v("asdfuhoh", uId);
        Query queryRef = ref.child(uId);

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {

                System.out.println("asdf" + "heyheyheyuserfirebaseaccountdetails");

                thisUser = new User();
                for (DataSnapshot child : snapshot.getChildren()) {
                    switch (child.getKey()) {
                        case "name":
                            thisUser.setName(String.valueOf(child.getValue()));
                            break;
                        case "description":
                            thisUser.setDescription(String.valueOf(child.getValue()));
                            break;
                        case "notification_toggle":
                            thisUser.setNotificationToggle(Boolean.valueOf(String.valueOf(child.getValue())));
                            break;
                        case "number_following":
                            thisUser.setNumberFollowing(Integer.valueOf(String.valueOf(child.getValue())));
                            break;
                        case "number_hosting":
                            thisUser.setNumberHosting(Integer.parseInt((String) child.getValue()));
                            break;
                        case "picture":
                            thisUser.setPicture(String.valueOf(child.getValue()));
                            break;
                        default:
                            Log.d("asdf", "userfirebasedefault " + child.getKey());

                    }
                }
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
        System.out.println("jkl; " + UserFirebase.uId);
        ref.child(UserFirebase.uId).updateChildren(map);
    }








    public static String convert(Context context, Uri uri) {
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
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

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

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        byte[] byte_arr = stream.toByteArray();
        return Base64.encodeBytes(byte_arr);

        // TODO (M): Upload picture
    }


}
