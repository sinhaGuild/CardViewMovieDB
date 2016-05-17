package com.example.cardviewdemo;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cardviewdemo.data.CardAdapterMovieDB;
import com.example.cardviewdemo.data.ConfigList;
import com.example.cardviewdemo.data.MovieDBAdapter;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Page limit
    public static final int pageLimit = 5;
    Toolbar toolbar;
    //Exit by back twice
    boolean doubleBackToExitPressedOnce = false;
    //Creating a List of movies
    private List<MovieDBAdapter> listMovieDB;
    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    //FAB stuff
    private FABToolbarLayout fabToolBar;
    private View one, two, three, four;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setItemAnimator(new ScaleInAnimator());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //Init our list
        listMovieDB = new ArrayList<>();
        //getting Json response and parsing it
        getData(pageLimit, ConfigList.MDB_NOW_PLAYING);

        //FAB onClick init
        fab = (FloatingActionButton) findViewById(R.id.fabtoolbar_fab);

//        //Vector animation for toolbar button
//        Drawable drawable = fab.getDrawable();
//        if (drawable instanceof Animatable){
//            ((Animatable)drawable).start();
//        }


        fabToolBar = (FABToolbarLayout) findViewById(R.id.fabtoolbar);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);


        //Fabtoolbar menu 1 Upcoming
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initializing our list
                listMovieDB = new ArrayList<>();
                fabToolBar.hide();
                getData(pageLimit, ConfigList.MDB_UPCMONING);
            }
        });


        //Fabtoolbar menu 2 Popular
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initializing our list
                listMovieDB = new ArrayList<>();
                fabToolBar.hide();
                getData(pageLimit, ConfigList.MDB_POPULAR);
            }
        });

        //Fabtoolbar menu 3 Now playing
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listMovieDB = new ArrayList<>();
                fabToolBar.hide();
                getData(pageLimit, ConfigList.MDB_NOW_PLAYING);
            }
        });

        //Fabtoolbar menu 4 Latest
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listMovieDB = new ArrayList<>();
                fabToolBar.hide();
                getData(pageLimit, ConfigList.MDB_TOP_RATED);
            }
        });


        //Fab Button click
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabToolBar.show();
            }
        });

    }


    //Hide FAB on back button press
    public void onBackPressed() {
        fabToolBar.hide();
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    @Override
    public void onClick(View v) {
        Toast.makeText(this, "You press that button ??", Toast.LENGTH_SHORT).show();
    }

    //This method will get data from the web api
    private void getData(final int uptil, final String fetchFromMovieDBSortedBy) {

        /**
         * Build the URL with variable value of page
         * http://api.themoviedb.org/3/movie/now_playing?api_key=f5ebdbf26f1f950bf415ff4c7d72c476";
         */
        final String builtURL = buildURLByPage(1, fetchFromMovieDBSortedBy);
        Log.v("URL :", builtURL);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, builtURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        try {
                            int totalPages = Integer.parseInt(response.getString("total_pages"));
                            for (int i = 1; i < uptil; i++) {
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                                        buildURLByPage(i, fetchFromMovieDBSortedBy),
                                        null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    JSONArray jsonArray = response.getJSONArray("results");
                                                    parseData(jsonArray);
                                                    Log.v("Response is:", jsonArray.toString());
                                                    Log.d("Response", response.toString());
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
                                        });
                                //Creating request queue
                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                //Adding request to the queue
                                requestQueue.add(jsonObjectRequest);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
            }
        });
        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //Adding request to the queue
        requestQueue.add(jsonObjectRequest);
    }


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

    public String buildURLByPage(int uptilPage, String fetchFromMovieDBSortedBy) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").
                authority(ConfigList.DATA_URL).
                appendPath("3").
                appendPath("movie").
                appendPath(fetchFromMovieDBSortedBy).
                appendQueryParameter(ConfigList.API_KEY, ConfigList.API_KEY_VALUE).
                appendQueryParameter(ConfigList.PAGES, String.valueOf(uptilPage));
        return builder.build().toString();
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

        //Animator
        SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(adapter);
        alphaAdapter.setDuration(1000);
//        //Change Interpolater
        alphaAdapter.setInterpolator(new OvershootInterpolator(0.5f));

        //Adding adapter to recyclerview
        recyclerView.setAdapter(alphaAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}