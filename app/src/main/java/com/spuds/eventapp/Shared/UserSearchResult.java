package com.spuds.eventapp.Shared;

/**
 * Created by tina on 5/30/16.
 */
public class UserSearchResult {
    public String userId;
    public String name;
    public String picture;
    public boolean follow;

    public UserSearchResult(String userId, String name, String picture, boolean follow) {
        this.userId = userId;
        this.name = name;
        this.picture = picture;
        this.follow = follow;
    }

    public UserSearchResult(User user, boolean follow) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.picture = user.getPicture();
        this.follow = follow;
    }
}
