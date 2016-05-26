package com.spuds.eventapp.Shared;

import android.content.Context;
import android.graphics.Typeface;
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
import com.spuds.eventapp.Firebase.UserFirebase;
import com.spuds.eventapp.Profile.ProfileFeedFragment;
import com.spuds.eventapp.Profile.ProfileFragment;
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
        TextView eventAttendees;
        TextView eventCategories;
        TextView eventHost;
        TextView seeMore;
        TextView monthDate, dayDate;
        TextView eventTime;


        EventViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.cv);
            eventPic = (ImageView) itemView.findViewById(R.id.event_pic);
            eventName = (TextView) itemView.findViewById(R.id.event_name);
            eventLocation = (TextView) itemView.findViewById(R.id.event_loc);
            eventAttendees = (TextView) itemView.findViewById(R.id.event_attendees);
            eventCategories = (TextView) itemView.findViewById(R.id.event_categories);
            eventHost = (TextView) itemView.findViewById(R.id.event_host);
            seeMore = (TextView) itemView.findViewById(R.id.label_see_more);
            monthDate = (TextView) itemView.findViewById(R.id.date_month);
            dayDate = (TextView) itemView.findViewById(R.id.date_day);
            eventTime = (TextView) itemView.findViewById(R.id.event_time);
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
        //Change the view of this font!!!
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_event_feed, viewGroup, false);
        //Typeface raleway_light = Typeface.createFromAsset(v.getContext().getAssets(),  "raleway-light.ttf");
        overrideFonts(v.getContext(),v);

      /*  Typeface raleway_medium = Typeface.createFromAsset(getAssets(),  "Raleway-Medium.ttf");

        //title font
        TextView upload = (TextView) v.findViewById(R.id.upload);
        upload.setTypeface(raleway_medium);*/

        EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {

        final int index = i;
        // Add card See More... for profile fragment
        if (tagCurrentFragment.equals(currentFragment.getString(R.string.fragment_profile)) && i == 3) {
            eventViewHolder.seeMore.setVisibility(View.VISIBLE);


            ((ViewManager)eventViewHolder.eventPic.getParent()).removeView(eventViewHolder.eventPic);
            ((ViewManager)eventViewHolder.eventName.getParent()).removeView(eventViewHolder.eventName);
            ((ViewManager)eventViewHolder.eventLocation.getParent()).removeView(eventViewHolder.eventLocation);
            ((ViewManager)eventViewHolder.eventAttendees.getParent()).removeView(eventViewHolder.eventAttendees);
            ((ViewManager)eventViewHolder.eventCategories.getParent()).removeView(eventViewHolder.eventCategories);
            ((ViewManager)eventViewHolder.monthDate.getParent()).removeView(eventViewHolder.monthDate);
            ((ViewManager)eventViewHolder.dayDate.getParent()).removeView(eventViewHolder.dayDate);


            eventViewHolder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileFeedFragment profileFeedFragment = new ProfileFeedFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString(currentFragment.getString(R.string.tab_tag), tabType);
                    bundle.putString(currentFragment.getString(R.string.user_id), userId);

                    profileFeedFragment.setArguments(bundle);

                    ((MainActivity) currentFragment.getActivity()).removeSearchToolbar();
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
        eventViewHolder.eventName.setText(events.get(i).getEventName());
        eventViewHolder.eventLocation.setText(events.get(i).getLocation());
        eventViewHolder.eventAttendees.setText(String.valueOf(events.get(i).getAttendees()));
        eventViewHolder.eventHost.setText(events.get(i).getHostName());

        String d = "";

        switch (events.get(i).getDate().substring(3,5)) {
            case "01":
                d = "J A N";
                break;
            case "02":
                d = "F E B";
                break;
            case "03":
                d = "M A R";
                break;
            case "04":
                d = "A P R";
                break;
            case "05":
                d = "M A Y";
                break;
            case "06":
                d = "J U N";
                break;
            case "07":
                d = "J U L";
                break;
            case "08":
                d = "A U G";
                break;
            case "09":
                d = "S E P";
                break;
            case "10":
                d = "O C T";
                break;
            case "11":
                d = "N O V";
                break;
            case "12":
                d = "D E C";
                break;
        }
        eventViewHolder.monthDate.setText(d);

        eventViewHolder.dayDate.setText(String.valueOf(events.get(i).getDate().substring(6,8)));

        String tempString = events.get(i).getDate().substring(11, events.get(i).getDate().length());
        String sub = tempString.substring(0, tempString.indexOf(':'));
        String col = tempString.substring(tempString.indexOf(':'), tempString.length());
        int numb = Integer.parseInt(sub);

        //PM
        if(Integer.parseInt(sub) >= 12 && Integer.parseInt(sub) < 24) {
            if(numb != 12) {
                numb -= 12;
            }

            sub = numb + col + "PM";
        }

        //AM
        else{
            if(numb == 0) {
                numb += 12;
            }
            sub = numb + col + "AM";
        }

        eventViewHolder.eventTime.setText(sub);

        // Categories
        String categories = "";
        if(events.get(i).getCategories() != null || events.get(i).getCategories().size() != 0) {
            for (int categoryIndex = 0; categoryIndex < events.get(i).getCategories().size() - 1; categoryIndex++) {
                categories += events.get(i).getCategories().get(categoryIndex) + ", ";
            }
            categories += events.get(i).getCategories().get(events.get(i).getCategories().size() - 1);
        }

        eventViewHolder.eventCategories.setText(categories);

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

                /*if (!tabFragmentTag.equals("")) {
                    //make if else statements for all fragments that have tags
                    currentFragment = currentFragment.getActivity().getSupportFragmentManager()
                            .findFragmentByTag(tabFragmentTag);
                }*/


                ((MainActivity) currentFragment.getActivity()).removeSearchToolbar();
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

    private void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView ) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "raleway-regular.ttf"));
            }
        }
        catch (Exception e) {
        }
    }

    private void startProfileFragment(final UserFirebase userFirebase) {

        Fragment profileFragment = new ProfileFragment();

        Bundle bundle = new Bundle();
        bundle.putString(currentFragment.getString(R.string.profile_type),
                currentFragment.getString(R.string.profile_type_other));


        bundle.putSerializable(currentFragment.getString(R.string.user_details), userFirebase.anotherUser);

        profileFragment.setArguments(bundle);

        currentFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)currentFragment.getActivity()).removeSearchToolbar();
            }
        });
        // Add Event Details Fragment to fragment manager
        currentFragment.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame_layout, profileFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(currentFragment.getString(R.string.fragment_profile))
                .commit();



    }


}
