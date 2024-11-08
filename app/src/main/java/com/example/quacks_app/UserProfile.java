package com.example.quacks_app;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class UserProfile implements Parcelable {
    private String userName;
    private Bitmap profilePicture;
    private String email;
    private String phoneNumber;
    private String profilePictureUrl; // New field for storing the profile picture URL

    public UserProfile(){

    }
    // Default constructor
    public UserProfile(String userName, Bitmap profilePicture, String email, String phoneNumber) {
        this.userName = userName;
        this.profilePicture = profilePicture;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePictureUrl = null; // Default value
    }

    // Parcelable constructor
    protected UserProfile(Parcel in) {
        userName = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        profilePicture = in.readParcelable(Bitmap.class.getClassLoader());
        profilePictureUrl = in.readString(); // Read URL from Parcel
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        dest.writeParcelable(profilePicture, flags);
        dest.writeString(profilePictureUrl); // Write URL to Parcel
    }

    // Getters and setters
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

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
