package com.example.quacks_app;

import android.media.Image;

import java.io.Serializable;
import java.util.Date;

public class Event extends RepoModel implements Serializable {
    private Date dateTime;
    private String description;
    private Image QRCode;
    private String applicantList;
    private Facility facility;
    private String organizerId;
    private String eventId;

    public Event(Date dateTime, String description, ApplicantList applicantList, Facility facility, String organizerId) {
        // placeholder
    }


    public Event(){
        // need empty constructor for firebase
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
     * Retrieves the QR code image associated with the event.
     *
     * @return An {@code Image} object representing the event's QR code.
     */
    public Image getQRCode() {
        return QRCode;
    }

    /**
     * Sets the QR code image for the event.
     *
     * @param QRCode An {@code Image} object representing the event's QR code.
     */
    public void setQRCode(Image QRCode) {
        this.QRCode = QRCode;
    }

    /**
     * Retrieves the applicant list identifier for the event.
     *
     * @return A {@code String} representing the applicant list identifier.
     */
    public String getApplicantList() {
        return applicantList;
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
     * Retrieves the facility associated with the event.
     *
     * @return A {@code Facility} object representing the event's facility.
     */
    public Facility getFacility() {
        return facility;
    }

    /**
     * Sets the facility for the event.
     *
     * @param facility A {@code Facility} object representing the event's facility.
     */
    public void setFacility(Facility facility) {
        this.facility = facility;
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

    public String getDisplay() {
        return description;
    }

    public String getSubDisplay() {
        return "";
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return super.getId();
    }
}
