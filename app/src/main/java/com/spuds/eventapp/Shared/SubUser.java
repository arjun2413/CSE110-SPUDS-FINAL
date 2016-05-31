package com.spuds.eventapp.Shared;

import java.io.Serializable;

/**
 * Created by tina on 5/7/16.
 */
public class SubUser implements Serializable {
    private String userId;
    private String name;

    public SubUser() {}
    public SubUser(String name, String description, String picture) {
        this.setName(name);
    }
    public SubUser(String userId, String name) {

        this.setUserId(userId);
        this.setName(name);
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

}
