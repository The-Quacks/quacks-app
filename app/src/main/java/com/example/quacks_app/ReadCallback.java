package com.example.quacks_app;

public interface ReadCallback<T> {
    void onReadSuccess(T data);
    void onReadFailure(Exception e);
}
