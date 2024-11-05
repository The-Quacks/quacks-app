package com.example.quacks_app;

import android.media.Image;

import java.util.Date;

public class Event {

    private String eventName;
    private Date dateTime;
    private String description;
    private Image QRCode;
    private Facility facility;
    private String organizerId;
    private int waitlist_capacity;
    private int class_capacity;
    private String instructor;
    private String geolocation;

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(String geolocation) {
        this.geolocation = geolocation;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }



    public void setClass_capacity(int class_capacity) {
        this.class_capacity = class_capacity;
    }

    public int getWaitlist_capacity() {
        return waitlist_capacity;
    }

    public void setWaitlist_capacity(int waitlist_capacity) {
        this.waitlist_capacity = waitlist_capacity;
    }


    public int getClass_capacity() {
        return class_capacity;
    }

    public Event() {}

    public Date getStartDateTime() {
        return dateTime;
    }
    public Date getEndDateTime() {
        return dateTime;
    }

    public void setStartDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
    public void setEndDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image getQRCode() {
        return QRCode;
    }

    public void setQRCode(Image QRCode) {
        this.QRCode = QRCode;
    }


    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }
}
