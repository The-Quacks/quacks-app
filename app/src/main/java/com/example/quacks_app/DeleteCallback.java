package com.example.quacks_app;

/**
 * The {@code DeleteCallback} interface provides a template for functionality
 * for success and failure of a delete event call to the database
 */
public interface DeleteCallback {
    void onDeleteSuccess();
    void onDeleteFailure(Exception e);
}
