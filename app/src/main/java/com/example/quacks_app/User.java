package com.example.quacks_app;

public class User extends RepoModel {
    private String deviceId;
    private String userName;

    public User() {}

    public User(String deviceId, String userName) {
        this.deviceId = deviceId;
        this.userName = userName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
