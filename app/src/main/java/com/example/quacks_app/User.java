package com.example.quacks_app;

import android.graphics.Bitmap;
import android.provider.Settings;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class User extends RepoModel implements Serializable {
    private String deviceId;
    private ArrayList<Role> roles; // Note: Firebase does not like enum sets
    private UserProfile userProfile;
    // Location

    public User() {
    }

    public User(String deviceId, ArrayList<Role> roles, UserProfile userProfile) {
        this.deviceId = deviceId;
        this.roles = roles;
        this.userProfile = userProfile;
    }

    public User(String deviceId) {
        this.deviceId = deviceId;
        roles = new ArrayList<>();
    }


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void addRole(Role role) {
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }

    public ArrayList<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }


    public void uploadProfilePicture(Bitmap newImage) {
        // Ensure userProfile is not null and deviceId is set
        if (userProfile != null && deviceId != null) {
            // Upload the profile picture using deviceId
            UpdateProfilePicture.uploadProfilePicture(userProfile, newImage, deviceId);
        }
    }

    public void removeProfilePicture() {
        if (userProfile != null && deviceId != null) {
            // Remove the profile picture using deviceId
            UpdateProfilePicture.removeProfilePicture(userProfile, deviceId);
        }
    }

}
    /*
    public String getDisplay() {
        return userProfile.getUserName();
    }

    public String getSubDisplay() {
        String roleStr = "";
        for (Role role:roles){
            roleStr += (role.name() +"/");
        }
        return roleStr;
    }
     */

