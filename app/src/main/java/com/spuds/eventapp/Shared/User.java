package com.spuds.eventapp.Shared;

import java.io.Serializable;

/**
 * Created by tina on 5/7/16.
 */
public class User implements Serializable {
    private String userId;
    private String name;
    private String description;
    private boolean notificationToggle;
    private int numberFollowing;
    private int numberHosting;
    private String picture;
    private boolean subscribed;

    public User() {}
    public User(String name, String description, String picture) {
        this.setName(name);
        this.setDescription(description);
        this.setPicture(picture);

    }
    public User(String userId, String name, String description, boolean notificationToggle, int numberFollowing,
                int numberHosting, String picture, boolean subscribed) {

        this.setUserId(userId);
        this.setName(name);
        this.setDescription(description);
        this.setNumberFollowing(numberFollowing);
        this.setNumberHosting(numberHosting);
        this.setPicture(picture);
        this.setSubscribed(subscribed);
        this.setNotificationToggle(notificationToggle);

    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberFollowing() {
        return numberFollowing;
    }

    public void setNumberFollowing(int numberFollowing) {
        this.numberFollowing = numberFollowing;
    }

    public int getNumberHosting() {
        return numberHosting;
    }

    public void setNumberHosting(int numberHosting) {
        this.numberHosting = numberHosting;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String avatarFileName) {
        this.picture = avatarFileName;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public boolean getNotificationToggle() {
        return notificationToggle;
    }

    public void setNotificationToggle(boolean notificationToggle) {
        this.notificationToggle = notificationToggle;
    }
}
