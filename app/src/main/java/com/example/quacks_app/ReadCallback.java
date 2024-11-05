package com.example.quacks_app;


public interface ReadCallback<T> {
    void onDataRead(T data);
}
