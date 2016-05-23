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
public class FindPeopleRVAdapter extends RecyclerView.Adapter<FindPeopleRVAdapter.FindPeopleViewHolder> {
	ArrayList<User> people;
    Fragment fragment;




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

    public FindPeopleRVAdapter(ArrayList<User> people, Fragment fragment) {
        this.people = people;
        this.fragment = fragment;
    }

	@Override
    public FindPeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    	View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_people, parent, false);
        FindPeopleViewHolder fvh = new FindPeopleViewHolder(v);
        return fvh;
    }


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

        holder.inviteeName.setText(people.get(position).name);
    }

    @Override
    public int getItemCount() {
        return people.size();
    }
}
