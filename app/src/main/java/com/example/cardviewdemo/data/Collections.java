package com.example.cardviewdemo.data;

import android.net.Uri;

/**
 * Created by anuragsinha on 16-05-18.
 */
public class Collections {

    String name;
    String collections_poster_path;
    String collections_backdrop_path;

    public Collections(String collections_backdrop_path, String collections_poster_path, String name) {
        this.collections_backdrop_path = collections_backdrop_path;
        this.collections_poster_path = collections_poster_path;
        this.name = name;
    }

    public Collections() {
        name = "";
        collections_backdrop_path = "";
        collections_poster_path = "";
    }

    public String getCollections_backdrop_path() {
        return buildURL(collections_backdrop_path);
    }

    public void setCollections_backdrop_path(String collections_backdrop_path) {
        this.collections_backdrop_path = collections_backdrop_path;
    }

    public String getCollections_poster_path() {
        return buildURL(collections_poster_path);
    }

    public void setCollections_poster_path(String collections_poster_path) {
        this.collections_poster_path = collections_poster_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String buildURL(String path) {
        if (path.length() > 1) {
            Uri.Builder uri = new Uri.Builder();
            uri.scheme("http").authority(ConfigItem.TAG_IMAGE_URL_BUILDER).
                    appendPath("t").
                    appendPath("p").
                    appendPath("w500").appendPath(path.substring(1));
            return uri.build().toString();
        }
        return "";
    }
}
