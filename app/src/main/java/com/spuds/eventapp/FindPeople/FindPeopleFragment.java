package com.spuds.eventapp.FindPeople;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.spuds.eventapp.CreateEvent.CreateEventFragment;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;


public class FindPeopleFragment extends Fragment {

    public FindPeopleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find_people, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
