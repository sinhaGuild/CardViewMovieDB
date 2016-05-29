package com.example.cardviewdemo.lists;

import android.util.Log;

import java.text.DecimalFormat;

/**
 * Created by anuragsinha on 16-05-07.
 */
public class MovieDBObject {

    public static final String TAG_IMAGE_URL_BUILDER = "http://image.tmdb.org/t/p/w500/";

    //Data Variables
    private String poster_path;
    private String backdrop_path;
    private String original_title;
    private int popularity;
    private String release_date;
    private String language;
    private String overview;
    private String vote_average;
    private int movie_id;


    //Getters and Setters

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }



    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {

        this.poster_path = TAG_IMAGE_URL_BUILDER+poster_path.substring(1);
        Log.v("Poster Path", this.poster_path);
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }


    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = TAG_IMAGE_URL_BUILDER+backdrop_path.substring(1);
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public void setOriginalTitle(String name) {
        this.original_title = name;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getReleaseDate() {
        if (release_date.length() == 4) {
            return release_date;
        } else if (release_date.length() > 4) {
            return release_date.substring(0, Math.min(release_date.length(), 4));
        } else {
            // whatever is appropriate in this case
            throw new IllegalArgumentException("word has less than 3 characters!");
        }
    }

    public void setReleaseDate(String realName) {
        this.release_date = realName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
        Log.v("Overview :",this.overview);
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        DecimalFormat precision = new DecimalFormat("0.0");
        this.vote_average = precision.format(vote_average);
    }

}
