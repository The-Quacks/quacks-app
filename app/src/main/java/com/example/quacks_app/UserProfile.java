package com.example.quacks_app;

import android.media.Image;
import java.io.Serializable;
import java.util.ArrayList;

public class UserProfile implements Serializable {
    private String userName;
    private String email;
    private String phoneNumber;
    private String profilePicturePath;

    // Required empty constructor for Firebase
    public UserProfile() {}

    // Constructor without the event list
    public UserProfile(String userName, String email, String phoneNumber) {
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }
}
