package com.spuds.eventapp.CreateEvent;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.CategoryTextButton;

import java.util.ArrayList;
import java.util.List;

import cn.refactor.library.SmoothCheckBox;
/*---------------------------------------------------------------------------
Class Name:                CreateEventCategoryRVAdapter
Description:               Adapts information from an Array List  about categories
                           into the Recycler View
---------------------------------------------------------------------------*/
public class CreateEventCategoryRVAdapter extends RecyclerView.Adapter<CreateEventCategoryRVAdapter.CategoryViewHolder> {
    // Holds categories clicked by user
    public ArrayList<String> categoryList = new ArrayList<String>(3);
    // Fragment that contains Recycler View
    public Fragment currentFragment;
    // Holds information to be put onto RecyclerView
    List<CategoryTextButton> categories;
    // Makes sure categories clicked has a maximum of three
    int counterMaxThree = 0;

    /*---------------------------------------------------------------------------
    Function Name:                CreateEventCategoryRVAdapter
    Description:                  List<CategoryTextButton> categories
                                        - holds information for categories
                                  Fragment currentFragment
                                        - fragment RVAdapter instantiated from

    Input:                        List<CategoryTextButton> categories - contains
                                  information about categories for create event
                                  Fragment currentFragment - reference to fragment
                                  that created this object
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public CreateEventCategoryRVAdapter(List<CategoryTextButton> categories, Fragment currentFragment){
        this.categories = categories;
        this.currentFragment = currentFragment;
    }

    /*---------------------------------------------------------------------------
    Class Name:                CategoryViewHolder
    Description:               Holds all the elements a part of a category needed
                               create event.
    ---------------------------------------------------------------------------*/
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView text;
        SmoothCheckBox scb;

        /*---------------------------------------------------------------------------
        Function Name:                CategoryViewHolder
        Description:                  View holder containing the views
                                      for each card in the Recycler View
        Input:                        View itemView
        Output:                       None.
        ---------------------------------------------------------------------------*/
        CategoryViewHolder(View itemView) {
            super(itemView);
            card = (CardView)itemView.findViewById(R.id.cv);
            text = (TextView)itemView.findViewById(R.id.category_text);
            scb = (SmoothCheckBox) itemView.findViewById(R.id.category_scb);

        }
    }

    /*---------------------------------------------------------------------------
    Function Name:                getItemCount()
    Description:                  Necessary method to override: How many items
                                  in the RecyclerView
    Input:                        None
    Output:                       int - number of cards/items
    ---------------------------------------------------------------------------*/
    @Override
    public int getItemCount() {
        return categories.size();
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreateViewHolder()
    Description:                  Necessary method to override: Defines the layout
                                  and type of each view holder
    Input:                        ViewGroup viewGroup
                                  int viewType
    Output:                       int - number of cards/items
    ---------------------------------------------------------------------------*/
    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Inflates layout for each category item
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_create_event_category, viewGroup, false);

        // Change the fonts for each TextView in the card
        overrideFonts(v.getContext(),v);

        // Returns new CategoryViewHolder
        CategoryViewHolder evh = new CategoryViewHolder(v);
        return evh;
    }

    /*---------------------------------------------------------------------------
    Function Name:                onBindViewHolder()
    Description:                  Necessary method to override: Binds information
                                  to each view holder at position i
    Input:                        CategoryViewHolder categoryViewHolder
                                  int i - position of the item in the RecyclerView
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onBindViewHolder(final CategoryViewHolder categoryViewHolder, int i) {
        final int j = i;

        // Get the CategoryTextButton object information for the specific card
        final CategoryTextButton currentSub = categories.get(i);

        // Set the text for the category
        categoryViewHolder.text.setText(currentSub.text);

        //On Check Change Listener for the SmoothCheckBox to set max three categories to check
        categoryViewHolder.scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                //("SmoothCheckBox", String.valueOf(isChecked));

                // If the person has checked something and yet to pick more than three categ.
                if (isChecked) {
                    if (counterMaxThree < 3) {
                        counterMaxThree++;

                        // Checks which category has been chosen based on the position
                        // and updates category list
                        if (j == 0) {
                            categoryList.add("Food");
                        }
                        if (j == 1) {
                            categoryList.add("Social");
                        }
                        if (j == 2) {
                            categoryList.add("Concerts");
                        }
                        if (j == 3) {
                            categoryList.add("Sports");
                        }
                        if (j == 4) {
                            categoryList.add("Student Orgs");
                        }
                        if (j == 5) {
                            categoryList.add("Academic");
                        }
                        if (j == 6) {
                            categoryList.add("Free");
                        }

                        currentSub.setCheckedBoolean(true);


                    }

                    // If user has checked more than three uncheck the SmoothCheckBox
                    else if( counterMaxThree >=3){
                        counterMaxThree++;
                        categoryViewHolder.scb.setChecked(false,false);
                    }
                }
                // If the person has deselected a category
                else{

                    // Decrement counter if the user has checked more than three things
                    if(counterMaxThree > 3){
                        counterMaxThree--;
                    }
                    // If the user has not checked more than three things decrement counter
                    else if (counterMaxThree > 0 && counterMaxThree <=3) {
                        counterMaxThree--;

                        // Update category list of what category has been deselected based on position
                        if (j == 0) {
                            categoryList.remove("Food");
                        }
                        if (j == 1) {
                            categoryList.remove("Social");
                        }
                        if (j == 2) {
                            categoryList.remove("Concerts");
                        }
                        if (j == 3) {
                            categoryList.remove("Sports");
                        }
                        if (j == 4) {
                            categoryList.remove("Student Orgs");
                        }
                        if (j == 5) {
                            categoryList.remove("Academic");
                        }
                        if (j == 6) {
                            categoryList.remove("Free");
                        }

                        // Set checked boolean for CategoryTextButton object
                        currentSub.setCheckedBoolean(false);

                    }
                }
            }

        });
    }

    /*---------------------------------------------------------------------------
    Function Name:                getList()
    Description:                  Returns category list
    Input:                        None
    Output:                       ArrayList<String> of categories checked
    ---------------------------------------------------------------------------*/
    public ArrayList<String> getList() {
        return categoryList;
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

}