package com.example.quacks_app;

import java.util.ArrayList;

public interface ReadMultipleCallback<T> {
    void onReadMultipleSuccess(ArrayList<T> data);
    void onReadMultipleFailure(Exception e);
}
