package com.example.quacks_app;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

/**
 * The {@code RepoModel} class serves as an abstract base class for all models
 * that require Firestore integration. It includes a {@code documentId} field
 * annotated with {@code DocumentId} to represent the Firestore document ID
 * associated with each model. This class implements the {@code Serializable}
 * interface, enabling its subclasses to be serialized for storage or intent transfer.
 */
public abstract class RepoModel implements Serializable {

    @DocumentId
    private String documentId; // Firestore document ID for the model

    /**
     * Retrieves the Firestore document ID for the model.
     *
     * @return A {@code String} representing the document ID.
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     * Sets the Firestore document ID for the model.
     *
     * @param documentId A {@code String} representing the document ID to be set.
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
