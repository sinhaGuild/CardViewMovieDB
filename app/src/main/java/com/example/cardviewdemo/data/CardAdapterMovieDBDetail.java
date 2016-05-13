package com.example.cardviewdemo.data;

import android.content.Context;

import java.util.List;

/**
 * Created by anuragsinha on 16-05-13.
 */
public class CardAdapterMovieDBDetail {

    List<MovieDBItemDetail> list;
    Context context;
    int movieID;

    public CardAdapterMovieDBDetail(Context context, List<MovieDBItemDetail> list, int movieID) {
        this.context = context;
        this.list = list;
        this.movieID = movieID;
    }
}
