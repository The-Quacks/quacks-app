package com.example.quacks_app;

import java.io.Serializable;
import java.util.ArrayList;

public class Facility implements Serializable {

    private String name;
    private String location;
    private String contactInfo;
    private String details;
    private String accessibilityStat;


    public void setName(String named){
        name = named;
    }

    public void setLocation(String locationed){
        location = locationed;
    }

    public void setContactInfo(String contactInfod){
        contactInfo = contactInfod;
    }

    public void setDetails(String detailed){
        details = detailed;
    }

    public void setaccessibilityStat(String status){
        accessibilityStat = status;
    }

    public String getName(){
        return name;
    }

    public String getLocation(){
        return location;
    }
    public String getContactInfo(){
        return contactInfo;
    }
    public String getDetails(){
        return details;
    }
    public String accessibilityStat(){
        return accessibilityStat;
    }

}
