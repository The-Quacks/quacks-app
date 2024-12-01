package com.example.quacks_app;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is the notification class
 * A singular notification holds the User, the EventId, the Status of their acceptance in the waitlist, and the eventId
 *
 */


public class Notification extends RepoModel implements Serializable {
   private User user;
   private String ApplicantListId;
   private String EventId;
   private String waitlist_status;
   private String sent_status;
   private String NotificationListId;
   private boolean accepted;

    /**
     * Creates a notification
     */


    public Notification(){
        //need empty constructor for firebase
    }
    /**
     * Sets Notification list Id
     */
    public void setNotificationListId(String id){
        this.NotificationListId = id;
    }

    /**
     * Gets Notification list Id
     */
    public String getNotificationListId(){
        return this.NotificationListId;
    }


    /**
     * Sets the User
     */
    public void setUser(User user){
        this.user = user;
    }

    /**
     * Gets the User
     */
    public User getUser(){
        return this.user;
    }

    /**
     * Sets the EventId
     */
    public void setNotificationEventId(String id){
        this.EventId = id;
    }
    /**
     * Gets the EventId
     */
    public String getNotificationEventId(){
        return this.EventId;
    }

    /**
     * Set the Applicant List Id
     */
    public void setApplicantListId(String id){
        this.ApplicantListId = id;
    }

    /**
     * Gets the Applicant List id
     */
    public String getApplicantListId(){
        return this.ApplicantListId;
    }

    /**
     * Waitlist Status is: "Accepted","Declined","Unknown"
     */
    public void setWaitlistStatus(String status){
        if (status.contains("Accepted") || status.contains("Declined") || status.contains("Unknown")){
            this.waitlist_status = status;
        }
    }

    /**
     * Gets the waitlist status from the notification
     */
    public String getWaitlistStatus(){
        return this.waitlist_status;
    }

    /**
     * Sent Status is: "Sent","Pending","Not Sent"
     */
    public void setSentStatus(String status){
        if (status.contains("Sent")||status.contains("Pending")||status.contains("Not Sent")){
            this.sent_status = status;
        }
    }

    /**
     * Gets sent status
     */
    public String getSentStatus(){
        return this.sent_status;
    }

    /**
     * Sets the accepted boolean
     * @param decision
     */
    public void setAccepted(boolean decision){
        this.accepted = decision;

    }

    /**
     * Gets the accepted boolean
     * @return
     */
    public boolean getAccepted(){
        return this.accepted;
    }


}

