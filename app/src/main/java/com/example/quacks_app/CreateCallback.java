package com.example.quacks_app;

public interface CreateCallback {
    void onCreateSuccess();
    void onCreateFailure(Exception e);
}
