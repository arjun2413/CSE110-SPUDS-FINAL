package com.spuds.eventapp.EditEvent;

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

import java.util.List;

import cn.refactor.library.SmoothCheckBox;

/**
 * Created by David on 5/17/16.
 */
public class EditEventRVAdapter extends RecyclerView.Adapter<EditEventRVAdapter.EventViewHolder> {

    public Fragment currentFragment;
    List<CategoryTextButton> categories;

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


    public EditEventRVAdapter(List<CategoryTextButton> categories, Fragment currentFragment){
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
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        final CategoryTextButton currentSub = categories.get(i);
        eventViewHolder.text.setText(currentSub.text);
        eventViewHolder.scb.setChecked(currentSub.checked);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
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
