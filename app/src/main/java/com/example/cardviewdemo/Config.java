package com.example.cardviewdemo;

/**
 * Created by anuragsinha on 16-05-07.
 */
public class Config {

    //URL of my API
    public static final String DATA_URL = "http://api.themoviedb.org/3/movie/now_playing?api_key=f5ebdbf26f1f950bf415ff4c7d72c476";
    //Tags for my JSON
    public static final String TAG_IMAGE_URL = "poster_path";
    public static final String TAG_BACKDROP = "backdrop_path";
    public static final String TAG_TITLE = "original_title";
    public static final String TAG_POPULARITY = "popularity";
    public static final String TAG_REAL_RELEASE_DATE = "release_date";
    public static final String TAG_LANGUAGE = "original_language";
    public static final String TAG_OVERVIEW = "overview";
    public static final String TAG_VOTER_RATING = "vote_average";
}
