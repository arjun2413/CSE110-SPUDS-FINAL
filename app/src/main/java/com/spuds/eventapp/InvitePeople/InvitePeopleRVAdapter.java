package com.spuds.eventapp.InvitePeople;

import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        SmoothCheckBox scb;
        TextView followerName;
        CardView card;

        public InviteViewHolder(View view) {
            super(view);

            card = (CardView) view.findViewById(R.id.cv);
            scb = (SmoothCheckBox) view.findViewById(R.id.scb);
            followerName = (TextView) view.findViewById(R.id.follower_name);

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

        holder.scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if (isChecked)
                    invited.add(followers.get(i));
                else
                    invited.remove(followers.get(i));
            }
        });

        holder.followerName.setText(followers.get(position).name);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inviteHolder.scb.isChecked())
                    inviteHolder.scb.setChecked(false, true);
                else
                    inviteHolder.scb.setChecked(true, true);
            }
        });

        if (selectAll)
            holder.scb.setChecked(true, true);
        else
            holder.scb.setChecked(false, true);
    }

    @Override
    public int getItemCount() {
        return followers.size();
    }


}
