package com.example.quacks_app;

import android.media.Image;

import androidx.appcompat.app.AppLocalesMetadataHolderService;

import java.util.Date;

public class Event extends RepoModel{
    private Date dateTime;
    private String description;
    private Image QRCode;
    private ApplicantList applicantList;
    private Facility facility;
    private String organizerId;

    public Event(Date dateTime, String description, ApplicantList applicantList, Facility facility, String organizerId) {}

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

    public ApplicantList getApplicantList() {
        return applicantList;
    }

    public void setApplicantList(ApplicantList applicantList) {
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
        return "";
    }

    public String getSubDisplay() {
        return "";
    }
}
