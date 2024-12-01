package com.example.quacks_app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Event extends RepoModel implements Serializable {
    private Date dateTime;
    private String eventName;
    private String description;
    private String qrCodePath;
    private String applicantList;
    private NotificationList notificationList;
    private String facilityId;
    private String organizerId;
    private String QRCodeHash;
    private boolean geolocationRequired;
    private int waitlist_capacity;
    private int registration_capacity;
    private String instructorName;
    private Boolean geoRequired;
    private ApplicantList final_list;

    public Event(Date dateTime, String description, ApplicantList applicantList, String facilityId, String organizerId) {
        // placeholder
    }


    public Event(){
        // need empty constructor for firebase
    }

    /**
     * Set Geo Location
     */
    public void setGeo(Boolean decision) {
        this.geoRequired = decision;
    }

    /**
     * Gets the Geo Location
     * @ return String geo
     */
    public Boolean getGeo() {
        return this.geoRequired;
    }

    /**
     * Sets the Instructor name
     */
    public void setInstructor(String name) {
        this.instructorName = name;
    }

    /**
     * Gets the instructor name
     * @ return String name
     */
    public String getInstructor() {
        return this.instructorName;
    }

    /**
     * Sets the waitlist capacity for the event
     */
    public void setWaitlistCapacity(int number) {
        this.waitlist_capacity = number;
    }

    /**
     * Gets the waitlist capacity for the event
     * @ return int capacity
     */
    public int getWaitlistCapacity(){
        return this.waitlist_capacity;
    }

    /**
     * Sets the registration Capacity
     */
    public void setRegistrationCapacity(int number){
        this.registration_capacity = number;
    }

    /**
     * Gets the registration Capacity
     * @ return int number
     */
    public int getRegistrationCapacity(){
       return this.registration_capacity;
    }

    /**
     * Sets the name of the event
     * @ return none
     */
    public void setEventName(String Name){
        this.eventName = Name;
    }
    /**
     * Gets the name of the event
     * @ return String name
     */
    public String getEventName(){
        return this.eventName;
    }

    /**
     * Retrieves the date and time of the event.
     *
     * @return A {@code Date} object representing the event's date and time.
     */
    public Date getDateTime() {
        return dateTime;
    }

    /**
     * Sets the date and time of the event.
     *
     * @param dateTime A {@code Date} object representing the event's date and time.
     */
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Retrieves the description of the event.
     *
     * @return A {@code String} containing the event's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the event.
     *
     * @param description A {@code String} containing the event's description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the applicant list identifier for the event.
     *
     * @return A {@code String} representing the applicant list identifier.
     */
    public String getApplicantList() {
        if (this.applicantList != null) {
            return this.applicantList;
        }
        return "0";
    }

    /**
     * Sets the applicant list identifier for the event.
     *
     * @param applicantList A {@code String} representing the applicant list identifier.
     */
    public void setApplicantList(String applicantList) {
        this.applicantList = applicantList;
    }

    /**
     * Retrieves the facility identifier for the event.
     *
     * @return A {@code Facility} object representing the facility's identifier.
     */
    public String getFacility() {
        return facilityId;
    }

    /**
     * Sets the facility identifier for the event.
     *
     * @param facilityId A {@code String} representing the facility's identifier.
     */
    public void setFacility(String facilityId) {
        this.facilityId = facilityId;
    }

    /**
     * Retrieves the organizer's identifier for the event.
     *
     * @return A {@code String} representing the organizer's identifier.
     */
    public String getOrganizerId() {
        return organizerId;
    }

    /**
     * Sets the organizer's identifier for the event.
     *
     * @param organizerId A {@code String} representing the organizer's identifier.
     */
    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public String getQRCodeHash() {
        return QRCodeHash;
    }

    public void setQRCodeHash(String QRCodeHash) {
        this.QRCodeHash = QRCodeHash;
    }

    public String getDisplay() {
        return description;
    }

    public String getSubDisplay() {
        return "";
    }

    public String getEventId() {
        return super.getDocumentId();
    }

    public String getQrCodePath() {
        return qrCodePath;
    }

    public void setQrCodePath(String qrCodePath) {
        this.qrCodePath = qrCodePath;
    }

    public void setNotificationList(NotificationList list){
        this.notificationList = list;
    }

    public NotificationList getNotificationList(){
        return this.notificationList;
    }

    /***
     * Sets the final list of applicants when closing registration for event
     */
    public void setFinal_list(ApplicantList final_list){
        this.final_list = final_list;
    }
    /***
     * Sets the final list of applicants when closing registration for event
     */
    public ApplicantList getFinal_list(){
        if (this.final_list == null){
            return null;
        }
        return this.final_list;
    }
}
