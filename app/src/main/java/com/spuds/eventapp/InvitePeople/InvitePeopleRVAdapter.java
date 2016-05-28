package com.spuds.eventapp.InvitePeople;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.User;

import java.util.ArrayList;

/**
 * Created by tina on 5/16/16.
 */
public class InvitePeopleRVAdapter extends RecyclerView.Adapter<InvitePeopleRVAdapter.InviteViewHolder> {

    public static class InviteViewHolder extends RecyclerView.ViewHolder {

        ImageView photo;
        TextView followerName;
        Button inviteButton;
        CardView card;

        public InviteViewHolder(View view) {
            super(view);

            card = (CardView) view.findViewById(R.id.cv);
            photo = (ImageView) view.findViewById(R.id.invite_user_image);
            inviteButton = (Button) view.findViewById(R.id.invite_toggle);
            followerName = (TextView) view.findViewById(R.id.invite_user_name);

        }
    }

    ArrayList<User> followers;
    ArrayList<User> invited;
    Fragment fragment;
    boolean selectAll;

    public InvitePeopleRVAdapter(ArrayList<User> followers, ArrayList<User> invited, Fragment fragment) {
        this.followers = followers;
        this.invited = invited;
        this.fragment = fragment;
    }

    @Override
    public InviteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite_people, parent, false);
        overrideFonts(v.getContext(),v);
        InviteViewHolder ivh = new InviteViewHolder(v);
        return ivh;
    }

    boolean b;
    @Override
    public void onBindViewHolder(final InviteViewHolder holder, int position) {
        final int i = position;

        String imageFile = followers.get(i).getPicture();
        if (imageFile != "" && imageFile != null) {
            Bitmap src = null;
            try {
                byte[] imageAsBytes = Base64.decode(imageFile, Base64.DEFAULT);
                src = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            } catch (OutOfMemoryError e) {
                System.err.println(e.toString());
            }
            if (src != null) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(fragment.getResources(), src);
                circularBitmapDrawable.setCircular(true);
                circularBitmapDrawable.setAntiAlias(true);
                holder.photo.setImageDrawable(circularBitmapDrawable);
            } else {
                src = BitmapFactory.decodeResource(fragment.getResources(), R.drawable.profile_pic_icon);

                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(fragment.getResources(), src);
                circularBitmapDrawable.setCircular(true);
                circularBitmapDrawable.setAntiAlias(true);
                holder.photo.setImageDrawable(circularBitmapDrawable);
            }
        } else {
            Bitmap src = BitmapFactory.decodeResource(fragment.getResources(), R.drawable.profile_pic_icon);

            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(fragment.getResources(), src);
            circularBitmapDrawable.setCircular(true);
            circularBitmapDrawable.setAntiAlias(true);
            holder.photo.setImageDrawable(circularBitmapDrawable);
        }
        holder.inviteButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                // TODO: colors
                if (!invited.contains(followers.get(i))) {

                    holder.inviteButton.setBackgroundTintList(fragment.getResources().getColorStateList(R.color.color_selected));
                    invited.add(followers.get(i));

                } else {

                    holder.inviteButton.setBackgroundTintList(fragment.getResources().getColorStateList(R.color.color_unselected));
                    invited.remove(followers.get(i));

                }

            }
        });


        holder.followerName.setText(followers.get(position).getName());

        // TODO: colors
        /*
        //What is selectAll? How do you select all?
        if (selectAll)
            holder.scb.setChecked(true, true);
        else
            holder.scb.setChecked(false, true);
        */
    }

    @Override
    public int getItemCount() {
        return followers.size();
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
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Medium.ttf"));
            }
        }
        catch (Exception e) {
        }
    }
}
