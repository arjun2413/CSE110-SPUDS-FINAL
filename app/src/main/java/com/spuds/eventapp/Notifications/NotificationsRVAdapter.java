package com.spuds.eventapp.Notifications;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.EventDetails.EventDetailsFragment;
import com.spuds.eventapp.Firebase.EventsFirebase;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.Shared.Notification;

import java.util.List;

/**
 * Created by tina on 5/13/16.
 */

/*---------------------------------------------------------------------------
Class Name:                NotificationsRVAdapter
Description:               Contains information about NotificationsRVAdapter
---------------------------------------------------------------------------*/
public class NotificationsRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static String TYPE_REPLY = "Reply Notification";
    private final static String TYPE_INVITE = "Invite Notification";
    private final static String TYPE_UPDATE = "Update Notification";


  /*---------------------------------------------------------------------------
    Class Name:                UpdateViewHolder
    Description:               Holds all the elements necessary for an updated event
    ---------------------------------------------------------------------------*/
    public static class UpdateViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        ImageView transparent;
        ImageView eventPicture;
        TextView dayDate;
        TextView monthDate;
        TextView timeDate;
        TextView host;
        TextView eventName;

        /*---------------------------------------------------------------------------
        Function Name:                CreateEventCategoryRVAdapter
        Description:                  List<CategoryTextButton> categories
                                           - holds information for categories
                                      Fragment currentFragment
                                           - fragment RVAdapter instantiated from

        Input:                        List<CategoryTextButton> categories - contains
                                      information about categories for create event
                                      Fragment currentFragment - reference to fragment
                                      that created this object
        Output:                       None.
        ---------------------------------------------------------------------------*/
        UpdateViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.cv);
            transparent = (ImageView) itemView.findViewById(R.id.transparent_overlay);
            eventPicture = (ImageView) itemView.findViewById(R.id.picture);
            dayDate = (TextView) itemView.findViewById(R.id.date_day);
            monthDate = (TextView) itemView.findViewById(R.id.date_month);
            timeDate = (TextView) itemView.findViewById(R.id.date_time);
            host = (TextView) itemView.findViewById(R.id.host);
            eventName = (TextView) itemView.findViewById(R.id.event_name);
        }
    }

  /*---------------------------------------------------------------------------
    Class Name:                InviteViewHolder
    Description:               Holds all the elements necessary for an invite card
    ---------------------------------------------------------------------------*/
    public static class InviteViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        ImageView transparent;
        ImageView hostPicture;
        TextView dayDate;
        TextView monthDate;
        TextView timeDate;
        TextView host;
        TextView eventName;

        /*---------------------------------------------------------------------------
        Function Name:                InviteViewHolder
        Description:                  View holder containing the views
                                      for invite view holder card in the Recycler View
        Input:                        View itemView
        Output:                       None.
        ---------------------------------------------------------------------------*/
        InviteViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.cv);
            transparent = (ImageView) itemView.findViewById(R.id.transparent_overlay);

            hostPicture = (ImageView) itemView.findViewById(R.id.picture);
            dayDate = (TextView) itemView.findViewById(R.id.date_day);
            monthDate = (TextView) itemView.findViewById(R.id.date_month);
            timeDate = (TextView) itemView.findViewById(R.id.date_time);
            host = (TextView) itemView.findViewById(R.id.host);
            eventName = (TextView) itemView.findViewById(R.id.event_name);
        }
    }

    // Holds list of all notifications
    List<Notification> notificationsList;
    // Reference to fragment that instantiated this
    Fragment currentFragment;

    /*---------------------------------------------------------------------------
    Function Name:                NotificationsRVAdapter
    Description:                  Constructor
    Input:                        List<Notification> notificationsList: array list of
                                  notifications
                                  Fragment currentFragment: fragment RVAdapter instantiated.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public NotificationsRVAdapter(List<Notification> notificationsList, Fragment currentFragment) {

        this.notificationsList = notificationsList;
        this.currentFragment = currentFragment;

    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreateViewHolder()
    Description:                  Necessary method to override: Defines the layout
                                  and type of each view holder
    Input:                        ViewGroup viewGroup
                                  int viewType
    Output:                       SubViewHolder
    ---------------------------------------------------------------------------*/
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = null;

        // Based on the view type, create the corresponding view holder
        switch(viewType) {

            case 0:
                // Inflate the item for update notification
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notifications_update, viewGroup, false);

                // Update textviews with custom fonts
                overrideFonts(v.getContext(),v);
                Typeface raleway_medium = Typeface.createFromAsset(viewGroup.getContext().getAssets(),  "Raleway-Medium.ttf");

                TextView name = (TextView) v.findViewById(R.id.host);
                name.setTypeface(raleway_medium);

                TextView event = (TextView) v.findViewById(R.id.event_name);
                event.setTypeface(raleway_medium);

                // Create new update view holder for this card
                return new UpdateViewHolder(v);

            case 1:
                // Inflate the item for invite notification
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notifications_invite, viewGroup, false);

                // Update textviews with custom fonts
                overrideFonts(v.getContext(),v);

                Typeface medium = Typeface.createFromAsset(viewGroup.getContext().getAssets(),  "Raleway-Medium.ttf");

                TextView host_name = (TextView) v.findViewById(R.id.host);
                host_name.setTypeface(medium);

                TextView host_event = (TextView) v.findViewById(R.id.event_name);
                host_event.setTypeface(medium);

                // Create new invite view holder for this card
                return new InviteViewHolder(v);

            default:
                return null;
        }
    }

    /*---------------------------------------------------------------------------
    Function Name:                getItemViewType()
    Description:                  gets the type of View that will be created by 
                                  getView() for the specified item.
    Input:                        int position
    Output:                       int
    ---------------------------------------------------------------------------*/
    @Override
    public int getItemViewType(int position) {

        // Based on notification type, return corresponding view type
        switch (notificationsList.get(position).notificationType) {

            case TYPE_UPDATE:
                return 0;
            case TYPE_INVITE:
                return 1;
            case TYPE_REPLY:
                return 2;
            default:
                return 0;
        }
    }

   /*---------------------------------------------------------------------------
    Function Name:                onBindViewHolder()
    Description:                  Necessary method to override: Binds information
                                  to each view holder at position i
    Input:                        SubViewHolder subViewHolder
                                  int i: position of the item in the RecyclerView
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final Notification noti = notificationsList.get(position);

        switch (noti.notificationType) {

            case TYPE_UPDATE:
                holderUpdate((NotificationsRVAdapter.UpdateViewHolder) holder, noti);
                break;
            case TYPE_INVITE:
                holderInvite((NotificationsRVAdapter.InviteViewHolder) holder, noti);
                break;
            default:
                break;
        }
    }

    /*---------------------------------------------------------------------------
    Function Name:                holderUpdate()
    Description:
    Input:                        NotificationsRVAdapter.UpdateViewHolder holder:
                                  final Notification noti:
    Output:                       None.
    ---------------------------------------------------------------------------*/
    void holderUpdate(NotificationsRVAdapter.UpdateViewHolder holder, final Notification noti) {

        // Update the corresponding updated event information on the notifications screen
        holder.host.setText(noti.host);
        holder.eventName.setText(noti.eventName);

        holder.timeDate.setText(noti.time);
        holder.monthDate.setText(noti.month);
        holder.dayDate.setText(noti.day);
        String imageFile = noti.picture;

        // If the image file exists
        if (imageFile != null && imageFile != "") {

            // Try to get bitmap from the imagefile
            Bitmap src = null;
            try {
                byte[] imageAsBytes = Base64.decode(imageFile, Base64.DEFAULT);
                src = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            } catch (OutOfMemoryError e) {
                System.err.println(e.toString());
            }
            // If it was created successfully, set image bitmap
            if (src != null) {
                holder.eventPicture.setImageBitmap(src);
            }
        // If the image doesn't exist
        } else {
            // Set the event picture to default wine and dine drawable
            holder.eventPicture.setImageResource(R.drawable.wineanddine);
        }

        // Make card clickable
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // New thread to wait for event details form database has been received to do stuff
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // if the event details has not been received by the database yet, wait
                        while (!EventsFirebase.detailsThreadCheck) {
                            try {
                                Thread.sleep(70);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        Fragment eventDetailsFragment = new EventDetailsFragment();

                        // Pass the event object in a bundle to pass to Event Details Fragment
                        Bundle bundle = new Bundle();

                        bundle.putSerializable(currentFragment.getString(R.string.event_details), EventsFirebase.eventDetailsEvent);
                        eventDetailsFragment.setArguments(bundle);

                        ((MainActivity) currentFragment.getActivity()).removeSearchToolbar();

                        // Add Event Details Fragment to fragment manager
                        currentFragment.getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_frame_layout, eventDetailsFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .addToBackStack(currentFragment.getString(R.string.event_details_fragment))
                                .commit();

                    }
                }).start();



            }
        });

    }


    /*---------------------------------------------------------------------------
    Function Name:                holderInvite()
    Description:
    Input:                        NotificationsRVAdapter.InviteViewHolder holder:
                                  final Notification noti:
    Output:                       None.
    ---------------------------------------------------------------------------*/
    void holderInvite(NotificationsRVAdapter.InviteViewHolder holder, final Notification noti) {
        holder.host.setText(noti.host);
        holder.eventName.setText(noti.eventName);

        holder.timeDate.setText(noti.time);
        holder.monthDate.setText(noti.month);
        holder.dayDate.setText(noti.day);

        // If the picture exists
        if (noti.picture != null && noti.picture != "") {

            //Try changing the picture into a bitmap
            Bitmap src = null;
            try {
                byte[] imageAsBytes = Base64.decode(noti.picture, Base64.DEFAULT);
                src = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            } catch(OutOfMemoryError e) {
                System.err.println(e.toString());
            }

            // If the bitmap was created successfully
            if (src != null) {

                // Change the bitmap to a circle
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(currentFragment.getResources(), src);
                circularBitmapDrawable.setCircular(true);
                circularBitmapDrawable.setAntiAlias(true);

                // Load the circle image to the picture view for the user
                holder.hostPicture.setImageDrawable(circularBitmapDrawable);
            // If the bitmap was not created successfully
            } else {
                try {
                    // Create a bitmap from default profile pic icon
                    src = BitmapFactory.decodeResource(currentFragment.getResources(), R.drawable.profile_pic_icon);

                    // Change the bitmap to a circle
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(currentFragment.getResources(), src);
                    circularBitmapDrawable.setCircular(true);
                    circularBitmapDrawable.setAntiAlias(true);

                    // Load the stock profile pic icon circle image to the user picture view for the user
                    holder.hostPicture.setImageDrawable(circularBitmapDrawable);
                } catch (OutOfMemoryError e) {
                    System.err.println(e.toString());
                }
            }
        // If the user does not have a profile picture
        } else {

            try {
                // Get bitmap from stock profile pic
                Bitmap src = BitmapFactory.decodeResource(currentFragment.getResources(), R.drawable.profile_pic_icon);

                // Change the bitmap to a circle
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(currentFragment.getResources(), src);
                circularBitmapDrawable.setCircular(true);
                circularBitmapDrawable.setAntiAlias(true);

                // Load the stock profile pic icon circle image to the user picture view for the user
                holder.hostPicture.setImageDrawable(circularBitmapDrawable);
            } catch (OutOfMemoryError e) {
                System.err.println(e.toString());
            }
        }

        // Make card clickable
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // New thread to wait for event details form database has been received to do stuff
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // if the event details has not been received by the database yet, wait
                        while (!EventsFirebase.detailsThreadCheck) {
                            try {
                                Thread.sleep(70);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        // New event details fragment
                        Fragment eventDetailsFragment = new EventDetailsFragment();

                        // Pass the event object in a bundle to pass to Event Details Fragment
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(currentFragment.getString(R.string.event_details), EventsFirebase.eventDetailsEvent);
                        eventDetailsFragment.setArguments(bundle);

                        // Show the event details fragment/add it to fragment manager
                        ((MainActivity) currentFragment.getActivity()).removeSearchToolbar();
                        // Add Event Details Fragment to fragment manager
                        currentFragment.getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_frame_layout, eventDetailsFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .addToBackStack(currentFragment.getString(R.string.event_details_fragment))
                                .commit();

                    }
                }).start();



            }
        });

    }

    /*---------------------------------------------------------------------------
    Function Name:                getItemCount()
    Description:                  Necessary method to override: How many items
                                  in the RecyclerView
    Input:                        None
    Output:                       int: number of cards/items
    ---------------------------------------------------------------------------*/
    @Override
    public int getItemCount() {
        return notificationsList.size();
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
