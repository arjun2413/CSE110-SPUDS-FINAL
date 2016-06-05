package com.spuds.eventapp.Settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spuds.eventapp.Firebase.UserFirebase;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.Shared.Setting;

import java.util.ArrayList;
import java.util.List;

/*---------------------------------------------------------------------------
Class Name:                SettingsFragment
Description:               Contains information about the Settings page
---------------------------------------------------------------------------*/
public class SettingsFragment extends Fragment {
    public SettingsRVAdapter adapter;
    private List<Setting> settings;

    /*---------------------------------------------------------------------------
    Function Name:                SettingsFragment
    Description:                  Required default no-argument constructor
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public SettingsFragment() {
        // required empty constructor
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreate
    Description:                  called when the fragment is created
    Input:                        Bundle savedInstanceState
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreateView()
    Description:                  Inflates View layout and sets fonts programmatically
    Input:                        LayoutInflater inflater - inflates layout
                                  ViewGroup container - parent view group
                                  Bundle savedInstanceState
    Output:                       View to be inflated
    ---------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //initialize the view
        View v = inflater.inflate(R.layout.recycler, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Settings");
        RecyclerView rv=(RecyclerView) v.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        rv.setLayoutManager(llm);

        //initialize array list
        settings = new ArrayList<>();

        //add the two cards to the settings page
        settings.add(new Setting("Change Password", R.drawable.setting_pass));
        settings.add(new Setting("Notifications", R.drawable.notifications));
        //settings.add(new Setting("Delete Account", R.drawable.delete));


        adapter = new SettingsRVAdapter(settings, this, UserFirebase.thisUser.getNotificationToggle());
        rv.setAdapter(adapter);

        return v;
    }

    /*---------------------------------------------------------------------------
    Function Name:                onResume()
    Description:                  called every time the fragment is resumed
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).removeSearchToolbar();
    }

}
