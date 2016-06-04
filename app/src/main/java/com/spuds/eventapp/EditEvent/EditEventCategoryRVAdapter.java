package com.spuds.eventapp.EditEvent;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.CategoryTextButton;

import java.util.ArrayList;
import java.util.List;

import cn.refactor.library.SmoothCheckBox;

/**
 * Created by David on 5/17/16.
 */
public class EditEventCategoryRVAdapter extends RecyclerView.Adapter<EditEventCategoryRVAdapter.EventViewHolder> {

    // Fragment that contains Recycler View
    public Fragment currentFragment;
    // Holds information to be put onto RecyclerView
    List<CategoryTextButton> categories;

    /*---------------------------------------------------------------------------
    Function Name:                EditEventCategoryRVAdapter
    Description:                  empty
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public EditEventCategoryRVAdapter() {

    }

    // Holds categories clicked by user
    ArrayList<String> existingCateg;
    // Makes sure categories clicked has a maximum of three
    int counter;

    /*---------------------------------------------------------------------------
    Class Name:                EventViewHolder
    Description:               Holds all the elements a part of a category needed
                               create event.
    ---------------------------------------------------------------------------*/
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView text;
        SmoothCheckBox scb;

        /*---------------------------------------------------------------------------
        Function Name:                EventViewHolder
        Description:                  View holder containing the views
                                      for each card in the Recycler View
        Input:                        View itemView
        Output:                       None.
        ---------------------------------------------------------------------------*/
        EventViewHolder(View itemView) {
            super(itemView);
            card = (CardView)itemView.findViewById(R.id.cv);
            text = (TextView)itemView.findViewById(R.id.category_text);
            scb = (SmoothCheckBox) itemView.findViewById(R.id.category_scb);

        }
    }

    /*---------------------------------------------------------------------------
    Function Name:                CreateEventCategoryRVAdapter
    Description:                  List<CategoryTextButton> categories
                                    - holds information for categories
                                  Fragment currentFragment
                                    - fragment RVAdapter instantiated from
                                  ArrayList<String> existingCateg
                                    - holds information for categories already clicked
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public EditEventCategoryRVAdapter(List<CategoryTextButton> categories, Fragment currentFragment, ArrayList<String> existingCateg){
        this.categories = categories;
        this.currentFragment = currentFragment;
        this.existingCateg = existingCateg;
        this.counter = existingCateg.size();
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
                                  int i
    Output:                       int - number of cards/items
    ---------------------------------------------------------------------------*/
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // Inflates layout for each category item
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_create_event_category, viewGroup, false);

        // Change the fonts for each TextView in the card
        overrideFonts(v.getContext(),v);
        // Returns new EventViewHolder
        EventViewHolder evh = new EventViewHolder(v);

        return evh;
    }

    static boolean first = false;

    /*---------------------------------------------------------------------------
    Function Name:                onBindViewHolder()
    Description:                  Necessary method to override: Binds information
                                  to each view holder at position i
    Input:                        EventViewHolder eventViewHolder
                                  int i - position of the item in the RecyclerView
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onBindViewHolder(final EventViewHolder eventViewHolder, int i) {

        // Get the CategoryTextButton object information for the specific card
        final CategoryTextButton currentSub = categories.get(i);

        // Set the text for the category
        eventViewHolder.text.setText(currentSub.text);
        final int j = i;

        first = false;

        // if a category is checked already
        if(categories.get(i).checked) {
            first = true;
            eventViewHolder.scb.setChecked(true,true);
        }

        //On Check Change Listener for the SmoothCheckBox to set max three categories to check
        eventViewHolder.scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {

                // If the person has checked something and yet to pick more than three categ.
                if (isChecked) {
                    if (counter < 3) {
                        counter++;

                        // Checks which category has been chosen based on the position
                        // and updates category list
                        if (j == 0) {
                            existingCateg.add("Food");
                        }
                        if (j == 1) {
                            existingCateg.add("Social");
                        }
                        if (j == 2) {
                            existingCateg.add("Concerts");
                        }
                        if (j == 3) {
                            existingCateg.add("Sports");
                        }
                        if (j == 4) {
                            existingCateg.add("Student Orgs");
                        }
                        if (j == 5) {
                            existingCateg.add("Academic");
                        }
                        if (j == 6) {
                            existingCateg.add("Free");
                        }
                        currentSub.setCheckedBoolean(true);

                    }

                    // If user has checked more than three uncheck the SmoothCheckBox
                    else if (counter >= 3) {
                        counter++;
                        eventViewHolder.scb.setChecked(false, false);
                    }
                }
                // If the person has deselected a category
                else {
                    // Decrement counter if the user has checked more than three things
                    if (counter > 3) {
                        counter--;
                    }
                    // If the user has not checked more than three things decrement counter
                    else if (counter > 0 && counter <= 3) {
                        counter--;
                        // Update category list of what category has been deselected based on position
                        if (j == 0) {
                            existingCateg.remove("Food");
                        }
                        if (j == 1) {
                            existingCateg.remove("Social");
                        }
                        if (j == 2) {
                            existingCateg.remove("Concerts");
                        }
                        if (j == 3) {
                            existingCateg.remove("Sports");
                        }
                        if (j == 4) {
                            existingCateg.remove("Student Orgs");
                        }
                        if (j == 5) {
                            existingCateg.remove("Academic");
                        }
                        if (j == 6) {
                            existingCateg.remove("Free");
                        }
                        // Set checked boolean for CategoryTextButton object
                        currentSub.setCheckedBoolean(false);

                    }
                }

                getList();
                first = false;
            }

        });

    }

    /*---------------------------------------------------------------------------
    Function Name:                getList()
    Description:                  Returns category list
    Input:                        None
    Output:                       ArrayList<String> of categories checked
    ---------------------------------------------------------------------------*/
    public ArrayList<String> getList(){
        Log.d("SmoothCheckBox3", String.valueOf(existingCateg));
        return existingCateg;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
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
            } else if (v instanceof TextView ) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "raleway-regular.ttf"));
            }
        }
        catch (Exception e) {
        }
    }

}