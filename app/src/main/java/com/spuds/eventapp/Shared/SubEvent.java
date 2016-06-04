package com.spuds.eventapp.Shared;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tina on 4/16/16.
 */

/*---------------------------------------------------------------------------
Class Name:                SubEvent
Description:               This class describes a Setting object.
---------------------------------------------------------------------------*/
public class SubEvent implements Serializable {
    private String eventId;
    private String eventName;

    /*---------------------------------------------------------------------------
    Function Name:                SubEvent
    Description:                  Default constructor
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public SubEvent() {}

    /*---------------------------------------------------------------------------
    Function Name:                SubEvent
    Description:                  Constructor
    Input:                        String eventId,
                                  String eventName
    Output:                       None.
    ---------------------------------------------------------------------------*/
    public SubEvent(String eventId, String eventName) {
        this.setEventId(eventId);
        this.setEventName(eventName);
    }

    /* getters and setters */
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
