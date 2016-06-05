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

/*---------------------------------------------------------------------------
Class Name:                SettingsRVAdapter
Description:               Contains information about the Settings page
---------------------------------------------------------------------------*/
public class SettingsRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    //notifications are on by default
    boolean isNotificationOn;
    //list of settings cards
    List<Setting> settings;
    //the current fragment
    Fragment currentFragment;


    /*---------------------------------------------------------------------------
    Class Name:                SettingsViewHolder
    Description:               Holds all the elements necessary for a settings card
    ---------------------------------------------------------------------------*/
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

    /*---------------------------------------------------------------------------
    Class Name:                SettingsViewHolder
    Description:               Holds all the elements necessary for the 
                               notifications toggle settings card
    ---------------------------------------------------------------------------*/
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

    /*---------------------------------------------------------------------------
    Function Name:                SettingsRVAdapter
    Description:                  Constructor
    Input:                        List<User> settings: array list of settings
                                  Fragment currentFragment
                                  boolean isNotificationOn: determines if notifcations
                                  are toggled on 
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public SettingsRVAdapter(List<Setting> settings, Fragment currentFragment, boolean isNotificationOn){
        this.settings = settings;
        this.currentFragment = currentFragment;
        this.isNotificationOn = isNotificationOn;
    }

    /*---------------------------------------------------------------------------
    Function Name:                getItemCount()
    Description:                  Necessary method to override: How many items
                                  in the RecyclerView. Should always be 2 for now
    Input:                        None
    Output:                       int: number of cards/items
    ---------------------------------------------------------------------------*/
    @Override
    public int getItemCount() {
        return 1;
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreateViewHolder()
    Description:                  Necessary method to override: Defines the layout
                                  and type of each view holder
    Input:                        ViewGroup viewGroup
                                  int viewType
    Output:                       InviteViewHolder
    ---------------------------------------------------------------------------*/
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_settings, viewGroup, false);
                overrideFonts(v.getContext(),v);
                return new SettingsViewHolder(v);


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

        return 0;


    }

    /*---------------------------------------------------------------------------
    Function Name:                onBindViewHolder()
    Description:                  Necessary method to override: Binds information
                                  to each view holder at position i
    Input:                        InviteViewHolder holder
                                  int position: position of the item in the RecyclerView
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        //i == 1, so the second settings card, which is notification toggle
      /*  if (i == 1){
            //set the fields
            ((NotifSettingsViewHolder) viewHolder).settingName.setText(settings.get(i).name);
            ((NotifSettingsViewHolder) viewHolder).settingPhoto.setImageResource(settings.get(i).photoId);
            ((NotifSettingsViewHolder) viewHolder).toggleNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    UserFirebase.updateNotificationToggle(isChecked);
                }
            });
            //toggle notification 
            if (isNotificationOn)
                ((NotifSettingsViewHolder) viewHolder).toggleNotifications.setChecked(true);
            else
                ((NotifSettingsViewHolder) viewHolder).toggleNotifications.setChecked(false);
        }
*/
  //      else{
            ((SettingsViewHolder) viewHolder).settingName.setText(settings.get(i).name);
            ((SettingsViewHolder) viewHolder).settingPhoto.setImageResource(settings.get(i).photoId);

    //    }*/


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
        }

    }

    /*---------------------------------------------------------------------------
    Function Name:                overrideFonts()
    Description:                  used to override fonts
    Input:                        Context context: the context we care about
                                  View v: the view we care about 
    Output:                       None.
    ---------------------------------------------------------------------------*/
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
