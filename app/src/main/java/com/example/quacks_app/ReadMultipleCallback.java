package com.example.quacks_app;

import java.util.ArrayList;

/**
 * The {@code ReadMultipleCallback} interface provides a template
 * for functionality for success and failure of a read multiple
 * event call to the database
 */
public interface ReadMultipleCallback<T> {
    void onReadMultipleSuccess(ArrayList<T> data);
    void onReadMultipleFailure(Exception e);

    //void onDataRead(T model);
}