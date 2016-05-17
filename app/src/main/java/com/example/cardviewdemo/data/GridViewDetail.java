package com.example.cardviewdemo.data;

/**
 * Created by anuragsinha on 16-05-16.
 */
public class GridViewDetail {

    private String image;
    private String title;

    public GridViewDetail(String image, String title) {
        super();
        this.image = image;
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
