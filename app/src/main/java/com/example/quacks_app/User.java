package com.example.quacks_app;

import java.util.EnumSet;
import java.util.Set;

public class User extends RepoModel {
    private String deviceId;
    private Set<Role> roles; // Note: Firebase does not like enum sets
    private String userProfileId;
    // Location

    public User() {}

    public User(String deviceId) {
        this.deviceId = deviceId;
//        roles = EnumSet.noneOf(Role.class);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public UserProfile getUserProfile(ReadCallback readCallback) {
        return new UserProfile(); // placeholder
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfileId = userProfile.getId();
    }
}
