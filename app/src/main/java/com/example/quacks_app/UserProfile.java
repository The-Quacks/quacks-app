package com.example.quacks_app;

import android.graphics.Bitmap;

public class UserProfile {
    private String userName;
    private Bitmap profilePicture; // Store profile picture as a Bitmap
    private String email;
    private String phoneNumber;

    public UserProfile() {
    }

    public UserProfile(String userName, String email, String phoneNumber) {
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePicture = null; // Start with no profile picture
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
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
}
