package com.example.quacks_app;

public interface UpdateCallback {
    void onUpdateSuccess();
    void onUpdateFailure(Exception e);
}
