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

    public Event(Date dateTime, String description, ApplicantList applicantList, Facility facility, String organizerId) {}

    public Event(){

    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
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

    public String getApplicantList() {
        return applicantList;
    }

    public void setApplicantList(String applicantList) {
        this.applicantList = applicantList;
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
