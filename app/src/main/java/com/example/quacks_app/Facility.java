package com.example.quacks_app;

import java.io.Serializable;

public class Facility extends RepoModel implements Serializable {

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


}
