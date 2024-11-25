package com.example.quacks_app;

import java.io.Serializable;
import java.util.ArrayList;

public class Facility extends RepoModel implements Serializable, Listable {

    private String accessible;
    private String name;
    private String phone;
    private String details;
    private String location;
    private String organizerId;
    private String eventListId;

    public void setEventListId(String eventListId) {
        this.eventListId = eventListId;
    }

    private ArrayList<Event> successful_events;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Facility(){}

    public String getDisplay() {
        return name;
    }

    public String getSubDisplay() {return location;}


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
