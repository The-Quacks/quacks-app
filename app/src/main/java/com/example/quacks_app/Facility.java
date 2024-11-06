package com.example.quacks_app;

import java.io.Serializable;

public class Facility extends RepoModel implements Serializable {

    private String accessible;
    private String name;
    private String phone;
    private String details;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    private String location;

    public Facility() {}

    public String getDisplay() {
        return "";
    }

    public String getSubDisplay() {
        return "";
    }


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
}
