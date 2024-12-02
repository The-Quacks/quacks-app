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
 * Represents a facility with its geographical location, contact information, and associated events.
 * Implements {@code Serializable} for object serialization and {@code Listable} for UI representation.
 */
public class Facility extends RepoModel implements Serializable, Listable {

    private String accessible;
    private String name;
    private String phone;
    private String details;
    private double latitude;
    private double longitude;
    private String organizerId;
    private String eventListId;
    private ArrayList<Event> successful_events;

    /**
     * Default constructor for creating a new Facility.
     */
    public Facility() {

    }

    /**
     * Sets the name of the facility.
     *
     * @param test1 The name of the facility.
     */
    public void setName(String test1) {
        this.name = test1;
    }

    /**
     * Gets the name of the facility.
     *
     * @return The facility name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the contact information for the facility.
     *
     * @return The contact phone number.
     */
    public String getPhone(){
        return this.phone;
    }

    public String getDisplay() {
        return name;
    }

    public String getSubDisplay() {
        return "";
    }

    public void setContactInfo(String test3) {
        this.phone = test3;
    }

    /**
     * Sets the details of the facility.
     *
     * @param test4 A string describing the facility.
     */
    public void setDetails(String test4) {
        this.details = test4;
    }

    /**
     * Gets the details of the facility.
     *
     * @return A string describing the facility.
     */
    public String getDetails(){
        return this.details;
    }

    /**
     * Sets the accessibility status of the facility.
     *
     * @param test5 A string indicating the accessibility status (e.g., "Yes", "No").
     */
    public void setaccessibilityStat(String test5) {
        this.accessible = test5;
    }

    /**
     * Gets the accessibility status of the facility.
     *
     * @return A string indicating the accessibility status.
     */
    public String getAccessible(){
        return this.accessible;
    }

    /**
     * Adds a notification (successful event) to the facility's list.
     *
     * @param eventId The event to add.
     */
    public void setNotification(Event eventId){
        if (eventId != null){
            successful_events.add(eventId);
        }
    }

    /**
     * Gets the list of successful events (notifications) for the facility.
     *
     * @return A list of {@code Event} objects.
     */
    public ArrayList<Event> getNotifications(){
        return this.successful_events;
    }

    /**
     * Sets the organizer ID for the facility.
     *
     * @param organizerId The ID of the organizer.
     */
    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    /**
     * Gets the organizer ID for the facility.
     *
     * @return The organizer ID.
     */
    public String getOrganizerId() {
        return this.organizerId;
    }

    /**
     * Sets the ID of the event list associated with the facility.
     *
     * @param eventListId The ID of the event list.
     */
    public void setEventListId(String eventListId) {
        this.eventListId = eventListId;
    }

    /**
     * Gets the ID of the event list associated with the facility.
     *
     * @return The event list ID.
     */
    public String getEventListId() {
        return this.eventListId;
    }

    /**
     * Sets the latitude of the facility.
     *
     * @param latitude The latitude value.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the latitude of the facility.
     *
     * @return The latitude value.
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * Sets the longitude of the facility.
     *
     * @param longitude The longitude value.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the longitude of the facility.
     *
     * @return The longitude value.
     */
    public double getLongitude() {
        return this.longitude;
    }


    /**
     * Sets the geographical coordinates of the facility using a GeoPoint.
     *
     * @param geoPoint The {@code GeoPoint} object containing latitude and longitude.
     */
    public void setGeoPoint(GeoPoint geoPoint) {
        if (geoPoint != null) {
            this.latitude = geoPoint.getLatitude();
            this.longitude = geoPoint.getLongitude();
        }
    }

    /**
     * Gets the geographical coordinates of the facility as a GeoPoint.
     *
     * @return A {@code GeoPoint} object containing latitude and longitude.
     */
    public GeoPoint getGeoPoint() {
        return new GeoPoint(latitude, longitude);
    }

    /**
     * Retrieves the geographical address of the facility as a string.
     *
     * @param context The context used to access location services.
     * @return A string representing the address or an error message if the address cannot be retrieved.
     */
    public String getGeoPointString(Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            } else {
                return "Address not found";
            }
        } catch (IOException e) {
            e.getMessage();
            return "Failed to retrieve address: " + e.getMessage();
        }
    }
}