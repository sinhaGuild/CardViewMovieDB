package com.example.cardviewdemo.detail;

import com.example.cardviewdemo.lists.MovieDBObject;

/**
 * Created by anuragsinha on 16-05-28.
 */
public class CollectionsObject extends MovieDBObject {

    String collections_details_image;
    String collections_details_title;
    String collections_details_text;

    public CollectionsObject(String collections_details_image, String collections_details_text, String collections_details_title) {
        this.collections_details_image = collections_details_image;
        this.collections_details_text = collections_details_text;
        this.collections_details_title = collections_details_title;
    }

    @Override
    public String getBackdrop_path() {
        return collections_details_image;
    }

    @Override
    public String getOriginalTitle() {
        return collections_details_title;
    }

    @Override
    public String getOverview() {
        return collections_details_text;
    }
}
