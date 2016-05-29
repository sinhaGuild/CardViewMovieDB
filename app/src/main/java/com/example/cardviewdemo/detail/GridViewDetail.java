package com.example.cardviewdemo.detail;

/**
 * Created by anuragsinha on 16-05-16.
 */
public class GridViewDetail {

    private String image;
    private String title;
    private String media_type;

    public GridViewDetail(String image, String title) {
        super();
        this.image = image;
        this.title = title;
    }

    public GridViewDetail(String image, String title, String media_type) {
        this.image = image;
        this.media_type = media_type;
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
