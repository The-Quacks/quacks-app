package com.example.quacks_app;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public abstract class RepoModel implements Serializable {
    @DocumentId
    private String documentId;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

}
