package com.spuds.eventapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by David on 4/22/16.
 */
public class MySubscriptionsRVAdapter extends RecyclerView.Adapter<MySubscriptionsRVAdapter.SubViewHolder>{

    public static class SubViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView subName;
        ImageView subPhoto;

        SubViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            subName = (TextView)itemView.findViewById(R.id.sub_name);
            subPhoto = (ImageView)itemView.findViewById(R.id.sub_photo);
        }
    }

    List<Subscription> subscriptions;

    MySubscriptionsRVAdapter(List<Subscription> subscriptions){

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
    public void onBindViewHolder(SubViewHolder personViewHolder, int i) {
        personViewHolder.subName.setText(subscriptions.get(i).name);
        personViewHolder.subPhoto.setImageResource(subscriptions.get(i).photoId);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



}
