package com.spuds.eventapp.SubscriptionsList;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.Firebase.UserFirebase;
import com.spuds.eventapp.Profile.ProfileFragment;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.Shared.Subscription;

import java.util.List;


/**
 * Created by David on 4/22/16.
 */

/*---------------------------------------------------------------------------
Class Name:                SubscriptionsListRVAdapter
Description:               Contains information about Subscriptions List
---------------------------------------------------------------------------*/
public class SubscriptionsListRVAdapter extends RecyclerView.Adapter<SubscriptionsListRVAdapter.SubViewHolder>{

    public Fragment currentFragment;
    //list holding all subscriptions
    List<Subscription> subscriptions;

    /*---------------------------------------------------------------------------
    Class Name:                SubViewHolder
    Description:               Holds all the elements necessary for a subscription
    ---------------------------------------------------------------------------*/
    public static class SubViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView subName;
        ImageView subPhoto;
        Button toggleFollow;
        boolean canClick = true;

        SubViewHolder(View itemView) {
            super(itemView);
            card = (CardView)itemView.findViewById(R.id.cv);
            subName = (TextView)itemView.findViewById(R.id.sub_name);
            subPhoto = (ImageView)itemView.findViewById(R.id.sub_photo);
            toggleFollow = (Button)itemView.findViewById(R.id.follow_toggle);
        }
    }


    /*---------------------------------------------------------------------------
    Function Name:                SubscriptionsListRVAdapter
    Description:                  Constructor
    Input:                        List<User> followers: array list of subscriptions
                                  Fragment currentFragment: fragment RVAdapter instantiated.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public SubscriptionsListRVAdapter(List<Subscription> subscriptions, Fragment currentFragment){
        this.subscriptions = subscriptions;
        this.currentFragment = currentFragment;
    }

    /*---------------------------------------------------------------------------
    Function Name:                getItemCount()
    Description:                  Necessary method to override: How many items
                                  in the RecyclerView
    Input:                        None
    Output:                       int: number of cards/items
    ---------------------------------------------------------------------------*/
    @Override
    public int getItemCount() {
        return subscriptions.size();
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreateViewHolder()
    Description:                  Necessary method to override: Defines the layout
                                  and type of each view holder
    Input:                        ViewGroup viewGroup
                                  int viewType
    Output:                       SubViewHolder
    ---------------------------------------------------------------------------*/
    @Override
    public SubViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_subscriptions, viewGroup, false);
        overrideFonts(v.getContext(),v);
        SubViewHolder svh = new SubViewHolder(v);
        return svh;
    }

    /*---------------------------------------------------------------------------
    Function Name:                onBindViewHolder()
    Description:                  Necessary method to override: Binds information
                                  to each view holder at position i
    Input:                        SubViewHolder subViewHolder
                                  int i: position of the item in the RecyclerView
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final SubViewHolder subViewHolder, final int i) {
        final Subscription currentSub = subscriptions.get(i);
        subViewHolder.subName.setText(subscriptions.get(i).name);

        //subViewHolder.subPhoto.setImageResource(subscriptions.get(i).photoId);


        if (currentSub.picture != null && currentSub.picture != "") {
            //attempt to convert the image string into a bitmap
            Bitmap src = null;
            try {
                byte[] imageAsBytes = Base64.decode(currentSub.picture, Base64.DEFAULT);
                src = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            } catch(OutOfMemoryError e) {
                System.err.println(e.toString());
            }
            //if the mitmap was created successfully
            if (src != null) {
                //change the bitmap into a circle
                RoundedBitmapDrawable dr =
                        RoundedBitmapDrawableFactory.create(currentFragment.getResources(), src);
                dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
                subViewHolder.subPhoto.setImageDrawable(dr);

            } else {
                try {
                    src = BitmapFactory.decodeResource(currentFragment.getResources(), R.drawable.profile_pic_icon);

                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(currentFragment.getResources(), src);
                    circularBitmapDrawable.setCircular(true);
                    circularBitmapDrawable.setAntiAlias(true);
                    //set the view for the picture to the new circle picture
                    subViewHolder.subPhoto.setImageDrawable(circularBitmapDrawable);
                } catch (OutOfMemoryError e) {
                    System.err.println(e.toString());
                }
            }
        } else {
            try {
                //convert the picture into a bitmap
                Bitmap src = BitmapFactory.decodeResource(currentFragment.getResources(), R.drawable.profile_pic_icon);

                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(currentFragment.getResources(), src);
                circularBitmapDrawable.setCircular(true);
                circularBitmapDrawable.setAntiAlias(true);
                subViewHolder.subPhoto.setImageDrawable(circularBitmapDrawable);
            } catch (OutOfMemoryError e) {
                System.err.println(e.toString());
            }
        }

        subViewHolder.toggleFollow.setBackgroundTintList(currentFragment.getResources().getColorStateList(R.color.color_selected));

        subViewHolder.toggleFollow.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                if (subViewHolder.canClick) {

                    subViewHolder.canClick = false;

                    final UserFirebase userFirebase = new UserFirebase();
                    // already following this user
                    if (currentSub.follow) {
                        currentSub.follow = false;
                        userFirebase.subscribe(subscriptions.get(i).userId, false);
                    } else {
                        currentSub.follow = true;
                        userFirebase.subscribe(subscriptions.get(i).userId, true);

                    }

                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            while (!userFirebase.subscribeThreadCheck) {
                                try {
                                    Thread.sleep(70);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            currentFragment.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (currentSub.follow) {
                                        subViewHolder.toggleFollow.setBackgroundTintList(currentFragment.getResources().getColorStateList(R.color.color_selected));
                                    } else {
                                        subViewHolder.toggleFollow.setBackgroundTintList(currentFragment.getResources().getColorStateList(R.color.color_unselected));
                                    }

                                    subViewHolder.canClick = true;

                                }
                            });


                        }
                    }).start();

                }
            }
        });

        subViewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final UserFirebase userFirebase = new UserFirebase();
                userFirebase.getAnotherUser(currentSub.userId);

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while (!userFirebase.threadCheckAnotherUser) {
                            try {
                                Thread.sleep(77);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }

                        startProfileFragment(userFirebase);

                    }
                }).start();


            }
        });
    }

    /*---------------------------------------------------------------------------
    Function Name:                startProfileFragment()
    Description:                  Switches the view to the profile fragment
                                  passing in the required fields
    Input:                        final UserFirebase userFirebase
    Output:                       None.
    ---------------------------------------------------------------------------*/
    private void startProfileFragment(final UserFirebase userFirebase) {

        //create a new profile fragment
        Fragment profileFragment = new ProfileFragment();
        //pass in the type of the profile
        Bundle bundle = new Bundle();
        bundle.putString(currentFragment.getString(R.string.profile_type),
                currentFragment.getString(R.string.profile_type_other));

        //pass in details of the user to profile
        bundle.putSerializable(currentFragment.getString(R.string.user_details), userFirebase.anotherUser);

        profileFragment.setArguments(bundle);
        
        currentFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)currentFragment.getActivity()).removeSearchToolbar();
            }
        });
        // Add Event Details Fragment to fragment manager
        currentFragment.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame_layout, profileFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(currentFragment.getString(R.string.fragment_profile))
                .commit();

    }



    /*---------------------------------------------------------------------------
    Function Name:                onAttachedToRecyclerView()
    Description:                  Called by RecyclerView when it starts observing this Adapter
    Input:                        RecyclerView recyclerView
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    /*---------------------------------------------------------------------------
    Function Name:                overrideFonts()
    Description:                  used to override fonts
    Input:                        Context context: the context we care about
                                  View v: the view we care about 
    Output:                       None.
    ---------------------------------------------------------------------------*/
    private void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView ) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Medium.ttf"));
            }
        }
        catch (Exception e) {
        }
    }

}
