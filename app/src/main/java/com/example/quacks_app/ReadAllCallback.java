package com.example.quacks_app;

import java.util.ArrayList;

public interface ReadAllCallback<T> {
    void onDataRead(ArrayList<T> data);
}
