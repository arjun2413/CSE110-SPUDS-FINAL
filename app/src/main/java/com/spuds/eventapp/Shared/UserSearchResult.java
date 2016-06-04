package com.spuds.eventapp.Shared;

/**
 * Created by tina on 5/30/16.
 */

/*---------------------------------------------------------------------------
Class Name:                UserSearchResult
Description:               This class describes a UserSearchResult object
---------------------------------------------------------------------------*/
public class UserSearchResult {
    public String userId;
    public String name;
    public String picture;
    public boolean follow;


    /*---------------------------------------------------------------------------
    Function Name:                UserSearchResult
    Description:                  Constructor
    Input:                        User user,
                                  boolean follow
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public UserSearchResult(User user, boolean follow) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.picture = user.getPicture();
        this.follow = follow;
    }
}
