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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.EventDetails.EventDetailsFragment;
import com.spuds.eventapp.Firebase.EventsFirebase;
import com.spuds.eventapp.Firebase.UserFirebase;
import com.spuds.eventapp.Profile.ProfileFragment;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.Shared.Notification;

import java.util.List;

/**
 * Created by tina on 5/13/16.
 */


public class NotificationsRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static String TYPE_REPLY = "Reply Notification";
    private final static String TYPE_INVITE = "Invite Notification";
    private final static String TYPE_UPDATE = "Update Notification";



    public static class UpdateViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        ImageView transparent;

        ImageView eventPicture;
        TextView dayDate;
        TextView monthDate;
        TextView timeDate;
        TextView host;
        TextView eventName;

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

    public static class InviteViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        ImageView transparent;

        ImageView hostPicture;
        TextView dayDate;
        TextView monthDate;
        TextView timeDate;
        TextView host;
        TextView eventName;

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

    public static class ReplyViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        ImageView transparent;

        ImageView replierPicture;
        TextView monthDate;
        TextView dayDate;
        TextView timeDate;

        TextView host;
        TextView eventName;
        TextView commentDescription;


        ReplyViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.cv);
            transparent = (ImageView) itemView.findViewById(R.id.transparent_overlay);

            replierPicture = (ImageView) itemView.findViewById(R.id.picture);
            eventName = (TextView) itemView.findViewById(R.id.event_name);
            host = (TextView) itemView.findViewById(R.id.host);
            timeDate = (TextView) itemView.findViewById(R.id.date_time);
            commentDescription = (TextView) itemView.findViewById(R.id.comment_description);
            monthDate = (TextView) itemView.findViewById(R.id.date_month);
            dayDate = (TextView) itemView.findViewById(R.id.date_day);
        }
    }



    List<Notification> notificationsList;
    Fragment currentFragment;

    public NotificationsRVAdapter(List<Notification> notificationsList, Fragment currentFragment) {

        this.notificationsList = notificationsList;
        this.currentFragment = currentFragment;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = null;

        switch(viewType) {
            case 0:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notifications_update, viewGroup, false);
                overrideFonts(v.getContext(),v);

                Typeface raleway_medium = Typeface.createFromAsset(viewGroup.getContext().getAssets(),  "Raleway-Medium.ttf");

                //title font
                TextView name = (TextView) v.findViewById(R.id.host);
                name.setTypeface(raleway_medium);

                TextView event = (TextView) v.findViewById(R.id.event_name);
                event.setTypeface(raleway_medium);

                return new UpdateViewHolder(v);
            case 1:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notifications_invite, viewGroup, false);
                overrideFonts(v.getContext(),v);

                Typeface medium = Typeface.createFromAsset(viewGroup.getContext().getAssets(),  "Raleway-Medium.ttf");

                //title font
                TextView host_name = (TextView) v.findViewById(R.id.host);
                host_name.setTypeface(medium);

                TextView host_event = (TextView) v.findViewById(R.id.event_name);
                host_event.setTypeface(medium);

                return new InviteViewHolder(v);
            case 2:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notifications_comment, viewGroup, false);
                overrideFonts(v.getContext(),v);

                Typeface med = Typeface.createFromAsset(viewGroup.getContext().getAssets(),  "Raleway-Medium.ttf");

                //title font
                TextView host = (TextView) v.findViewById(R.id.host);
                host.setTypeface(med);

                TextView thing = (TextView) v.findViewById(R.id.event_name);
                thing.setTypeface(med);

                return new ReplyViewHolder(v);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {

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
            case TYPE_REPLY:
                holderReply((NotificationsRVAdapter.ReplyViewHolder) holder, noti);
                break;
            default:
                break;

        }

    }

    void holderUpdate(NotificationsRVAdapter.UpdateViewHolder holder, final Notification noti) {

        holder.host.setText(noti.host);
        holder.eventName.setText(noti.eventName);

        holder.timeDate.setText(noti.time);
        holder.monthDate.setText(noti.month);
        holder.dayDate.setText(noti.day);
        String imageFile = noti.picture;

        if (imageFile != null && imageFile != "") {
            Bitmap src = null;
            try {
                byte[] imageAsBytes = Base64.decode(imageFile, Base64.DEFAULT);
                src = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            } catch (OutOfMemoryError e) {
                System.err.println(e.toString());
            }
            if (src != null) {
                holder.eventPicture.setImageResource(R.drawable.wineanddine);
            }
        } else {
            holder.eventPicture.setImageResource(R.drawable.wineanddine);
        }

        // Make card clickable
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the event object in a bundle to pass to Event Details Fragment

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while (!EventsFirebase.detailsThreadCheck) {
                            try {
                                //("sleepingthread","fam");

                                Thread.sleep(70);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        Fragment eventDetailsFragment = new EventDetailsFragment();

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

    void holderInvite(NotificationsRVAdapter.InviteViewHolder holder, final Notification noti) {
        holder.host.setText(noti.host);
        holder.eventName.setText(noti.eventName);

        holder.timeDate.setText(noti.time);
        holder.monthDate.setText(noti.month);
        holder.dayDate.setText(noti.day);

        if (noti.picture != null && noti.picture != "") {
            Bitmap src = null;
            try {
                byte[] imageAsBytes = Base64.decode(noti.picture, Base64.DEFAULT);
                src = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            } catch(OutOfMemoryError e) {
                System.err.println(e.toString());
            }
            if (src != null) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(currentFragment.getResources(), src);
                circularBitmapDrawable.setCircular(true);
                circularBitmapDrawable.setAntiAlias(true);
                holder.hostPicture.setImageDrawable(circularBitmapDrawable);
            } else {
                try {
                    src = BitmapFactory.decodeResource(currentFragment.getResources(), R.drawable.profile_pic_icon);

                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(currentFragment.getResources(), src);
                    circularBitmapDrawable.setCircular(true);
                    circularBitmapDrawable.setAntiAlias(true);
                    holder.hostPicture.setImageDrawable(circularBitmapDrawable);
                } catch (OutOfMemoryError e) {
                    System.err.println(e.toString());
                }
            }
        } else {

            try {
                Bitmap src = BitmapFactory.decodeResource(currentFragment.getResources(), R.drawable.profile_pic_icon);

                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(currentFragment.getResources(), src);
                circularBitmapDrawable.setCircular(true);
                circularBitmapDrawable.setAntiAlias(true);
                holder.hostPicture.setImageDrawable(circularBitmapDrawable);
            } catch (OutOfMemoryError e) {
                System.err.println(e.toString());
            }
        }

        // Make card clickable
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the event object in a bundle to pass to Event Details Fragment

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while (!EventsFirebase.detailsThreadCheck) {
                            try {
                                //("sleepingthread","fam");

                                Thread.sleep(70);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        Fragment eventDetailsFragment = new EventDetailsFragment();

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

    void holderReply(NotificationsRVAdapter.ReplyViewHolder holder, final Notification noti) {
        holder.host.setText(noti.host);
        holder.eventName.setText(noti.eventName);

        holder.timeDate.setText(noti.time);
        holder.monthDate.setText(noti.month);
        holder.dayDate.setText(noti.day);

        if (noti.picture != null && noti.picture != "") {
            Bitmap src = null;
            try {
                byte[] imageAsBytes = Base64.decode(noti.picture, Base64.DEFAULT);
                src = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            } catch(OutOfMemoryError e) {
                System.err.println(e.toString());
            }
            if (src != null) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(currentFragment.getResources(), src);
                circularBitmapDrawable.setCircular(true);
                circularBitmapDrawable.setAntiAlias(true);
                holder.replierPicture.setImageDrawable(circularBitmapDrawable);
            } else {
                try {
                     src = BitmapFactory.decodeResource(currentFragment.getResources(), R.drawable.profile_pic_icon);

                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(currentFragment.getResources(), src);
                    circularBitmapDrawable.setCircular(true);
                    circularBitmapDrawable.setAntiAlias(true);
                    holder.replierPicture.setImageDrawable(circularBitmapDrawable);
                } catch (OutOfMemoryError e) {
                    System.err.println(e.toString());
                }
            }
        } else {
            try {
                Bitmap src = BitmapFactory.decodeResource(currentFragment.getResources(), R.drawable.profile_pic_icon);

                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(currentFragment.getResources(), src);
                circularBitmapDrawable.setCircular(true);
                circularBitmapDrawable.setAntiAlias(true);
                holder.replierPicture.setImageDrawable(circularBitmapDrawable);
            } catch (OutOfMemoryError e) {
                System.err.println(e.toString());
            }
        }

        holder.commentDescription.setText(noti.commentDescription);

        // Make card clickable
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the event object in a bundle to pass to Event Details Fragment

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while (!EventsFirebase.detailsThreadCheck) {
                            try {
                                //("sleepingthread","fam");

                                Thread.sleep(70);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        Fragment eventDetailsFragment = new EventDetailsFragment();

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

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    private void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView) {
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
