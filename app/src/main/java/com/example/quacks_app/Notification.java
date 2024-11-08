package com.example.quacks_app;

import java.util.ArrayList;

public class Notification {
    private ArrayList<Event> notifications;
    private String deviceId;

    public Notification(String deviceId) {
        this.notifications = new ArrayList<>();
        this.deviceId = deviceId;

    }

    public void setNotification(Event event) {
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        notifications.add(event);
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}

