package com.example.quacks_app;

/**
 * The {@code ReadCallback} interface provides a template for functionality
 * for success and failure of a read event call to the database
 */
public interface ReadCallback<T> {
    void onReadSuccess(T data);
    void onReadFailure(Exception e);
}
