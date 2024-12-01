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

    public void setEventListId(String eventListId) {
        this.eventListId = eventListId;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
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
                return "Address not found";
            }
        } catch (IOException e) {
            e.getMessage();
            return "Failed to retrieve address: " + e.getMessage();
        }
    }

    public Facility(){}

    public String getDisplay() {
        return name;
    }

    public String getSubDisplay() {return "";}


    public void setName(String test1) {
        this.name = test1;
    }

    public void setContactInfo(String test3) {
        this.phone = test3;
    }

    public void setDetails(String test4) {
        this.details = test4;
    }

    public void setaccessibilityStat(String test5) {
        this.accessible = test5;
    }

    public String getName() {
        return this.name;
    }

    public String getContactInfo() {
        return this.phone;
    }

    public String getDetails(){
        return this.details;
    }

    public String getAccessible(){
        return this.accessible;
    }
    public String getPhone(){
        return this.phone;
    }

    public String getEventListId(){ return this.eventListId; }

    public void setNotification(Event eventId){
        if (eventId != null){
            successful_events.add(eventId);
        }
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public ArrayList<Event> getNotifications(){
        return this.successful_events;
    }
}
