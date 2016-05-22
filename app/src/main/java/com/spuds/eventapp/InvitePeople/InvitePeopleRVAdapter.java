package com.spuds.eventapp.InvitePeople;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
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

import cn.refactor.library.SmoothCheckBox;

/**
 * Created by tina on 5/16/16.
 */
public class InvitePeopleRVAdapter extends RecyclerView.Adapter<InvitePeopleRVAdapter.InviteViewHolder> {

    public static class InviteViewHolder extends RecyclerView.ViewHolder {

        ImageView photo;
        TextView followerName;
        ImageView inviteButton;
        CardView card;

        public InviteViewHolder(View view) {
            super(view);

            card = (CardView) view.findViewById(R.id.cv);
            photo = (ImageView) view.findViewById(R.id.invite_user_image);
            inviteButton = (ImageView) view.findViewById(R.id.invite_toggle);
            followerName = (TextView) view.findViewById(R.id.invite_user_name);

        }
    }

    ArrayList<User> followers;
    // TODO (C): Doesn't work // FIXME: 5/16/16
    ArrayList<User> invited;
    Fragment fragment;
    boolean selectAll;

    public InvitePeopleRVAdapter(ArrayList<User> followers, ArrayList<User> invited, Fragment fragment) {
        this.followers = followers;
        this.invited = invited;
        this.fragment = fragment;
    }

    @Override
    public InviteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite_people, parent, false);
        InviteViewHolder ivh = new InviteViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(InviteViewHolder holder, int position) {
        final int i = position;
        final InviteViewHolder inviteHolder = holder;

        Bitmap src = BitmapFactory.decodeResource(fragment.getResources(), R.drawable.christinecropped);
        RoundedBitmapDrawable dr =
                RoundedBitmapDrawableFactory.create(fragment.getResources(), src);
        dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
        holder.photo.setImageDrawable(dr);


        holder.inviteButton.setOnClickListener(new View.OnClickListener() {
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


        holder.followerName.setText(followers.get(position).name);

        /*
        //What is selectAll? How do you select all?
        if (selectAll)
            holder.scb.setChecked(true, true);
        else
            holder.scb.setChecked(false, true);
        */
    }

    @Override
    public int getItemCount() {
        return followers.size();
    }


}
