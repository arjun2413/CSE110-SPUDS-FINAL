package com.spuds.eventapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tina on 4/16/16.
 */
public class EventsRVAdapter extends RecyclerView.Adapter<EventsRVAdapter.PersonViewHolder> {

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView eventName;
        TextView eventLocation;

        PersonViewHolder(View itemView) {
            super(itemView);
            eventName = (TextView)itemView.findViewById(R.id.event_name);
            eventLocation = (TextView)itemView.findViewById(R.id.event_location);
        }
    }

    List<Event> events;

    EventsRVAdapter(List<Event> events){
        this.events = events;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_event_feed, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.eventName.setText(events.get(i).name);
        personViewHolder.eventLocation.setText(events.get(i).location);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
