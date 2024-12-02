package com.example.quacks_app;

/**
 * The {@code CreateCallback} interface provides a template for handling the result
 * of a create operation in the database. It defines methods to handle both success
 * and failure scenarios, allowing for robust handling of asynchronous database operations.
 */
public interface CreateCallback {

    /**
     * Called when a create operation completes successfully.
     * This method should handle any post-creation logic, such as updating the UI or
     * notifying the user of the successful creation.
     */
    void onCreateSuccess();

    /**
     * Called when a create operation fails.
     * This method should handle any error scenarios, such as displaying an error message
     * to the user or logging the exception for debugging purposes.
     *
     * @param e The {@link Exception} that caused the failure.
     */
    void onCreateFailure(Exception e);
}
