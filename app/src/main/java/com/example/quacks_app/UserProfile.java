package com.example.quacks_app;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class UserProfile implements Serializable {
    private String userName;
    private Bitmap profilePicture;
    private String email;
    private String phoneNumber;
    private String profilePictureUrl; // New field for storing the profile picture URL
    private Facility facility;
    private ArrayList<Event> successful_events;

    /**
     * Default constructor for creating an empty UserProfile object.
     */
    public UserProfile() {
    }

    /**
     * Constructor for creating a UserProfile object with specified details.
     *
     * @param userName          The user's name.
     * @param profilePicture    The user's profile picture as a Bitmap.
     * @param email             The user's email address.
     * @param phoneNumber       The user's phone number.
     */
    public UserProfile(String userName, Bitmap profilePicture, String email, String phoneNumber) {
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePictureUrl = null; // Default value for profile picture URL
    }

    /**
     * Constructor for creating a UserProfile object from a Parcel.
     * Used for Parcelable implementation.
     *
     * @param in The Parcel containing the UserProfile data.
     */
    protected UserProfile(Parcel in) {
        userName = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        profilePicture = in.readParcelable(Bitmap.class.getClassLoader());
        profilePictureUrl = in.readString();
    }

    /**
     * Creator object for Parcelable implementation.
     */
    public static final Parcelable.Creator<UserProfile> CREATOR = new Parcelable.Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    public UserProfile (String userName, String email, String phoneNumber) {
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters

    /**
     * Gets the user's name.
     *
     * @return The user's name.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user's name.
     *
     * @param userName The user's name to set.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the user's profile picture.
     *
     * @return The user's profile picture as a Bitmap.
     */
    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    /**
     * Sets the user's profile picture.
     *
     * @param profilePicture The Bitmap of the user's profile picture.
     */
    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     * Gets the user's email address.
     *
     * @return The user's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user's phone number.
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

    public void setFacility(Facility facility){
        this.facility = facility;
    }

    public Facility getFacility(){
        return this.facility;
    }

    public void setNotification(Event eventId){
        if (eventId != null){
            successful_events.add(eventId);
        }
    }

    public ArrayList<Event> getNotifications(){
        return this.successful_events;
    }

    /**
     * Gets the URL of the user's profile picture stored in Firebase.
     *
     * @return The URL of the profile picture.
     */
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    /**
     * Sets the URL of the user's profile picture.
     *
     * @param profilePictureUrl The URL to set.
     */
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

}