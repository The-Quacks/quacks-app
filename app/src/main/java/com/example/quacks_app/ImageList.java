package com.example.quacks_app;

import java.util.ArrayList;

/**
 * The {@code ImageList} class represents a list of image IDs, providing methods to manage
 * and retrieve image information. This class extends {@code RepoModel} to integrate
 * with the app's repository and database functionality.
 */
public class ImageList extends RepoModel {
    private ArrayList<String> imageIds;

    /**
     * Constructs an empty {@code ImageList}.
     */
    public ImageList() {
        imageIds = new ArrayList<>();
    }

    /**
     * Retrieves the list of image IDs.
     *
     * @return An {@code ArrayList} of {@code String} containing the image IDs.
     */
    public ArrayList<String> getImageIds() {
        return imageIds;
    }
    /**
     * Sets the list of image IDs.
     *
     * @param imageIds An {@code ArrayList} of {@code String} containing the image IDs to set.
     */
    public void setImageIds(ArrayList<String> imageIds) {
        this.imageIds = imageIds;
    }

    /**
     * Adds an image ID to the list.
     *
     * @param imageId The {@code String} representing the ID of the image to add.
     */
    public void addImage(String imageId){
        imageIds.add(imageId);
    }

    /**
     * Removes an image ID from the list.
     *
     * @param imageId The {@code String} representing the ID of the image to remove.
     */
    public void removeImage(String imageId){
        imageIds.remove(imageId);
    }
}
