package com.spuds.eventapp.Shared;

/**
 * Created by David on 4/27/16.
 */
public class Subscription {
    public String userId;
    public String name;
    public String picture;
    public boolean follow;

    public Subscription(String userId, String name, String picture, boolean follow) {
        this.userId = userId;
        this.name = name;
        this.picture = picture;
        this.follow = follow;
    }

}
