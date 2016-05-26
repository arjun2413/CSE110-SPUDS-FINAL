package com.spuds.eventapp.CreateEvent;

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
public class CreateEventRVAdapter extends RecyclerView.Adapter<CreateEventRVAdapter.EventViewHolder> {
    public ArrayList<String> categoryList = new ArrayList<String>(3);

    public Fragment currentFragment;
    List<CategoryTextButton> categories;
    int counter = 0;

    public CreateEventRVAdapter() {

    }

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


    public CreateEventRVAdapter(List<CategoryTextButton> categories, Fragment currentFragment){
        this.categories = categories;
        this.currentFragment = currentFragment;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_create_event_category, viewGroup, false);

        overrideFonts(v.getContext(),v);
        EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(final EventViewHolder eventViewHolder, int i) {
        final int j = i;
        final CategoryTextButton currentSub = categories.get(i);
        eventViewHolder.text.setText(currentSub.text);
        eventViewHolder.scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                Log.d("SmoothCheckBox", String.valueOf(isChecked));

                System.out.println("count"+ counter);
                if (isChecked) {
                    if (counter < 3) {
                        counter++;

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
                            categoryList.add("Campus Organizations");
                        }
                        if (j == 5) {
                            categoryList.add("Academic");
                        }
                        if (j == 6) {
                            categoryList.add("Free");
                        }
                        currentSub.setCheckedBoolean(true);


                    }
                    else if( counter >=3){
                        counter++;
                        eventViewHolder.scb.setChecked(false,false);
                    }
                }
                else{
                    if(counter >= 3){
                        counter--;
                    }
                    else if (counter > 0 && counter <3) {
                        counter--;
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
                            categoryList.remove("Campus Organizations");
                        }
                        if (j == 5) {
                            categoryList.remove("Academic");
                        }
                        if (j == 6) {
                            categoryList.remove("Free");
                        }
                        currentSub.setCheckedBoolean(false);

                    }
                }
                Log.d("SmoothCheckBox2", String.valueOf(categoryList));
                getList();
            }

        });
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