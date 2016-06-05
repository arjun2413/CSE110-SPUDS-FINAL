package com.spuds.eventapp.Search;

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
import com.spuds.eventapp.Shared.UserSearchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 5/28/16.
 */

/*---------------------------------------------------------------------------
Class Name:                SearchUsersRVAdapter
Description:               Contains information about SearchUsersRVAdapter
---------------------------------------------------------------------------*/
public class SearchUsersRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public Fragment currentFragment;
    List<UserSearchResult> users;

    /*---------------------------------------------------------------------------
    Function Name:                SearchUsersRVAdapter
    Description:                  Constructor
    Input:                        List<User> users: array list of users
                                  Fragment currentFragment: fragment RVAdapter instantiated
                                  String tagCurrentFragment
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public SearchUsersRVAdapter(ArrayList<UserSearchResult> users, SearchUsersFragment searchUsersFragment) {
        this.users = users;
        this.currentFragment = searchUsersFragment;
    }

    /*---------------------------------------------------------------------------
    Class Name:                SubViewHolder
    Description:               Holds all the elements necessary for an subscribers
    ---------------------------------------------------------------------------*/
    public static class SubViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView subName;
        ImageView subPhoto;
        Button toggleFollow;
        boolean canClick = true;

        SubViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.cv);
            subName = (TextView) itemView.findViewById(R.id.sub_name);
            subPhoto = (ImageView) itemView.findViewById(R.id.sub_photo);
            toggleFollow = (Button) itemView.findViewById(R.id.follow_toggle);
        }
    }


    /*---------------------------------------------------------------------------
    Class Name:                OwnViewHolder
    Description:               Holds all the elements necessary for the owner
    ---------------------------------------------------------------------------*/
    public static class OwnViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView subName;
        ImageView subPhoto;

        OwnViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.cv);
            subName = (TextView) itemView.findViewById(R.id.sub_name);
            subPhoto = (ImageView) itemView.findViewById(R.id.sub_photo);
        }
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreateViewHolder()
    Description:                  Necessary method to override: Defines the layout
                                  and type of each view holder
    Input:                        ViewGroup viewGroup
                                  int viewType
    Output:                       EventViewHolder
    ---------------------------------------------------------------------------*/
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder vh;
        if (viewType == 1) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_search_result, parent, false);
            overrideFonts(v.getContext(), v);
            vh = new SubViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_own_user_search_result, parent, false);
            overrideFonts(v.getContext(), v);
            vh = new OwnViewHolder(v);
        }

        return vh;
    }

    /*---------------------------------------------------------------------------
    Function Name:                onBindViewHolder()
    Description:                  Necessary method to override: Binds information
                                  to each view holder at position i
    Input:                        RecyclerView.ViewHolder viewHolder
                                  int i: position of the item in the RecyclerView
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        final UserSearchResult currentSearchResult = users.get(i);

        //get the picture from the follower
        if (viewHolder instanceof  OwnViewHolder) {
            OwnViewHolder ownViewHolder = (OwnViewHolder) viewHolder;
            ownViewHolder.subName.setText(users.get(i).name);

            if (currentSearchResult.picture != null && currentSearchResult.picture != "") {
                //attempt to convert the image string into a bitmap
                Bitmap src = null;
                try {
                    byte[] imageAsBytes = Base64.decode(currentSearchResult.picture, Base64.DEFAULT);
                    src = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                } catch (OutOfMemoryError e) {
                    System.err.println(e.toString());
                }
                //if the bitmap was created successfully
                if (src != null) {
                    //change the bitmap into a circle
                    try {
                        RoundedBitmapDrawable dr =
                                RoundedBitmapDrawableFactory.create(currentFragment.getResources(), src);
                        dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
                        ownViewHolder.subPhoto.setImageDrawable(dr);
                    } catch (OutOfMemoryError e) {
                        System.err.println(e.toString());
                    }

                } else {
                    try {
                        src = BitmapFactory.decodeResource(currentFragment.getResources(), R.drawable.profile_pic_icon);

                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(currentFragment.getResources(), src);
                        circularBitmapDrawable.setCircular(true);
                        circularBitmapDrawable.setAntiAlias(true);
                        ownViewHolder.subPhoto.setImageDrawable(circularBitmapDrawable);
                    } catch (OutOfMemoryError e) {
                        System.err.println(e.toString());
                    }
                }
            } else {
                try {
                    Bitmap src = BitmapFactory.decodeResource(currentFragment.getResources(), R.drawable.profile_pic_icon);

                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(currentFragment.getResources(), src);
                    circularBitmapDrawable.setCircular(true);
                    circularBitmapDrawable.setAntiAlias(true);
                    //set the view for the picture to the new circle picture
                    ownViewHolder.subPhoto.setImageDrawable(circularBitmapDrawable);
                } catch (OutOfMemoryError e) {
                    System.err.println(e.toString());
                }
            }

            //set on click listener for own card
            ownViewHolder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Fragment profileFragment = new ProfileFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString(currentFragment.getString(R.string.profile_type),
                            currentFragment.getString(R.string.profile_type_owner));

                    profileFragment.setArguments(bundle);

                    currentFragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((MainActivity)currentFragment.getActivity()).removeSearchToolbar();
                        }
                    });

                    currentFragment.getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_frame_layout, profileFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(currentFragment.getString(R.string.fragment_profile))
                            .commit();


                }
            });

        } else {
            final SubViewHolder subViewHolder = (SubViewHolder) viewHolder;
            subViewHolder.subName.setText(users.get(i).name);

            //subViewHolder.subPhoto.setImageResource(subscriptions.get(i).photoId);


            if (currentSearchResult.picture != null && currentSearchResult.picture != "") {
                //attempt to convert the image string into a bitmap
                Bitmap src = null;
                try {
                    byte[] imageAsBytes = Base64.decode(currentSearchResult.picture, Base64.DEFAULT);
                    src = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                } catch (OutOfMemoryError e) {
                    System.err.println(e.toString());
                }
                //if the bitmap was created successfully
                if (src != null) {
                    try {
                        //change the bitmap into a circle
                        RoundedBitmapDrawable dr =
                                RoundedBitmapDrawableFactory.create(currentFragment.getResources(), src);
                        dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
                        subViewHolder.subPhoto.setImageDrawable(dr);
                    } catch (OutOfMemoryError e) {
                        System.err.println(e.toString());
                    }

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
                    Bitmap src = BitmapFactory.decodeResource(currentFragment.getResources(), R.drawable.profile_pic_icon);

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
            //change the color of the follow button
            subViewHolder.toggleFollow.setBackgroundTintList(currentFragment.getResources().getColorStateList(R.color.color_selected));

            //on click listener for the follow button
            subViewHolder.toggleFollow.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {

                    if (subViewHolder.canClick) {

                        subViewHolder.canClick = false;

                        final UserFirebase userFirebase = new UserFirebase();
                        // already following this user
                        if (currentSearchResult.follow) {
                            currentSearchResult.follow = false;
                            userFirebase.subscribe(users.get(i).userId, false);
                        } else {
                            currentSearchResult.follow = true;
                            userFirebase.subscribe(users.get(i).userId, true);
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
                                        if (currentSearchResult.follow) {
                                            subViewHolder.toggleFollow.setBackgroundTintList(currentFragment.getResources().getColorStateList(R.color.color_selected));
                                        } else {
                                            subViewHolder.toggleFollow.setBackgroundTintList(currentFragment.getResources().getColorStateList(R.color.color_unselected));
                                        }

                                        subViewHolder.canClick = true;

                                    }
                                });
                                //subscribeThreadCheck = false;
                            }
                        }).start();
                    }
                }
            });

            //set on click listener for subscriber card
            subViewHolder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final UserFirebase userFirebase = new UserFirebase();
                    userFirebase.getAnotherUser(currentSearchResult.userId);

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
        return users.size();
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

    /*---------------------------------------------------------------------------
    Function Name:                getItemViewType()
    Description:                  gets the type of View that will be created by
                                  getView() for the specified item.
    Input:                        int position
    Output:                       int
    ---------------------------------------------------------------------------*/
    @Override
    public int getItemViewType(int position) {

        if (users.get(position).userId.equals(UserFirebase.uId))
            return 0;
        else
            return 1;

    }
}






