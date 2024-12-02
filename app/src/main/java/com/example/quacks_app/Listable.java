package com.example.quacks_app;

/**
 * The {@code Listable} interface provides a contract for objects that can be displayed
 * in a list with primary and secondary display values. It also requires a unique document ID
 * for database operations.
 */
public interface Listable {

    /**
     * Retrieves the primary display value of the object.
     *
     * @return A {@code String} representing the primary display value.
     */
    String getDisplay();

    /**
     * Retrieves the secondary display value of the object.
     *
     * @return A {@code String} representing the secondary display value.
     */
    String getSubDisplay();

    /**
     * Retrieves the unique document ID of the object.
     *
     * @return A {@code String} representing the document ID.
     */
    String getDocumentId();
}
