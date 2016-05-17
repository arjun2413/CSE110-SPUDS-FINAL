package com.spuds.eventapp.Settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.spuds.eventapp.CreateEvent.CreateEventFragment;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.Shared.Setting;

import java.util.ArrayList;
import java.util.List;


public class SettingsFragment extends Fragment {
    public SettingsRVAdapter adapter;
    private List<Setting> settings;

    public SettingsFragment() {
        // required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycler, container, false);
        RecyclerView rv=(RecyclerView) v.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        rv.setLayoutManager(llm);

        settings = new ArrayList<>();
        settings.add(new Setting("Change Password", R.drawable.setting_pass));
        settings.add(new Setting("Notifications", R.drawable.notifications));
        settings.add(new Setting("Delete Account", R.drawable.delete));


        adapter = new SettingsRVAdapter(settings, this);
        rv.setAdapter(adapter);

        return v;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_create_event) {
            CreateEventFragment createEventFragment = new CreateEventFragment();

            ((MainActivity)getActivity()).removeSearchToolbar();
            // Add Event Details Fragment to fragment manager
            this.getFragmentManager().beginTransaction()
                    .show(createEventFragment)
                    .replace(R.id.fragment_frame_layout, createEventFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(getString(R.string.fragment_create_event))
                    .commit();
        }

        return true;
    }
}
