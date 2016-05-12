package com.spuds.eventapp.SubscriptionsList;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.Subscription;

import java.util.List;


/**
 * Created by David on 4/22/16.
 */
public class SubscriptionsListRVAdapter extends RecyclerView.Adapter<SubscriptionsListRVAdapter.SubViewHolder>{

    public static class SubViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView subName;
        ImageView subPhoto;
        ImageView toggleFollow;

        SubViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            subName = (TextView)itemView.findViewById(R.id.sub_name);
            subPhoto = (ImageView)itemView.findViewById(R.id.sub_photo);
            toggleFollow = (ImageView)itemView.findViewById(R.id.follow_toggle);
        }
    }

    List<Subscription> subscriptions;

    public SubscriptionsListRVAdapter(List<Subscription> subscriptions){

        this.subscriptions = subscriptions;
    }


    @Override
    public int getItemCount() {
        return subscriptions.size();
    }

    @Override
    public SubViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_subscriptions, viewGroup, false);
        SubViewHolder svh = new SubViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(SubViewHolder subViewHolder, int i) {
        final Subscription currentSub = subscriptions.get(i);
        final SubViewHolder currentSubViewHolder = subViewHolder;
        subViewHolder.subName.setText(subscriptions.get(i).name);
        subViewHolder.subPhoto.setImageResource(subscriptions.get(i).photoId);


        subViewHolder.toggleFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // already following this user
                if (currentSub.follow){
                    currentSub.follow = false;
                    // TODO (V): Add image for "Follow" button
                    //currentSubViewHolder.toggleFollow.setImageResource(R.drawable.button_not_subscribed);
                    // TODO (M): Update database for follow boolean
                }
                else{
                    currentSub.follow = true;
                    // TODO (V): Add image for "Unfollow" button
                    //currentSubViewHolder.toggleFollow.setImageResource(R.drawable.button_subscribed);
                    // TODO (M): Update database for follow boolean

                }
            }
        });
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



}
