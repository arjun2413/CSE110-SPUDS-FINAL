package com.spuds.eventapp.Shared;

/**
 * Created by David on 5/17/16.
 */

/*---------------------------------------------------------------------------
Class Name:                CategoryTextButton
Description:               This class describes a CategoryTextButton object,
                           which is used in creating and editing and event.
                           It contains a string and a boolean.
---------------------------------------------------------------------------*/
public class CategoryTextButton {
    public String text;
    public boolean checked;

    /*---------------------------------------------------------------------------
    Function Name:                CategoryTextButton
    Description:                  Constructor
    Input:                        String text, the text of the category
                                  boolean checked, to determine if the checkbox
                                  has been checked or not
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public CategoryTextButton(String text, boolean checked){
        this.text = text;
        this.checked = checked;
    }

    /*---------------------------------------------------------------------------
    Function Name:                setCheckedBoolean
    Description:                  Setter, sets the boolean field
    Input:                        boolean checked
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public void setCheckedBoolean(boolean checked) {
        this.checked = checked;
    }
}
