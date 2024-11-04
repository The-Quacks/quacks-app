package com.example.quacks_app;

import android.media.Image;

public class UserProfile {
    private String userName;
    //private Image profilePicture;
    private String email;
    private String phoneNumber;

    public UserProfile(){

    }

    public UserProfile(String userName, String email, String phoneNumber) {
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    //public Image getProfilePicture() { return profilePicture;}

    //public void setProfilePicture(Image profilePicture) { this.profilePicture = profilePicture; }

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
