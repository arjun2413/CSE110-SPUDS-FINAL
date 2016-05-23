package com.spuds.eventapp.Shared;

/**
 * Created by David on 5/17/16.
 */
public class CategoryTextButton {
    public String text;
    public boolean checked;

    public CategoryTextButton(String text, boolean checked){
        this.text = text;
        this.checked = checked;
    }

    public void setCheckedBoolean(boolean checked) {
        this.checked = checked;
    }
}
