package com.spuds.eventapp.FindPeople;

import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.User;

import java.util.ArrayList;

/**
 * Created by David on 5/22/16.
 */

/*---------------------------------------------------------------------------
Class Name:                FindPeopleRVAdapter
Description:               Adapts information from an Array List about find people
                           into the Recycler View
---------------------------------------------------------------------------*/
public class FindPeopleRVAdapter extends RecyclerView.Adapter<FindPeopleRVAdapter.FindPeopleViewHolder> {

    //array list of users
	ArrayList<User> people;
    Fragment fragment;


    /*---------------------------------------------------------------------------
    Class Name:                FindPeopleViewHolder
    Description:               Holds all the elements necessary for a person card
    ---------------------------------------------------------------------------*/
    public static class FindPeopleViewHolder extends RecyclerView.ViewHolder {
		
		CardView card;
        ImageView photo;
        TextView inviteeName;
        ImageView subscribeButton;

        public FindPeopleViewHolder(View view) {
            super(view);

            card = (CardView) view.findViewById(R.id.cv);
            photo = (ImageView) view.findViewById(R.id.find_user_image);
            inviteeName = (TextView) view.findViewById(R.id.find_user_name);
            subscribeButton = (ImageView) view.findViewById(R.id.subscribe_toggle);

        }
    }
    /*---------------------------------------------------------------------------
    Function Name:                FindPeopleRVAdapter
    Description:                  Constructor
    Input:                        ArrayList<User> people: holds information for categories
                                  Fragment currentFragment: fragment RVAdapter instantiated.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public FindPeopleRVAdapter(ArrayList<User> people, Fragment fragment) {
        this.people = people;
        this.fragment = fragment;
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreateViewHolder()
    Description:                  Necessary method to override: Defines the layout
                                  and type of each view holder
    Input:                        ViewGroup viewGroup
                                  int viewType
    Output:                       FindPeopleViewHolder
    ---------------------------------------------------------------------------*/
	@Override
    public FindPeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    	View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_people, parent, false);
        FindPeopleViewHolder fvh = new FindPeopleViewHolder(v);
        return fvh;
    }

    /*---------------------------------------------------------------------------
    Function Name:                onBindViewHolder()
    Description:                  Necessary method to override: Binds information
                                  to each view holder at position i
    Input:                        CategoryViewHolder categoryViewHolder
                                  int i - position of the item in the RecyclerView
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onBindViewHolder(FindPeopleViewHolder holder, int position) {
    	final int i = position;
    	final FindPeopleViewHolder findHolder = holder;

    	Bitmap src = BitmapFactory.decodeResource(fragment.getResources(), R.drawable.christinecropped);
        RoundedBitmapDrawable dr =
                RoundedBitmapDrawableFactory.create(fragment.getResources(), src);
        dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
        holder.photo.setImageDrawable(dr);


        holder.subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                // TODO: add isInvited boolean field, need to create "Invite" object for the button?
                if (isInvited)
                    invited.add(followers.get(i));
                else
                    invited.remove(followers.get(i));
                */
            }
        });

        holder.inviteeName.setText(people.get(position).getName());
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
        return people.size();
    }
}
