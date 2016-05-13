package com.example.cardviewdemo;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cardviewdemo.data.CardAdapterMovieDBDetail;
import com.example.cardviewdemo.data.ConfigItem;
import com.example.cardviewdemo.data.ConfigList;
import com.example.cardviewdemo.data.Genre;
import com.example.cardviewdemo.data.MovieDBItemDetail;
import com.example.cardviewdemo.data.ProductionCompany;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anuragsinha on 16-05-13.
 */
public class CardViewDetailActivity extends AppCompatActivity {

    List<MovieDBItemDetail> movieDBItemDetailList;
    CardAdapterMovieDBDetail adapter;
    ProductionCompany[] prod;
    Genre[] genre;
    String movieID = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail_view);
        movieDBItemDetailList = new ArrayList<>();

        getDataForMovieDetail(movieID);
    }
    //Get Data and Set Data here

    public void getDataForMovieDetail(String movieID) {
        /**
         * Build the URL with variable value of page
         * http://api.themoviedb.org/3/movie/now_playing?api_key=f5ebdbf26f1f950bf415ff4c7d72c476";
         */
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").
                authority(ConfigItem.DATA_URL).
                appendPath("3").
                appendPath("movie").
                appendPath(movieID).
                appendQueryParameter(ConfigList.API_KEY, ConfigList.API_KEY_VALUE);
        Log.v("URL :", builder.build().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, builder.build().toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        parseJsonObject(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(jsonObjectRequest);


//		//Creating a json array request
//		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(ConfigList.DATA_URL,
//				new Response.Listener<JSONArray>() {
//					@Override
//					public void onResponse(JSONArray response) {
//						//Dismissing progress dialog
//						loading.dismiss();
//
//						//calling method to parse json array
//						parseData(response);
//					}
//				},
//				new Response.ErrorListener() {
//					@Override
//					public void onErrorResponse(VolleyError error) {
//
//					}
//				});

//		requestQueue.add(jsonArrayRequest);
    }

    //This method will parse json data
    private void parseJsonObject(JSONObject object) {
        MovieDBItemDetail movieDBItemDetail = new MovieDBItemDetail();
        JSONArray jsonProductionCompanies = null;
        JSONArray jsonGenres = null;
        try {

            //Extract production companies
            jsonProductionCompanies = object.getJSONArray("production_companies");
            for (int i = 0; i < jsonProductionCompanies.length(); i++) {
                JSONObject temp = jsonProductionCompanies.getJSONObject(i);
                String id = temp.getString("id");
                String name = temp.getString("name");
                prod[i] = new ProductionCompany(id, name);
            }


            //Extract Genres
            jsonGenres = object.getJSONArray("genres");
            for (int i = 0; i < jsonGenres.length(); i++) {
                JSONObject temp = jsonGenres.getJSONObject(i);
                String id = temp.getString("id");
                String name = temp.getString("name");
                genre[i] = new Genre(id, name);
            }

            movieDBItemDetail.setProduction_companies(prod);
            movieDBItemDetail.setGenres(genre);
            movieDBItemDetail.setPoster_path(object.getString(ConfigItem.TAG_IMAGE_URL));
            movieDBItemDetail.setBackdrop_path(object.getString(ConfigItem.TAG_BACKDROP));
            movieDBItemDetail.setOriginal_title(object.getString(ConfigItem.TAG_TITLE));
            movieDBItemDetail.setVote_average(object.getDouble(ConfigItem.TAG_VOTER_RATING));
            movieDBItemDetail.setPopularity(object.getInt(ConfigItem.TAG_POPULARITY));
            movieDBItemDetail.setOriginal_language(object.getString(ConfigItem.TAG_LANGUAGE));
            movieDBItemDetail.setRelease_date(object.getString(ConfigItem.TAG_REAL_RELEASE_DATE));
            movieDBItemDetail.setOverview(object.getString(ConfigList.TAG_OVERVIEW));
            movieDBItemDetail.setTagline(object.getString(ConfigItem.TAGLINE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        movieDBItemDetailList.add(movieDBItemDetail);

        adapter = new CardAdapterMovieDBDetail(this, movieDBItemDetailList, getTaskId());


//        //Finally initializing our adapter
//        adapter = new CardAdapterMovieDB(listMovieDB, this);
//
//        //Adding adapter to recyclerview
//        recyclerView.setAdapter(adapter);
    }

}
