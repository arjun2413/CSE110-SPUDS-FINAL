package com.spuds.eventapp.Shared;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.EventDetails.EventDetailsFragment;
import com.spuds.eventapp.Profile.ProfileFeedFragment;
import com.spuds.eventapp.R;

import java.util.List;

/**
 * Created by tina on 4/16/16.
 */
public class EventsFeedRVAdapter extends RecyclerView.Adapter<EventsFeedRVAdapter.EventViewHolder> {

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        ImageView eventPic;
        TextView eventName;
        TextView eventLocation;
        TextView eventDate;
        TextView eventAttendees;
        TextView eventCategories;
        TextView eventHost;
        TextView seeMore;

        EventViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.cv);
            eventPic = (ImageView) itemView.findViewById(R.id.event_pic);
            eventName = (TextView) itemView.findViewById(R.id.event_name);
            eventLocation = (TextView) itemView.findViewById(R.id.event_loc);
            eventDate = (TextView) itemView.findViewById(R.id.event_date);
            eventAttendees = (TextView) itemView.findViewById(R.id.event_attendees);
            eventCategories = (TextView) itemView.findViewById(R.id.event_categories);
            eventHost = (TextView) itemView.findViewById(R.id.event_host);
            seeMore = (TextView) itemView.findViewById(R.id.label_see_more);
        }

    }

    List<Event> events;
    Fragment currentFragment;
    String tagCurrentFragment;
    String tabType;
    String userId;

    public EventsFeedRVAdapter(List<Event> events, Fragment currentFragment, String tagCurrentFragment){
        this.events = events;
        this.currentFragment = currentFragment;
        this.tagCurrentFragment = tagCurrentFragment;
    }

    public EventsFeedRVAdapter(List<Event> events, Fragment currentFragment, String tagCurrentFragment, String tabType, String userId) {
        this.events = events;
        this.currentFragment = currentFragment;
        this.tagCurrentFragment = tagCurrentFragment;
        this.tabType = tabType;
        this.userId = userId;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_event_feed, viewGroup, false);
        EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {

        // Add card See More... for profile fragment
        if (tagCurrentFragment.equals(currentFragment.getString(R.string.fragment_profile)) && i == 3) {
            eventViewHolder.seeMore.setVisibility(View.VISIBLE);


            ((ViewManager)eventViewHolder.eventPic.getParent()).removeView(eventViewHolder.eventPic);
            ((ViewManager)eventViewHolder.eventName.getParent()).removeView(eventViewHolder.eventName);
            ((ViewManager)eventViewHolder.eventLocation.getParent()).removeView(eventViewHolder.eventLocation);
            ((ViewManager)eventViewHolder.eventDate.getParent()).removeView(eventViewHolder.eventDate);
            ((ViewManager)eventViewHolder.eventAttendees.getParent()).removeView(eventViewHolder.eventAttendees);
            ((ViewManager)eventViewHolder.eventCategories.getParent()).removeView(eventViewHolder.eventCategories);

            eventViewHolder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileFeedFragment profileFeedFragment = new ProfileFeedFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString(currentFragment.getString(R.string.tab_tag), tabType);
                    bundle.putString(currentFragment.getString(R.string.user_id), userId);

                    profileFeedFragment.setArguments(bundle);

                    // Add Event Details Fragment to fragment manager
                    currentFragment.getFragmentManager().beginTransaction()
                            .show(profileFeedFragment)
                            .replace(R.id.fragment_frame_layout, profileFeedFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(currentFragment.getString(R.string.fragment_profile_feed))
                            .commit();
                }
            });

            return;
        }

        /* Picasso for eventPic*/
        eventViewHolder.eventName.setText(events.get(i).name);
        eventViewHolder.eventLocation.setText(events.get(i).location);
        eventViewHolder.eventDate.setText(events.get(i).date);
        eventViewHolder.eventAttendees.setText(String.valueOf(events.get(i).attendees));

        if (events.get(i).categTwo != null)
            eventViewHolder.eventCategories.setText(events.get(i).categOne + ", " + events.get(i).categTwo);
        else
            eventViewHolder.eventCategories.setText(events.get(i).categOne);

        final int test = i;

        eventViewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Pass the event object in a bundle to pass to Event Details Fragment
                Fragment eventDetailsFragment = new EventDetailsFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable(currentFragment.getString(R.string.event_details), events.get(test));
                eventDetailsFragment.setArguments(bundle);

                // Makes sure the fragment being hidden is the tabs fragment which includes each page fragment,
                // not just one of the view page fragments
                String tabFragmentTag = "";
                if (tagCurrentFragment.equals(currentFragment.getString(R.string.fragment_home_feed))) {
                    tabFragmentTag = currentFragment.getString(R.string.home);
                } else if(tagCurrentFragment.equals(currentFragment.getString(R.string.fragment_my_events))) {
                    tabFragmentTag = currentFragment.getString(R.string.my_events);
                } else if (tagCurrentFragment.equals(currentFragment.getString(R.string.fragment_my_sub_feed))) {
                    tabFragmentTag = currentFragment.getString(R.string.subscriptionFeed);
                } else if (tagCurrentFragment.equals(currentFragment.getString(R.string.fragment_category_feed)))
                {
                    tabFragmentTag = currentFragment.getString(R.string.category_feed);
                }

                if (!tabFragmentTag.equals("")) {
                    //make if else statements for all fragments that have tags
                    currentFragment = currentFragment.getActivity().getSupportFragmentManager()
                            .findFragmentByTag(tabFragmentTag);
                }


                // Add Event Details Fragment to fragment manager
                currentFragment.getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame_layout, eventDetailsFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(tabFragmentTag)
                        .commit();

            }
        });

    }



    @Override
    public int getItemCount() {
        return events.size();
    }
}
