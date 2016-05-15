package com.spuds.eventapp.Settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.ChangePassword.ChangePasswordFragment;
import com.spuds.eventapp.Login.LoginActivity;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.Setting;

import java.util.List;

/**
 * Created by David on 5/12/16.
 */
public class SettingsRVAdapter extends RecyclerView.Adapter<SettingsRVAdapter.SettingsViewHolder>{

    // notifications are on by default
    boolean isNotification = true;
    List<Setting> settings;
    Fragment currentFragment;

    public static class SettingsViewHolder extends RecyclerView.ViewHolder{
        CardView card;
        ImageView settingPhoto;
        TextView settingName;
        ImageView toggleNotifications;

        public SettingsViewHolder(View itemView) {
            super(itemView);
            card = (CardView)itemView.findViewById(R.id.cv);
            settingPhoto = (ImageView)itemView.findViewById(R.id.settings_photo);
            settingName = (TextView)itemView.findViewById(R.id.settings_name);
            toggleNotifications = (ImageView)itemView.findViewById(R.id.settings_toggle);

        }
    }


    public SettingsRVAdapter(List<Setting> settings, Fragment currentFragment){
        this.settings = settings;
        this.currentFragment = currentFragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public SettingsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_settings, viewGroup, false);
        SettingsViewHolder svh = new SettingsViewHolder(v);
        return svh;


    }



    @Override
    public void onBindViewHolder(SettingsViewHolder settingsViewHolder, int i) {
        settingsViewHolder.settingName.setText(settings.get(i).name);
        settingsViewHolder.settingPhoto.setImageResource(settings.get(i).photoId);

        settingsViewHolder.toggleNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // notifications are on, toggle off
                if (isNotification){
                    isNotification = false;
                }
                else{
                    // notifications are off, toggle on
                    isNotification = true;

                }
            }
        });

        switch (i) {
            case 0:
                settingsViewHolder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();

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
                settingsViewHolder.card.setOnClickListener(new View.OnClickListener() {
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

}
