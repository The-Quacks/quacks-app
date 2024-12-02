package com.example.quacks_app;

import android.media.Image;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The {@code UserProfile} class represents a user's profile information, including personal details,
 * a profile picture, and an associated event list.
 * It provides methods for accessing and modifying profile details and managing events.
 */
public class UserProfile implements Serializable {
    private String userName;
    private String email;
    private String phoneNumber;
    private String profilePicturePath;
    private EventList eventList;

    /**
     * Default constructor required for Firebase serialization.
     */
    public UserProfile() {}

    /**
     * Constructs a {@code UserProfile} with the specified user name, email, and phone number.
     *
     * @param userName    The user's name.
     * @param email       The user's email address.
     * @param phoneNumber The user's phone number.
     */
    public UserProfile(String userName, String email, String phoneNumber) {
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Retrieves the user's name.
     *
     * @return The user's name.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user's name.
     *
     * @param userName The name to set.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Retrieves the user's email address.
     *
     * @return The user's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email The email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * Retrieves the user's phone number.
     *
     * @return The user's phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the user's phone number.
     *
     * @param phoneNumber The phone number to set.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Retrieves the path to the user's profile picture.
     *
     * @return The profile picture path.
     */
    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    /**
     * Sets the path to the user's profile picture.
     *
     * @param profilePicturePath The profile picture path to set.
     */
    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    /**
     * Retrieves the list of events associated with the user.
     *
     * @return The {@code EventList} containing the user's events.
     */
    public EventList getEventList() {
        return eventList;
    }

    /**
     * Sets the list of events associated with the user.
     *
     * @param eventList The {@code EventList} to set.
     */
    public void setEventList(EventList eventList) {
        this.eventList = eventList;
    }

    /**
     * Adds an event to the user's event list.
     *
     * @param event The {@code Event} to add.
     */
    public void addEvent(Event event) {
        if (eventList == null) {
            eventList = new EventList();
        }
        eventList.addEvent(event);
    }

    /**
     * Removes an event from the user's event list.
     *
     * @param event The {@code Event} to remove.
     */
    public void removeEvent(Event event) {
        if (eventList != null) {
            eventList.removeEvent(event);
        }
    }
}
