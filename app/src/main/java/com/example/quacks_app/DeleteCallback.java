package com.example.quacks_app;

/**
 * The {@code DeleteCallback} interface provides a template for functionality
 * for success and failure of a delete event call to the database
 */
public interface DeleteCallback {
    /**
     * Called when a delete operation completes successfully.
     * This method should handle any post-deletion logic, such as updating the UI
     * or notifying the user that the operation was successful.
     */
    void onDeleteSuccess();

    /**
     * Called when a delete operation fails.
     * This method should handle any error scenarios, such as displaying an error message
     * to the user or logging the exception for debugging purposes.
     *
     * @param e The {@link Exception} that caused the delete operation to fail.
     */
    void onDeleteFailure(Exception e);
}
