package com.spuds.eventapp.Shared;

/**
 * Created by David on 4/27/16.
 */
public class Subscription {
    public String name;
    public int photoId;
    public boolean follow;

    public Subscription(String name, int photoId, boolean follow) {
        this.name = name;
        this.photoId = photoId;
        this.follow = follow;
    }

}
