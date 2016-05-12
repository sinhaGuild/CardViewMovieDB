package com.example.cardviewdemo;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

/**
 * Created by anuragsinha on 16-05-11.
 */
public class RecyclerCardListClickListener implements RecyclerView.OnItemTouchListener {


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
