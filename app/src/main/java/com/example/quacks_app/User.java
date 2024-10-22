package com.example.quacks_app;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class User extends RepoModel {
    private String deviceId;
    @Exclude
    private EnumSet<Role> roles; // Note: Firebase does not like enum sets
    private String userProfileId;
    // Location

    public User() {}

    public User(String deviceId) {
        this.deviceId = deviceId;
        roles = EnumSet.noneOf(Role.class);
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

    @Exclude
    public EnumSet<Role> getRoles() {
        return this.roles;
    }

    public UserProfile getUserProfile(ReadCallback readCallback) {
        return new UserProfile(); // placeholder
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfileId = userProfile.getId();
    }

    @PropertyName("roles")
    public List<String> getRolesFirestore() {
        return roles.stream().map(Role::name).collect(Collectors.toList());
    }

    @PropertyName("roles")
    public void setRolesFirestore(List<String> roleStrings) {
        this.roles = roleStrings.stream().map(Role::valueOf)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Role.class)));
    }
}
