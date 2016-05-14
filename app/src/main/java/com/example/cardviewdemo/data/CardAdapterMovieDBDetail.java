package com.example.cardviewdemo.data;

import android.content.Context;

/**
 * Created by anuragsinha on 16-05-13.
 */
public class CardAdapterMovieDBDetail {

    MovieDBItemDetail item;
    Context context;
    int movieID;

    public CardAdapterMovieDBDetail(Context context, MovieDBItemDetail item) {
        this.context = context;
        this.item = item;
    }
}
