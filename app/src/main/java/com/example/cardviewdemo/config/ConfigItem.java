package com.example.cardviewdemo.config;

/**
 * Created by anuragsinha on 16-05-13.
 */
public class ConfigItem {

    //URL of my API
//http://api.themoviedb.org/3/movie/271110?api_key=f5ebdbf26f1f950bf415ff4c7d72c476
    public static final String API_KEY = "api_key";
    public static final String API_KEY_VALUE = "f5ebdbf26f1f950bf415ff4c7d72c476";
    public static final String DATA_URL = "api.themoviedb.org";
    public static final String TAG_IMAGE_URL_BUILDER = "image.tmdb.org";

    public static final String GET_POPULAR_MOVIES = "http://api.themoviedb.org/3/discover/movie?language=en&sort_by=popularity.desc&api_key=" + API_KEY;
    public static final String GET_HIGHEST_RATED_MOVIES = "http://api.themoviedb.org/3/discover/movie?vote_count.gte=500&language=en&sort_by=vote_average.desc&api_key=" + API_KEY;
    public static final String GET_TRAILERS = "http://api.themoviedb.org/3/movie/%s/videos?api_key=" + API_KEY_VALUE;
    public static final String GET_REVIEWS = "http://api.themoviedb.org/3/movie/%s/reviews?api_key=" + API_KEY_VALUE;
//    public static final String POSTER_PATH = "http://image.tmdb.org/t/p/w342";
//    public static final String BACKDROP_PATH = "http://image.tmdb.org/t/p/w780";


    //KEY
    public static final String PAGES = "page";
    public static final String RESULTS = "results";

    //Tags for my Movies
    public static final String POSTER_PATH = "poster_path";
    public static final String TAG_BACKDROP = "backdrop_path";
    public static final String TAG_TITLE = "original_title";
    public static final String TAG_POPULARITY = "popularity";
    public static final String TAG_REAL_RELEASE_DATE = "release_date";
    public static final String TAG_OVERVIEW = "overview";
    public static final String TAG_VOTER_RATING = "vote_average";
    public static final String MOVIE_ID = "id";
    public static final String BUDGET = "budget";
    public static final String TAGLINE = "tagline";
    public static final String VIDEO = "videos";
    public static final String CREDITS = "credits";
    public static final String CAST = "cast";
    public static final String CREW = "crew";
    public static final String BELONGS_TO_COLLECTION = "belongs_to_collection";

    public static final String REVIEWS = "reviews";
    public static final String AUTHOR = "author";
    public static final String CONTENT = "content";
    //Tags unique for TV
    public static final String TV_NAME = "original_name";
    public static final String TV_AIR_DATE = "first_air_date";
    public static final String TV_STATUS = "status";

    //GENRES ARRAY
    public static final String GENRE = "genres";
    public static final String GENRE_ID = "id";
    public static final String NAME = "name";
    public static final String TAG_LANGUAGE = "original_language";


}
