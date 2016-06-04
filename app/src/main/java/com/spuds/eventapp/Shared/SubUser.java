package com.spuds.eventapp.Shared;

import java.io.Serializable;

/**
 * Created by tina on 5/7/16.
 */

/*---------------------------------------------------------------------------
Class Name:                SubUser
Description:               This class describes a SubUser object
---------------------------------------------------------------------------*/
public class SubUser implements Serializable {
    private String userId;
    private String name;


    /*---------------------------------------------------------------------------
    Function Name:                SubUser
    Description:                  Default constructor
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public SubUser() {}

    /*---------------------------------------------------------------------------
    Function Name:                SubUser
    Description:                  Default constructor
    Input:                        String userId,
                                  String name: the name of the subUser
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public SubUser(String userId, String name) {

        this.setUserId(userId);
        this.setName(name);
    }

    /* getters and setters */
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
