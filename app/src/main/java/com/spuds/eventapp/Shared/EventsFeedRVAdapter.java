package com.spuds.eventapp.Shared;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.R;

import java.util.List;

/**
 * Created by tina on 4/16/16.
 */
public class EventsFeedRVAdapter extends RecyclerView.Adapter<EventsFeedRVAdapter.EventViewHolder> {

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        ImageView eventPic;
        TextView eventName;
        TextView eventLocation;
        TextView eventDate;
        TextView eventAttendees;
        TextView eventCategories;
        TextView eventHost;

        EventViewHolder(View itemView) {
            super(itemView);
            eventPic = (ImageView)itemView.findViewById(R.id.event_pic);
            eventName = (TextView)itemView.findViewById(R.id.event_name);
            eventLocation = (TextView)itemView.findViewById(R.id.event_loc);
            eventDate = (TextView)itemView.findViewById(R.id.event_date);
            eventAttendees = (TextView)itemView.findViewById(R.id.event_attendees);
            eventCategories = (TextView)itemView.findViewById(R.id.event_categories);
            eventHost = (TextView)itemView.findViewById(R.id.event_host);
        }
    }

    List<Event> events;

    public EventsFeedRVAdapter(List<Event> events){
        this.events = events;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_event_feed, viewGroup, false);
        EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        /* Picasso for eventPic*/
        eventViewHolder.eventName.setText(events.get(i).name);
        eventViewHolder.eventLocation.setText(events.get(i).location);
        eventViewHolder.eventDate.setText(events.get(i).date);
        eventViewHolder.eventAttendees.setText(String.valueOf(events.get(i).attendees));

        if (events.get(i).categTwo != null)
            eventViewHolder.eventCategories.setText(events.get(i).categOne + ", " + events.get(i).categTwo);
        else
            eventViewHolder.eventCategories.setText(events.get(i).categOne);

        eventViewHolder.eventHost.setText(events.get(i).host);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
