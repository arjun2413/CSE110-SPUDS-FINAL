package com.spuds.eventapp.CategoriesList;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.spuds.eventapp.FilteredCategoryFeed.CategoryFeedFragment;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;

/*---------------------------------------------------------------------------
Class Name:                CategoriesListFragment
Description:               Contains information Categories List
---------------------------------------------------------------------------*/
public class CategoriesListFragment extends Fragment {
    public String categoryType;

    /*---------------------------------------------------------------------------
    Function Name:                CategoriesListFragment
    Description:                  Required default no-argument constructor
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public CategoriesListFragment() {
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

        // Set up layout to be inflated for categories list
        View view = inflater.inflate(R.layout.fragment_categories_list, container, false);

        // Set fonts for each textview
        overrideFonts(view.getContext(),view);

        // Setup title name
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Categories");

        // Setup picking categories
        pickCategory(view);
        return view;
    }

    /*---------------------------------------------------------------------------
    Function Name:                pickCategory()
    Description:                  Helper method to setup each category's
                                  onClickListener's method
    Input:                        View view - view to be inflated
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public void pickCategory(View view) {
        academicButton(view);
        sportsButton(view);
        campusButton(view);
        concertsButton(view);
        foodButton(view);
        freeButton(view);
        socialButton(view);
        sportsButton(view);
    }

    /*---------------------------------------------------------------------------
    Function Name:                academicButton()
    Description:                  Setup onClickListener for button and set
                                  the categoryType
    Input:                        View view
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public void academicButton(View view) {

        final Button academicButton = (Button) view.findViewById(R.id.academic);

        // If the button exists
        if (academicButton != null) {

            // When the button is clicked, set category type and show the
            // corresponding feed
            academicButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    categoryType = getString(R.string.cat_academic);

                    startFeed();
                }
            });
        }
    }

    /*---------------------------------------------------------------------------
    Function Name:                sportsButton()
    Description:                  Setup onClickListener for button and set
                                  the categoryType
    Input:                        View view
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public void sportsButton(View view) {

        final Button sportsButton = (Button) view.findViewById(R.id.sports);

        if (sportsButton != null) {

            // When the button is clicked, set category type and show the
            // corresponding feed
            sportsButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    categoryType = getString(R.string.cat_sports);

                    startFeed();
                }
            });
        }
    }

    /*---------------------------------------------------------------------------
    Function Name:                socialButton()
    Description:                  Setup onClickListener for button and set
                                  the categoryType
    Input:                        View view
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public void socialButton(View view) {

        final Button socialButton = (Button) view.findViewById(R.id.social);

        if (socialButton != null) {

            // When the button is clicked, set category type and show the
            // corresponding feed
            socialButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    categoryType = getString(R.string.cat_social);


                    startFeed();
                }
            });
        }
    }

    /*---------------------------------------------------------------------------
    Function Name:                freeButton()
    Description:                  Setup onClickListener for button and set
                                  the categoryType
    Input:                        View view
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public void freeButton(View view) {

        final Button freeButton = (Button) view.findViewById(R.id.free);

        if (freeButton != null) {

            // When the button is clicked, set category type and show the
            // corresponding feed
            freeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    categoryType = getString(R.string.cat_free);

                    startFeed();
                }
            });
        }
    }

    /*---------------------------------------------------------------------------
    Function Name:                foodButton()
    Description:                  Setup onClickListener for button and set
                                  the categoryType
    Input:                        View view
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public void foodButton(View view) {

        final Button foodButton = (Button) view.findViewById(R.id.food);

        if (foodButton != null) {

            // When the button is clicked, set category type and show the
            // corresponding feed
            foodButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    categoryType = getString(R.string.cat_food);

                    startFeed();
                }
            });
        }
    }

    /*---------------------------------------------------------------------------
    Function Name:                concertsButton()
    Description:                  Setup onClickListener for button and set
                                  the categoryType
    Input:                        View view
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public void concertsButton(View view) {

        final Button concertsButton = (Button) view.findViewById(R.id.concerts);

        if (concertsButton != null) {

            // When the button is clicked, set category type and show the
            // corresponding feed
            concertsButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    categoryType = getString(R.string.cat_concerts);

                    startFeed();
                }
            });
        }
    }

    /*---------------------------------------------------------------------------
    Function Name:                campusButton()
    Description:                  Setup onClickListener for button and set
                                  the categoryType
    Input:                        View view
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public void campusButton(View view) {

        final Button campusButton = (Button) view.findViewById(R.id.campus_orgs);

        if (campusButton != null) {

            // When the button is clicked, set category type and show the
            // corresponding feed
            campusButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    categoryType = getString(R.string.cat_student_orgs);

                    startFeed();
                }
            });
        }
    }

    /*---------------------------------------------------------------------------
    Function Name:                startFeed()
    Description:                  Starts the CategoryFeedFragment for specified
                                  category type
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    void startFeed() {

        // Bundle with category type to be passed into fragment
        Bundle category = new Bundle();
        category.putString(getString(R.string.category_bundle), categoryType);

        // Creates CategoryFeedFragment passing in bundle
        CategoryFeedFragment categoryFeedTab = new CategoryFeedFragment();
        categoryFeedTab.setArguments(category);


        // Start the transaction to switch the view to the CategoryFeedFragment
        ((MainActivity) getActivity()).searchType = categoryType;
        getFragmentManager().beginTransaction()
                .show(categoryFeedTab)
                .replace(R.id.fragment_frame_layout, categoryFeedTab)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(getString(R.string.fragment_category_feed))
                .commit();
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
