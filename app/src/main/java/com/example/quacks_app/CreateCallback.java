package com.example.quacks_app;

/**
 * The {@code CreateCallback} interface provides a template for functionality
 * for success and failure of a create event call to the database
 */
public interface CreateCallback {
    void onCreateSuccess();
    void onCreateFailure(Exception e);
}
