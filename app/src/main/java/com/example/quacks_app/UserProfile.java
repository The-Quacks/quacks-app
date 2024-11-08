package com.example.quacks_app;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * The UserProfile class represents a user's profile information.
 * It includes details such as username, profile picture, email, phone number,
 * and a profile picture URL. The class implements the Parcelable interface
 * to allow UserProfile objects to be passed between activities.
 */
public class UserProfile implements Parcelable {

    // Fields
    private String userName;
    private Bitmap profilePicture;
    private String email;
    private String phoneNumber;
    private String profilePictureUrl; // New field for storing the profile picture URL

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
        this.profilePicture = profilePicture;
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

    /**
     * Describes the contents of the Parcelable object.
     *
     * @return An integer representing the contents.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the UserProfile object to a Parcel.
     *
     * @param dest  The Parcel to write data to.
     * @param flags Flags for writing the Parcel.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        dest.writeParcelable(profilePicture, flags);
        dest.writeString(profilePictureUrl);
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
