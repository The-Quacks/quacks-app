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

/**
 * The {@code User} class represents a user within the application, holding user-specific data such as roles,
 * geolocation, and profile information. This class is serializable and provides utility methods for handling
 * user-related operations.
 */
public class User extends RepoModel implements Serializable, Listable {
    private String deviceId;
    private ArrayList<Role> roles; // Note: Firebase does not like enum sets
    private UserProfile userProfile;
    private double latitude;
    private double longitude;

    /**
     * Default constructor for creating a new {@code User} instance.
     * Initializes the roles list as an empty ArrayList.
     */
    public User() {
        roles = new ArrayList<>();
    }

    /**
     * Constructs a new {@code User} instance with the specified device ID, roles, and user profile.
     *
     * @param deviceId    The device ID of the user.
     * @param roles       A list of roles assigned to the user.
     * @param userProfile The user's profile information.
     */
    public User(String deviceId, ArrayList<Role> roles, UserProfile userProfile) {
        this.deviceId = deviceId;
        this.roles = roles;
        this.userProfile = userProfile;
    }

    /**
     * Constructs a new {@code User} instance with the specified device ID.
     * Initializes the roles list as an empty ArrayList.
     *
     * @param deviceId The device ID of the user.
     */
    public User(String deviceId) {
        this.deviceId = deviceId;
        roles = new ArrayList<>();
    }

    /**
     * Returns the user's device ID.
     *
     * @return The device ID.
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * Sets the user's device ID.
     *
     * @param deviceId The new device ID.
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * Adds a role to the user's list of roles if it is not already present.
     *
     * @param role The role to add.
     */
    public void addRole(Role role) {
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }

    /**
     * Returns a display-friendly string, typically the user's name from their profile.
     *
     * @return The display name.
     */
    public String getDisplay() {
        if (userProfile != null){
            return userProfile.getUserName();
        }
        return "";
    }

    /**
     * Returns a string representation of the user's roles as a sub-display string.
     *
     * @return The sub-display string.
     */
    public String getSubDisplay() {
        String roleStr = "";
        if (roles != null){
            for (Role role:roles){
                roleStr += (role.name() +"/");
            }
        }
        return roleStr;
    }

    /**
     * Removes a role from the user's list of roles.
     *
     * @param role The role to remove.
     */
    public void removeRole(Role role) {
        roles.remove(role);
    }

    /**
     * Returns the list of roles assigned to the user.
     *
     * @return The list of roles.
     */
    public ArrayList<Role> getRoles() {
        return this.roles;
    }

    /**
     * Sets the list of roles for the user.
     *
     * @param roles The new list of roles.
     */
    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }

    /**
     * Returns the user's profile information.
     *
     * @return The user's profile.
     */
    public UserProfile getUserProfile() {
        return this.userProfile;
    }


    /**
     * Sets the user's profile information.
     *
     * @param userProfile The new user profile.
     */
    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    /**
     * Sets the latitude for the user's geolocation.
     *
     * @param latitude The latitude value.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns the latitude of the user's geolocation.
     *
     * @return The latitude value.
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * Sets the longitude for the user's geolocation.
     *
     * @param longitude The longitude value.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Returns the longitude of the user's geolocation.
     *
     * @return The longitude value.
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * Sets the user's geolocation using a {@code GeoPoint}.
     *
     * @param geoPoint The {@code GeoPoint} object containing latitude and longitude values.
     */
    public void setGeoPoint(GeoPoint geoPoint) {
        if (geoPoint != null) {
            this.latitude = geoPoint.getLatitude();
            this.longitude = geoPoint.getLongitude();
        }
    }

    /**
     * Returns the user's geolocation as a {@code GeoPoint}.
     *
     * @return The {@code GeoPoint} object.
     */
    public GeoPoint getGeoPoint() {
        return new GeoPoint(latitude, longitude);
    }

    /**
     * Returns a string representation of the user's geolocation.
     * If the geolocation is invalid, an error message is returned.
     *
     * @param context The application context.
     * @return The geolocation as a string.
     */
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

