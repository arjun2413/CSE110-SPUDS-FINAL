package com.spuds.eventapp.About;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;

/*---------------------------------------------------------------------------
Class Name:                About Fragment
Description:               Fragment that contains information about software
                           developers
---------------------------------------------------------------------------*/
public class AboutFragment extends Fragment {

    /*---------------------------------------------------------------------------
    Function Name:                AboutFragment
    Description:                  Required default no-argument constructor
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public AboutFragment() {
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreate()
    Description:                  Called each time fragment is created
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
        // Inflates layout
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        // Setup fonts for each textview
        overrideFonts(view.getContext(), view);

        // Setup title name
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("About");

        // Setup title font
        TextView title = (TextView) view.findViewById(R.id.meet);
        Typeface raleway_medium = Typeface.createFromAsset(getActivity().getAssets(),  "Raleway-Medium.ttf");
        title.setTypeface(raleway_medium);

        return view;
    }

    /*---------------------------------------------------------------------------
    Function Name:                overrideFonts()
    Description:                  Sets fonts for all TextViews
    Input:                        final Context context
                                  final View v
    Output:                       View to be inflated
    ---------------------------------------------------------------------------*/
    private void overrideFonts(final Context context, final View v) {
        try {

            // If the view is a ViewGroup
            if (v instanceof ViewGroup) {

                ViewGroup vg = (ViewGroup) v;

                // Iterate through ViewGroup children
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);

                    // Call method again for each child
                    overrideFonts(context, child);
                }

            // If the view is a TextView set the font
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "raleway-regular.ttf"));
            }

        }
        catch (Exception e) {
            // Print out error if one is encountered
            System.err.println(e.toString());
        }
    }

    /*---------------------------------------------------------------------------
    Function Name:                onResume()
    Description:                  Every time the About Fragment comes into view
                                  remove the search toolbar
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onResume() {
        super.onResume();

        // Call the remove search toolbar method in activity
        ((MainActivity)getActivity()).removeSearchToolbar();
    }
}
