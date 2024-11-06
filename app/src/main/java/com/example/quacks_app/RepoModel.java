package com.example.quacks_app;


import com.google.firebase.firestore.DocumentId;

public abstract class RepoModel {
    @DocumentId
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}