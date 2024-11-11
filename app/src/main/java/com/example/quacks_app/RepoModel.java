package com.example.quacks_app;

import com.google.firebase.firestore.DocumentId;

public abstract class RepoModel {
    @DocumentId
    private String documentId;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

}
