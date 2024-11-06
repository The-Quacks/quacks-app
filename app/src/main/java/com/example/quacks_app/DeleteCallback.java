package com.example.quacks_app;

public interface DeleteCallback {
    void onDeleteSuccess();
    void onDeleteFailure(Exception e);
}
