package com.example.quacks_app;

/**
 * The {@code UpdateCallback} interface provides a template for functionality
 * for success and failure of an update event call to the database
 */
public interface UpdateCallback {
    void onUpdateSuccess();
    void onUpdateFailure(Exception e);
}
