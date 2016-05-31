package com.spuds.eventapp.Settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.spuds.eventapp.ChangePassword.ChangePasswordFragment;
import com.spuds.eventapp.Firebase.UserFirebase;
import com.spuds.eventapp.Login.LoginActivity;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.Shared.Setting;

import java.util.List;

/**
 * Created by David on 5/12/16.
 */
public class SettingsRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    // notifications are on by default
    boolean isNotificationOn;
    List<Setting> settings;
    Fragment currentFragment;

    public static class SettingsViewHolder extends RecyclerView.ViewHolder{
        CardView card;
        ImageView settingPhoto;
        TextView settingName;

        public SettingsViewHolder(View itemView) {
            super(itemView);
            card = (CardView)itemView.findViewById(R.id.cv);
            settingPhoto = (ImageView)itemView.findViewById(R.id.settings_photo);
            settingName = (TextView)itemView.findViewById(R.id.settings_name);

        }

    }

    public static class NotifSettingsViewHolder extends RecyclerView.ViewHolder{
        CardView card;
        ImageView settingPhoto;
        TextView settingName;
        Switch toggleNotifications;

        public NotifSettingsViewHolder(View itemView) {
            super(itemView);
            card = (CardView)itemView.findViewById(R.id.cv);
            settingPhoto = (ImageView)itemView.findViewById(R.id.settings_photo);
            settingName = (TextView)itemView.findViewById(R.id.settings_name);
            toggleNotifications = (Switch)itemView.findViewById(R.id.settings_toggle);
        }
    }
    public SettingsRVAdapter(List<Setting> settings, Fragment currentFragment, boolean isNotificationOn){
        this.settings = settings;
        this.currentFragment = currentFragment;
        this.isNotificationOn = isNotificationOn;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        switch(viewType){
            case 1:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_settings_notif, viewGroup, false);
                overrideFonts(v.getContext(),v);
                return new NotifSettingsViewHolder(v);

            default:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_settings, viewGroup, false);
                overrideFonts(v.getContext(),v);
                return new SettingsViewHolder(v);
        }

    }


    @Override
    public int getItemViewType(int position) {

        switch (position) {

            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 0;

        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (i == 1){
            ((NotifSettingsViewHolder) viewHolder).settingName.setText(settings.get(i).name);
            ((NotifSettingsViewHolder) viewHolder).settingPhoto.setImageResource(settings.get(i).photoId);
            ((NotifSettingsViewHolder) viewHolder).toggleNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    UserFirebase.updateNotificationToggle(isChecked);
                }
            });

            if (isNotificationOn)
                ((NotifSettingsViewHolder) viewHolder).toggleNotifications.setChecked(true);
            else
                ((NotifSettingsViewHolder) viewHolder).toggleNotifications.setChecked(false);
        }

        else{
            ((SettingsViewHolder) viewHolder).settingName.setText(settings.get(i).name);
            ((SettingsViewHolder) viewHolder).settingPhoto.setImageResource(settings.get(i).photoId);

        }


        switch (i) {
            case 0:
                ((SettingsViewHolder) viewHolder).card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();

                        ((MainActivity) currentFragment.getActivity()).removeSearchToolbar();
                        // Add Change Password Fragment to fragment manager
                        currentFragment.getFragmentManager().beginTransaction()
                                .show(changePasswordFragment)
                                .replace(R.id.fragment_frame_layout, changePasswordFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .addToBackStack(currentFragment.getString(R.string.fragment_change_password))
                                .commit();
                    }
                });
                break;
            case 2:
                ((SettingsViewHolder) viewHolder).card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(currentFragment.getActivity())
                                .setTitle("Delete Account")
                                .setMessage("Are you sure you want to delete your account?")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO (M): Delete account
                                        currentFragment.startActivity(new Intent(currentFragment.getActivity(), LoginActivity.class));
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                //.setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
                break;
        }

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
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Medium.ttf"));
            }
        }
        catch (Exception e) {
        }
    }

}
