package com.example.cardviewdemo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cardviewdemo.data.CardAdapterMovieDB;
import com.example.cardviewdemo.data.ConfigList;
import com.example.cardviewdemo.data.MovieDBAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    //Creating a List of movies
    private List<MovieDBAdapter> listMovieDB;
    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our list
        listMovieDB = new ArrayList<>();

        //getting Json response and parsing it
        getData();

    }


    //This method will get data from the web api
    private void getData() {

        /**
         * Build the URL with variable value of page
         * http://api.themoviedb.org/3/movie/now_playing?api_key=f5ebdbf26f1f950bf415ff4c7d72c476";
         */
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").
                authority(ConfigList.DATA_URL).
                appendPath("3").
                appendPath("movie").
                appendPath(ConfigList.MDB_NOW_PLAYING).
                appendQueryParameter(ConfigList.API_KEY, ConfigList.API_KEY_VALUE).
                appendQueryParameter(ConfigList.PAGES, String.valueOf(1));
        Log.v("URL :", builder.build().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, builder.build().toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            parseData(jsonArray);
                            Log.v("Response is:", jsonArray.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            MovieDBAdapter movieDBAdapter = new MovieDBAdapter();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                movieDBAdapter.setPoster_path(json.getString(ConfigList.TAG_IMAGE_URL));
                movieDBAdapter.setMovie_id(json.getInt(ConfigList.MOVIE_ID));
                movieDBAdapter.setBackdrop_path(json.getString(ConfigList.TAG_BACKDROP));
                movieDBAdapter.setOriginalTitle(json.getString(ConfigList.TAG_TITLE));
                movieDBAdapter.setVote_average(json.getDouble(ConfigList.TAG_VOTER_RATING));
//				movieDBAdapter.setPopularity(json.getInt(ConfigList.TAG_POPULARITY));
//				movieDBAdapter.setLanguage(json.getString(ConfigList.TAG_LANGUAGE));
                movieDBAdapter.setReleaseDate(json.getString(ConfigList.TAG_REAL_RELEASE_DATE));
//				movieDBAdapter.setOverview(json.getString(ConfigList.TAG_OVERVIEW));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listMovieDB.add(movieDBAdapter);
        }

        //Finally initializing our adapter
        adapter = new CardAdapterMovieDB(listMovieDB, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}