package com.example.quacks_app;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * NotificationList holds notifications
 * The notifications are sorted and correspond to the eventId
 */

public class NotificationList extends RepoModel implements Serializable {
    private ArrayList<Notification> notifications;
    private String eventId;
    private String notificationListId;

    /**
     * Creates a NotificationList for an event
     */
    public NotificationList(){ notifications = new ArrayList<>();}

    /**
     * Sets the eventId for the notification list
     */
    public void setNotificationEventId(String id){
        this.eventId = id;
    }
    /**
     * Gets the eventId for the notification list
     * @ return String id
     */
    public String getNotificationEventId(){
        return this.eventId;
    }

    /**
     * Retrieves the list of notifications
     * @ return an {@code ArrayList} of {@code String} representing notification
     */
    public ArrayList<Notification> getNotificationList(){
        return notifications;
    }

    /**
     * Adds a notification to the notification list
     */
    public void addNotification(Notification notification){
        notifications.add(notification);
    }

    /**
     * Already have a notification list--just want to set it
     */
    public void setNotificationList(ArrayList<Notification> list){
        this.notifications = list;
    }

    /**
     * Removes a notification from list
     */
    public void removeNotification(Notification notification){
        notifications.remove(notification);
    }

    public void setNotificationListId(String id){
        this.notificationListId = id;
    }
    public String getNotificationListId(){
        return this.notificationListId;
    }

}
