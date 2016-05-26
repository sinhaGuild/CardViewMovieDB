package com.example.cardviewdemo.detail;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by anuragsinha on 16-05-25.
 */
public class SearchSuggestionsMovieDB implements SearchSuggestion {

    public static final Creator<SearchSuggestionsMovieDB> CREATOR = new Creator<SearchSuggestionsMovieDB>() {
        @Override
        public SearchSuggestionsMovieDB createFromParcel(Parcel in) {
            return new SearchSuggestionsMovieDB(in.readString(), in.readString(), in.readString(), in.readString());
        }

        @Override
        public SearchSuggestionsMovieDB[] newArray(int size) {
            return new SearchSuggestionsMovieDB[size];
        }
    };
    String name;
    String backdrop_image;
    String media_type;
    String id;
    private boolean mIsHistory;

    public SearchSuggestionsMovieDB(String backdrop_image, String name, String media_type, String id) {
        this.backdrop_image = backdrop_image;
        this.name = name;
        this.media_type = media_type;
        this.id = id;
    }

    public static Creator<SearchSuggestionsMovieDB> getCREATOR() {
        return CREATOR;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getBackdrop_image() {
        return backdrop_image;
    }

    public void setBackdrop_image(String backdrop_image) {
        this.backdrop_image = backdrop_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean ismIsHistory() {

        return mIsHistory;
    }

    public void setmIsHistory(boolean mIsHistory) {
        this.mIsHistory = mIsHistory;
    }

    @Override
    public String getBody() {
        return name;
    }

    @Override
    public Creator getCreator() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(backdrop_image);
        dest.writeString(media_type);
        dest.writeString(id);
    }

//    public String buildImageURL() {
//        Uri.Builder uri = new Uri.Builder();
//        uri.scheme("http").authority(ConfigItem.TAG_IMAGE_URL_BUILDER).
//                appendPath("t").
//                appendPath("p").
//                appendPath("w500").appendPath(backdrop_image.substring(1));
//        return uri.build().toString();
//    }
}
