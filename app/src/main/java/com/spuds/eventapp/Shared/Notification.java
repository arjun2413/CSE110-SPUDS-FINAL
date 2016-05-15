package com.spuds.eventapp.Shared;

/**
 * Created by tina on 5/13/16.
 */
public class Notification {
    private final static String TYPE_REPLY = "Reply Notification";
    private final static String TYPE_INVITE = "Invite Notification";
    private final static String TYPE_UPDATE = "Update Notification";
    private final static String ACTION_REPLY = "just commented:";
    private final static String ACTION_INVITE = "invited you to:";
    private final static String ACTION_UPDATE = "just updated:";

    public String notificationType;
    public String eventId;
    public String userId;
    public String host;
    public String date;
    public String time;
    public String eventName;

    public String picFileName;

    public String commentDescription = "";

    public String actionText;

    public String month = "";
    public String day = "";

    // Event update & Invitation Notification
    public Notification(String notificationType, String eventId, String userId, String picFileName,
                        String host, String date, String eventName) {

        this.notificationType = notificationType;
        this.eventId = eventId;
        this.userId = userId;
        this.picFileName = picFileName;
        this.host = host;
        this.date = date;
        this.eventName = eventName;
        this.notificationType = notificationType;

        updateActionText();
        updateDateValues();
    }

    // Comment
    public Notification(String notificationType, String eventId, String userId, String picFileName,
                        String host, String date, String eventName, String commentDescription) {

        this.notificationType = notificationType;
        this.eventId = eventId;
        this.userId = userId;
        this.picFileName = picFileName;
        this.host = host;
        this.date = date;
        this.eventName  = "in " + eventName;
        this.commentDescription = "\"" + commentDescription + "\"";

        updateActionText();
        updateDateValues();

    }

    void updateActionText() {
        switch (notificationType) {
            case TYPE_REPLY:
                actionText = ACTION_REPLY;
                break;
            case TYPE_INVITE:
                actionText = ACTION_INVITE;
                break;
            case TYPE_UPDATE:
                actionText = ACTION_UPDATE;
                break;
        }
    }

    void updateDateValues() {
        Date dateObject = new Date(date);

        time = dateObject.getTime();

        if (notificationType.equals(TYPE_UPDATE)) {
            day = dateObject.getDay();
            month = dateObject.getMonth(true);
        }
    }

}
