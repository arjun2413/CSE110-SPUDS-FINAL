package com.spuds.eventapp.Shared;

/**
 * Created by David on 4/27/16.
 */
/*---------------------------------------------------------------------------
Class Name:                Subscription
Description:               This class describes a Subscription object. A subscription
                           has a name, photo, and a follow button, as well as a
                           userId.
---------------------------------------------------------------------------*/
public class Subscription {
    public String userId;
    public String name;
    public String picture;
    public boolean follow;

    /*---------------------------------------------------------------------------
    Function Name:                Subscription
    Description:                  Constructor
    Input:                        String userId,
                                  String name,
                                  String picture,
                                  boolean follow
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public Subscription(String userId, String name, String picture, boolean follow) {
        this.userId = userId;
        this.name = name;
        this.picture = picture;
        this.follow = follow;
    }

}
