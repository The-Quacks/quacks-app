package com.example.quacks_app;

import java.io.Serializable;
import java.util.Date;

public class Event extends RepoModel implements Serializable {
    private Date dateTime;
    private String description;
    private String qrCodePath;
    private String applicantList;
    private String facilityId;
    private String organizerId;
    private String QRCodeHash;

    public Event(Date dateTime, String description, ApplicantList applicantList, String facilityId, String organizerId) {
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
}
