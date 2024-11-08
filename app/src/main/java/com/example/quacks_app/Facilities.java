package com.example.quacks_app;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class Facilities implements Serializable {
    private ArrayList<Facility> facilityDataList;

    public void createNew(){
        facilityDataList = new ArrayList<Facility>();
    }

    public void updateFacilities(Facility facility){
        if (facility != null){
            facilityDataList.add(facility);
        }
    }

    public void deleteFacility(Facility facility){
        if (facility != null){
            facilityDataList.remove(facility);
        }
    }
}
