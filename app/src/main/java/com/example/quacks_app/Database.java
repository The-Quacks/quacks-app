package com.example.quacks_app;

import com.google.firebase.firestore.FirebaseFirestore;

public class Database {
    private FirebaseFirestore db;

    public Database() {
        db = FirebaseFirestore.getInstance();
    }

    public FirebaseFirestore getDb() {
        return db;
    }
}
