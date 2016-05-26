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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.EventDetails.EventDetailsFragment;
import com.spuds.eventapp.Firebase.UserFirebase;
import com.spuds.eventapp.Profile.ProfileFragment;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.Shared.Notification;

import java.util.List;

/**
 * Created by tina on 5/13/16.
 */


public class NotificationsRVAdapter extends RecyclerView.Adapter<NotificationsRVAdapter.NotificationViewHolder> {

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        ImageView picture;
        ImageView transparent;
        TextView host;
        TextView actionText;
        TextView eventName;
        TextView time;

        TextView commentDescription;

        TextView monthDate;
        TextView dayDate;
        TextView in;

        NotificationViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.cv);
            picture = (ImageView) itemView.findViewById(R.id.picture);
            transparent = (ImageView) itemView.findViewById(R.id.transparent_overlay);
            eventName = (TextView) itemView.findViewById(R.id.event_name);
            host = (TextView) itemView.findViewById(R.id.host);
            actionText = (TextView) itemView.findViewById(R.id.action_text);
            time = (TextView) itemView.findViewById(R.id.date_time);
            commentDescription = (TextView) itemView.findViewById(R.id.comment_description);
            monthDate = (TextView) itemView.findViewById(R.id.date_month);
            dayDate = (TextView) itemView.findViewById(R.id.date_day);
            in = (TextView) itemView.findViewById(R.id.in_text);
        }
    }

    private final static String TYPE_REPLY = "Reply Notification";
    private final static String TYPE_INVITE = "Invite Notification";
    private final static String TYPE_UPDATE = "Update Notification";

    List<Notification> notificationsList;
    Fragment currentFragment;

    public NotificationsRVAdapter(List<Notification> notificationsList, Fragment currentFragment) {

        this.notificationsList = notificationsList;
        this.currentFragment = currentFragment;

    }
    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notifications_comment, viewGroup, false);
        overrideFonts(v.getContext(),v);

        NotificationViewHolder nvh = new NotificationViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {

        final Notification noti = notificationsList.get(position);

        holder.host.setText(noti.host);
        holder.host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UserFirebase userFirebase = new UserFirebase();
                userFirebase.getAnotherUser(noti.userId);

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while (!userFirebase.threadCheckAnotherUser) {
                            try {
                                Thread.sleep(77);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }


                        startProfileFragment(userFirebase);

                    }
                }).start();
            }
        });

        holder.actionText.setText(noti.actionText);
        holder.eventName.setText(noti.eventName);



        // TODO (C): Time from date
        holder.time.setText(noti.time);
        holder.monthDate.setText(noti.month);
        holder.dayDate.setText(noti.day);


        if (noti.notificationType.equals(TYPE_INVITE) ||
                noti.notificationType.equals(TYPE_REPLY)) {

            // TODO (M): Picasso for image using noti.picFileName
            //holder.picture.setImageResource(R.drawable.arjun);

            Bitmap src = BitmapFactory.decodeResource(currentFragment.getResources(), R.drawable.arjun);
            RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(currentFragment.getResources(), src);
            dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
            holder.picture.setImageDrawable(dr);

            holder.picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final UserFirebase userFirebase = new UserFirebase();
                    userFirebase.getAnotherUser(noti.userId);

                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            while (!userFirebase.threadCheckAnotherUser) {
                                try {
                                    Thread.sleep(77);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }

                            startProfileFragment(userFirebase);

                        }
                    }).start();
                }
            });

        }

        holder.commentDescription.setText(noti.commentDescription);

        if (noti.notificationType.equals(TYPE_INVITE) ||
                noti.notificationType.equals(TYPE_UPDATE)) {
            ((ViewManager)holder.commentDescription.getParent()).removeView(holder.commentDescription);
            ((ViewManager)holder.in.getParent()).removeView(holder.in);
            ((ViewManager)holder.transparent.getParent()).removeView(holder.transparent);
        }


        // Make card clickable
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the event object in a bundle to pass to Event Details Fragment
                Fragment eventDetailsFragment = new EventDetailsFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable(currentFragment.getString(R.string.event_id), noti.eventId);
                bundle.putString(currentFragment.getString(R.string.event_details), null);
                eventDetailsFragment.setArguments(bundle);

                ((MainActivity) currentFragment.getActivity()).removeSearchToolbar();
                // Add Event Details Fragment to fragment manager
                currentFragment.getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame_layout, eventDetailsFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(currentFragment.getString(R.string.event_details_fragment))
                        .commit();
            }
        });

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


}
