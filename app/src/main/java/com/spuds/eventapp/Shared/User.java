package com.spuds.eventapp.Shared;

import java.io.Serializable;

/**
 * Created by tina on 5/7/16.
 */
public class User implements Serializable {
    public int userId;
    public String name;
    public String description;
    public boolean verified;
    public int numberFollowing;
    public int numberHosting;
    public String avatarFileName;
    public boolean subscribed;

    public User(int userId, String name, String description, boolean verified, int numberFollowing,
                int numberHosting, String avatarFileName, boolean subscribed) {

        this.userId = userId;
        this.name = name;
        this.description = description;
        this.verified = verified;
        this.numberFollowing = numberFollowing;
        this.numberHosting = numberHosting;
        this.avatarFileName = avatarFileName;
        this.subscribed = subscribed;

    }

}
