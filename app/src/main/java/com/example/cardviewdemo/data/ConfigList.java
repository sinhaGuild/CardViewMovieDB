package com.example.cardviewdemo.data;

/**
 * Created by anuragsinha on 16-05-07.
 */
public class ConfigList {

    //URL of my API
//    public static final String DATA_URL = "http://api.themoviedb.org/3/movie/now_playing?api_key=f5ebdbf26f1f950bf415ff4c7d72c476";

    public static final String DATA_URL = "api.themoviedb.org";

    //Options for MovieDB
    public static final String MDB_NOW_PLAYING = "now_playing";
    public static final String MDB_LATEST = "latest";
    public static final String MDB_POPULAR = "popular";
    public static final String MDB_TOP_RATED = "top_rated";
    public static final String MDB_UPCMONING = "upcoming";

    //KEY
    public static final String API_KEY = "api_key";
    public static final String API_KEY_VALUE = "f5ebdbf26f1f950bf415ff4c7d72c476";
    public static final String PAGES = "page";

    //Tags for my JSON
    public static final String TAG_IMAGE_URL = "poster_path";
    public static final String TAG_BACKDROP = "backdrop_path";
    public static final String TAG_TITLE = "original_title";
    public static final String TAG_POPULARITY = "popularity";
    public static final String TAG_REAL_RELEASE_DATE = "release_date";
    public static final String TAG_LANGUAGE = "original_language";
    public static final String TAG_OVERVIEW = "overview";
    public static final String TAG_VOTER_RATING = "vote_average";
    public static final String TOTAL_PAGES = "total_pages";
    public static final String MOVIE_ID = "id";
}
