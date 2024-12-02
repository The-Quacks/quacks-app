package com.example.quacks_app;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a collection of facilities with functionality to create, update, and delete facilities.
 */
public class Facilities implements Serializable {
    private ArrayList<Facility> facilityDataList;

    /**
     * Initializes a new list of facilities.
     */
    public void createNew() {
        facilityDataList = new ArrayList<>();
    }

    /**
     * Updates the list of facilities by adding a new facility.
     *
     * @param facility The {@code Facility} object to be added.
     *                 If {@code null}, the operation will be ignored.
     */
    public void updateFacilities(Facility facility) {
        if (facility != null) {
            facilityDataList.add(facility);
        }
    }

    /**
     * Removes a facility from the list.
     *
     * @param facility The {@code Facility} object to be removed.
     *                 If {@code null} or not present in the list, the operation will be ignored.
     */
    public void deleteFacility(Facility facility) {
        if (facility != null) {
            facilityDataList.remove(facility);
        }
    }
}
