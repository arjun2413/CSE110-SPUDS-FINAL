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
public class EditEventRVAdapter extends RecyclerView.Adapter<EditEventRVAdapter.EventViewHolder> {
    public ArrayList<String> categoryList = new ArrayList<String>(3);

    public Fragment currentFragment;
    List<CategoryTextButton> categories;
    int counter = 0;

    public EditEventRVAdapter() {

    }

    ArrayList<String> existingCateg;
    int counter = existingCateg.size();
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView text;
        SmoothCheckBox scb;

        EventViewHolder(View itemView) {
            super(itemView);
            card = (CardView)itemView.findViewById(R.id.cv);
            text = (TextView)itemView.findViewById(R.id.category_text);
            scb = (SmoothCheckBox) itemView.findViewById(R.id.category_scb);

        }
    }


    public EditEventRVAdapter(List<CategoryTextButton> categories, Fragment currentFragment, ArrayList<String> existingCateg){
        this.categories = categories;
        this.currentFragment = currentFragment;
        this.existingCateg = existingCateg;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_create_event_category, viewGroup, false);

        overrideFonts(v.getContext(),v);
        final EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        final CategoryTextButton currentSub = categories.get(i);
        eventViewHolder.text.setText(currentSub.text);
        final int j = i;
        eventViewHolder.scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                Log.d("SmoothCheckBox", String.valueOf(isChecked));

                System.out.println("count"+ counter);
                if (isChecked) {
                    if (counter < 3) {
                        counter++;

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
                            existingCateg.add("Campus Organizations");
                        }
                        if (j == 5) {
                            existingCateg.add("Academic");
                        }
                        if (j == 6) {
                            existingCateg.add("Free");
                        }
                        currentSub.setCheckedBoolean(true);


                    }
                    else if( counter >=3){
                        counter++;
                        eventViewHolder.scb.setChecked(false,false);
                    }
                }
                else{
                    if(counter > 3){
                        counter--;
                    }
                    else if (counter > 0 && counter <=3) {
                        counter--;
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
                            existingCateg.remove("Campus Organizations");
                        }
                        if (j == 5) {
                            existingCateg.remove("Academic");
                        }
                        if (j == 6) {
                            existingCateg.remove("Free");
                        }
                        currentSub.setCheckedBoolean(false);

                    }
                }
                Log.d("SmoothCheckBox2", String.valueOf(existingCateg));
                getList();
            }

        });
    }
    public ArrayList<String> getList(){
        Log.d("SmoothCheckBox3", String.valueOf(existingCateg));
        return existingCateg;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public ArrayList<String> getList(){
        Log.d("SmoothCheckBox3", String.valueOf(categoryList));
        return categoryList;
    }

    private void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView ) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "raleway-regular.ttf"));
            }
        }
        catch (Exception e) {
        }
    }

}