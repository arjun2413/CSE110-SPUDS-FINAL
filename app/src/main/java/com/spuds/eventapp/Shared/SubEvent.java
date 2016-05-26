package com.spuds.eventapp.Shared;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tina on 4/16/16.
 */
public class SubEvent implements Serializable {
    private String eventId;
    private String eventName;

    public SubEvent() {}

     public SubEvent(String eventId, String eventName) {
         this.setEventId(eventId);
         this.setEventName(eventName);
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

}
