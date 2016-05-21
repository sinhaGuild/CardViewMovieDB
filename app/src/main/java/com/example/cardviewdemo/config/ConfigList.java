package com.example.cardviewdemo.config;

/**
 * Created by anuragsinha on 16-05-07.
 */
public class ConfigList {

    //URL of my API
//    public static final String DATA_URL = "http://api.themoviedb.org/3/movie/now_playing?api_key=f5ebdbf26f1f950bf415ff4c7d72c476";

    public static final String DATA_QUERY_PARAMS = "videos,credits,similar";

    public static final String DATA_URL = "api.themoviedb.org";
    public static final String DATA_TYPE_MOVIES = "movie";
    public static final String DATA_TYPE_TV = "tv";

    //Options for MovieDB Movies
    public static final String MOVIES_NOW_PLAYING = "now_playing";
    public static final String MOVIES_LATEST = "latest";
    public static final String MOVIES_POPULAR = "popular";
    public static final String MOVIES_TOP_RATED = "top_rated";
    public static final String MOVIES_UPCOMING = "upcoming";

    //Options for MovieDB TV Shows
    public static final String TV_LATEST = "latest";
    public static final String TV_ON_THE_AIR = "on_the_air";
    public static final String TV_AIRING_TODAY = "airing_today";
    public static final String TV_TOP_RATED = "top_rated";
    public static final String TV_POPULAR = "popular";
    public static final String TV_NAME = "original_name";

    //KEY
    public static final String API_KEY = "api_key";
    public static final String API_KEY_VALUE = "f5ebdbf26f1f950bf415ff4c7d72c476";
    public static final String PAGES = "page";
    public static final String VIDEO = "append_to_response";

    //Tags for my MOVIES JSON
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

    //Tags for TV JSON
    public static final String TAG_FIRST_AIR_DATE = "first_air_date";
}
