package com.spuds.eventapp.Shared;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.EventDetails.EventDetailsFragment;
import com.spuds.eventapp.R;

import java.util.List;

/**
 * Created by tina on 4/16/16.
 */
/*---------------------------------------------------------------------------
Class Name:                EventsFeedRVAdapter
Description:               Adapts information from an Array List about events
                           into the Recycler View
---------------------------------------------------------------------------*/
public class EventsFeedRVAdapter extends RecyclerView.Adapter<EventsFeedRVAdapter.EventViewHolder> {

    /*---------------------------------------------------------------------------
    Class Name:                EventViewHolder
    Description:               Holds all the views for the card for event feed
    ---------------------------------------------------------------------------*/
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

        /*---------------------------------------------------------------------------
        Function Name:                EventViewHolder
        Description:                  Initializes all views for this card
        Input:                        View itemView
        Output:                       None.
        ---------------------------------------------------------------------------*/
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

    // Holds all events
    List<Event> events;

    // Reference to this fragment
    Fragment currentFragment;

    // Reference to the tag for the current fragment
    String tagCurrentFragment;

    /*---------------------------------------------------------------------------
    Function Name:                EventsFeedRVAdapter
    Description:                  Contructor to initialize the instance variables
    Input:                        List<Event> events - holds events for feed
                                  Fragment currentFragment -  fragment that contains the feed
                                  String tagCurrentFragment - type of tab for this feed
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public EventsFeedRVAdapter(List<Event> events, Fragment currentFragment, String tagCurrentFragment){
        this.events = events;
        this.currentFragment = currentFragment;
        this.tagCurrentFragment = tagCurrentFragment;
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreateViewHolder()
    Description:                  Necessary method to override: Defines the layout
                                  and type of each view holder
    Input:                        ViewGroup viewGroup
                                  int viewType
    Output:                       int - number of cards/items
    ---------------------------------------------------------------------------*/
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // Inflates the view for the feed
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_event_feed, viewGroup, false);

        // Make the textviews into the custom font
        overrideFonts(v.getContext(),v);

        // Set the font for the folloiwng text views
        Typeface raleway_medium = Typeface.createFromAsset(viewGroup.getContext().getAssets(),  "Raleway-Medium.ttf");

        TextView name = (TextView) v.findViewById(R.id.event_name);
        name.setTypeface(raleway_medium);

        // Create the new event view holder to return
        EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }

    /*---------------------------------------------------------------------------
    Function Name:                onBindViewHolder()
    Description:                  Necessary method to override: Binds information
                                  to each view holder at position i
    Input:                        CategoryViewHolder categoryViewHolder
                                  int i - position of the item in the RecyclerView
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onBindViewHolder(final EventViewHolder eventViewHolder, final int i) {

        // If the event picture isn't null or an empty string
        if (events.get(i).getPicture() != null && events.get(i).getPicture() != "") {
            // Get the image file string
            String imageFile = events.get(i).getPicture();

            // Attempt the create a bitmpa from the imagefile
            Bitmap src = null;
            if (imageFile != null && imageFile != "") {
                try {
                    byte[] imageAsBytes = Base64.decode(imageFile, Base64.DEFAULT);
                    src = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                } catch (OutOfMemoryError e) {
                    System.err.println(e.toString());
                }
            }

            // If the bitmap was created successfully, update event picture
            if (src != null)
                eventViewHolder.eventPic.setImageBitmap(src);
            // If the bitmap was created unsuccessfully, don't update event picture view
            else
                eventViewHolder.eventPic.setImageResource(R.drawable.wineanddine);

        // If there is no event picture, update event picture view
        } else {
            eventViewHolder.eventPic.setImageResource(R.drawable.wineanddine);
        }

        // Update the following views with correct event information
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

        // Update the following views with correct event information
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

        // Set the event time with correct event information
        eventViewHolder.eventTime.setText(sub);

        // Categories
        String categories = "";
        if(events.get(i).getCategories() != null && events.get(i).getCategories().size() != 0) {
            for (int categoryIndex = 0; categoryIndex < events.get(i).getCategories().size() - 1; categoryIndex++) {
                categories += events.get(i).getCategories().get(categoryIndex) + ", ";
            }
            categories += events.get(i).getCategories().get(events.get(i).getCategories().size() - 1);
        }

        // Set the event categories with correct event information
        eventViewHolder.eventCategories.setText(categories);

        // Reference to position i
        final int test = i;

        // On click listener for the card
        eventViewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Pass the event object in a bundle to pass to Event Details Fragment
                Fragment eventDetailsFragment = new EventDetailsFragment();

                // Pass event details to event details fragment
                Bundle bundle = new Bundle();
                bundle.putSerializable(currentFragment.getString(R.string.event_details), events.get(test));
                eventDetailsFragment.setArguments(bundle);

                // Makes sure the fragment being hidden is the tabs fragment which includes each page fragment,
                // not just one of the view page fragments by specifying tab fragment tag if there is a tab fragment
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


                // Remove the search bar for event details
                ((MainActivity) currentFragment.getActivity()).removeSearchToolbar();


                // Add Event Details Fragment to fragment manager / show it to the viewer
                currentFragment.getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame_layout, eventDetailsFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(tabFragmentTag)
                        .commit();

            }
        });

    }

    /*---------------------------------------------------------------------------
    Function Name:                getItemCount()
    Description:                  Gets the number of items in recycler - the events list
    Input:                        None
    Output:                       int - number of items in recyclerview
    ---------------------------------------------------------------------------*/
    @Override
    public int getItemCount() {
        return events.size();
    }

    /*---------------------------------------------------------------------------
    Function Name:                overrideFonts()
    Description:                  Sets fonts for all TextViews
    Input:                        final Context context
                                  final View v
    Output:                       View to be inflated
    ---------------------------------------------------------------------------*/
    private void overrideFonts(final Context context, final View v) {
        try {

            // If the view is a ViewGroup
            if (v instanceof ViewGroup) {

                ViewGroup vg = (ViewGroup) v;

                // Iterate through ViewGroup children
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);

                    // Call method again for each child
                    overrideFonts(context, child);
                }

                // If the view is a TextView set the font
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "raleway-regular.ttf"));
            }

        }
        catch (Exception e) {
            // Print out error if one is encountered
            System.err.println(e.toString());
        }
    }

}
