package com.spuds.eventapp.CreateEvent;

import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.CategoryTextButton;

import java.util.List;

import cn.refactor.library.SmoothCheckBox;

/**
 * Created by David on 5/17/16.
 */
public class CreateEventRVAdapter extends RecyclerView.Adapter<CreateEventRVAdapter.EventViewHolder> {

    public Fragment currentFragment;
    List<CategoryTextButton> categories;

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView text;
        SmoothCheckBox scb;

        EventViewHolder(View itemView) {
            super(itemView);
            card = (CardView)itemView.findViewById(R.id.cv);
            text = (TextView)itemView.findViewById(R.id.category_text);
            scb = (SmoothCheckBox) itemView.findViewById(R.id.category_scb);

        }
    }


    public CreateEventRVAdapter(List<CategoryTextButton> categories, Fragment currentFragment){
        this.categories = categories;
        this.currentFragment = currentFragment;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_create_event_category, viewGroup, false);
        EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        final CategoryTextButton currentSub = categories.get(i);
        eventViewHolder.text.setText(categories.get(i).text);

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
