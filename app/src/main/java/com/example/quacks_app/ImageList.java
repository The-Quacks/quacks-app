package com.example.quacks_app;

import java.util.ArrayList;

public class ImageList extends RepoModel{
    private ArrayList<String> imageIds;

    public ImageList() {
        imageIds = new ArrayList<String>();
    }

    public ArrayList<String> getImageIds() {
        return imageIds;
    }

    public void setImageIds(ArrayList<String> imageIds) {
        this.imageIds = imageIds;
    }


    public void addImage(String imageId){imageIds.add(imageId);}

    public void removeImage(String imageId){imageIds.remove(imageId);}
}
