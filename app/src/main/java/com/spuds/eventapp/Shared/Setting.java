package com.spuds.eventapp.Shared;

/**
 * Created by David on 5/14/16.
 */

/*---------------------------------------------------------------------------
Class Name:                Setting
Description:               This class describes a Setting object.
---------------------------------------------------------------------------*/
public class Setting {
    public String name;
    public int photoId;

    /*---------------------------------------------------------------------------
    Function Name:                Setting
    Description:                  Constructor
    Input:                        String name,
                                  int photoId
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public Setting(String name, int photoId) {
        this.name = name;
        this.photoId = photoId;
    }

}
