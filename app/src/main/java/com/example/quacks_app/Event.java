package com.example.quacks_app;

import android.graphics.Bitmap;

import androidx.appcompat.app.AppLocalesMetadataHolderService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Event extends RepoModel implements Serializable {
    private Date dateTime;
    private String description;
    private Bitmap QRCode;
    private String applicantList;
    private Facility facility;
    private String organizerId;
    private String QRCodeHash;

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

    public Bitmap getQRCode() {
        return QRCode;
    }

    public void setQRCode(Bitmap QRCode) {
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

    public String getQRCodeHash() {
        return QRCodeHash;
    }

    public void setQRCodeHash(String QRCodeHash) {
        this.QRCodeHash = QRCodeHash;
      
    public String getDisplay() {
        return description;
    }

    public String getSubDisplay() {
        return "";
    }
}
