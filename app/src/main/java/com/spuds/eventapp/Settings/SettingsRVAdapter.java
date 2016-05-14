package com.spuds.eventapp.Settings;

import android.media.audiofx.BassBoost;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    public static class SettingsViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        ImageView settingPhoto;
        TextView settingName;
        ImageView toggleNotifications;

        public SettingsViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            settingPhoto = (ImageView)itemView.findViewById(R.id.settings_photo);
            settingName = (TextView)itemView.findViewById(R.id.settings_name);
            toggleNotifications = (ImageView)itemView.findViewById(R.id.settings_toggle);

        }
    }


    public SettingsRVAdapter(List<Setting> settings){
        this.settings = settings;
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

    }

}
