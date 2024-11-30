package com.example.quacks_app;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class User extends RepoModel implements Serializable,Listable {
    private String deviceId;
    private ArrayList<Role> roles; // Note: Firebase does not like enum sets
    private UserProfile userProfile;
    private double latitude;
    private double longitude;

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
    public String getDisplay() {
        if (userProfile != null){
            return userProfile.getUserName();
        }
        return "";
    }

    public String getSubDisplay() {
        String roleStr = "";
        if (roles != null){
            for (Role role:roles){
                roleStr += (role.name() +"/");
            }
        }
        return roleStr;
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

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        if (geoPoint != null) {
            this.latitude = geoPoint.getLatitude();
            this.longitude = geoPoint.getLongitude();
        }
    }

    public GeoPoint getGeoPoint() {
        return new GeoPoint(latitude, longitude);
    }

    public String getGeoPointString(Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            } else {
                return "Address not found for coordinates";
            }
        } catch (IOException e) {
            e.getMessage();
            return "Failed to find coordinates: " + e.getMessage();
        }
    }
}


