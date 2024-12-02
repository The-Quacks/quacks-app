package com.example.quacks_app;

/**
 * The {@code Cartable} class simplifies the selection and notification process for users.
 * It acts as a data model for representing and managing selectable user data in lists.
 */
public class Cartable {
    String field;
    String subfield;
    boolean selected;

    /**
     * Default constructor for creating an empty {@code Cartable} object.
     */
    public Cartable() {
        // Default constructor
    }

    /**
     * Constructs a {@code Cartable} object with specified field, subfield, selection status,
     * and a reference to a user's profile.
     *
     * @param field    The main information about the user (e.g., name).
     * @param subfield The additional information about the user (e.g., role or ID).
     * @param selected The selection status of the user.
     * @param profile  The {@link UserProfile} of the user (currently unused in this implementation).
     */
    public Cartable(String field, String subfield, Boolean selected, UserProfile profile) {
        this.field = field;
        this.subfield = subfield;
        this.selected = selected;
    }

    /**
     * Sets the primary field of the user.
     *
     * @param field The main information about the user.
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * Retrieves the primary field of the user.
     *
     * @return The main information about the user.
     */
    public String getField() {
        return this.field;
    }

    /**
     * Sets the secondary field of the user.
     *
     * @param subfield The additional information about the user.
     */
    public void setSubfield(String subfield) {
        this.subfield = subfield;
    }

    /**
     * Retrieves the display value for the primary field.
     *
     * @return The main information to display.
     */
    public String getDisplay() {
        return field;
    }

    /**
     * Retrieves the display value for the secondary field.
     *
     * @return The additional information to display.
     */
    public String getSubDisplay() {
        return subfield;
    }

    /**
     * Checks whether the user is selected (carted).
     *
     * @return {@code true} if the user is selected, {@code false} otherwise.
     */
    public boolean Carted() {
        return this.selected;
    }

    /**
     * Updates the selection status of the user.
     *
     * @param selected {@code true} to mark the user as selected, {@code false} otherwise.
     */
    public void setCart(boolean selected) {
        this.selected = selected;
    }

    /**
     * Checks whether the specified field matches the user's field and returns its selection status.
     *
     * @param field The field to compare with the user's field.
     * @return {@code true} if the fields match and the user is selected; {@code false} otherwise.
     */
    public boolean getCarted(String field) {
        if (field.equals(this.field)) {
            return this.selected;
        }
        return true;
    }
}


