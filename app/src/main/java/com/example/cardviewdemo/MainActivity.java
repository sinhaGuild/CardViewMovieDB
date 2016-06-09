package com.example.cardviewdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.view.BodyTextView;
import com.arlib.floatingsearchview.util.view.IconImageView;
import com.example.cardviewdemo.config.ConfigList;
import com.example.cardviewdemo.config.ConfigSearch;
import com.example.cardviewdemo.detail.SearchDetail;
import com.example.cardviewdemo.detail.SearchSuggestionsMovieDB;
import com.example.cardviewdemo.lists.CardAdapterMovieDB;
import com.example.cardviewdemo.lists.MovieDBObject;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

public class MainActivity extends AppCompatActivity {

    //Page limit
    public static final int pageLimit = 5;
    //Video Background
    private final String TAG = "MainActivity";
    //Exit by back twice
    boolean doubleBackToExitPressedOnce = false;
    String mDBType;
    String mMovieID;
    //Creating a List of movies
    private List<MovieDBObject> listMovieDB;
    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    //FAB stuff
    private FABToolbarLayout fabToolBar;
    private View movie_toolbar, tv_toolbar, collections_toolbar, search_toolbar;
    private FloatingActionButton fab;
    //Search
    private FloatingSearchView mSearchView;
    private ViewGroup mParentView;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Initializing Search views
        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        mParentView = (ViewGroup) findViewById(R.id.search_parent_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.search_drawer_layout);
        setSearchListeners();

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Init our list
        listMovieDB = new ArrayList<>();
        //getting Json response and parsing it
        getData(pageLimit, ConfigList.MOVIES_TOP_RATED, ConfigList.DATA_TYPE_MOVIES);

        //FAB onClick init
        fab = (FloatingActionButton) findViewById(R.id.fabtoolbar_fab);
        fabToolBar = (FABToolbarLayout) findViewById(R.id.fabtoolbar);
        movie_toolbar = findViewById(R.id.one);
        tv_toolbar = findViewById(R.id.two);
        collections_toolbar = findViewById(R.id.three);
        search_toolbar = findViewById(R.id.four);

        //Hide fabToolbar if scroll event has happened
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    fabToolBar.hide();
                }
            }
        });

        //Fabtoolbar menu 1 Upcoming
        movie_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_simple);
                rotation.setDuration(1000);
                v.startAnimation(rotation);
                //Initializing our list
                listMovieDB = new ArrayList<>();
                //fabToolBar.hide();
                getData(pageLimit, ConfigList.MOVIES_UPCOMING, ConfigList.DATA_TYPE_MOVIES);
                fabToolBar.hide();
            }
        });


        //Fabtoolbar menu 2 Popular
        tv_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_simple);
                rotation.setDuration(500);
                v.startAnimation(rotation);
                //Initializing our list
                listMovieDB = new ArrayList<>();
                getData(pageLimit, ConfigList.TV_TOP_RATED, ConfigList.DATA_TYPE_TV);
                fabToolBar.hide();
            }
        });

        //Fabtoolbar menu 3 Now playing
        collections_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_simple);
                rotation.setDuration(500);
                v.startAnimation(rotation);
                listMovieDB = new ArrayList<>();
                getData(pageLimit, ConfigList.MOVIES_NOW_PLAYING, ConfigList.DATA_TYPE_MOVIES);
                fabToolBar.hide();
            }
        });

        //Fabtoolbar menu 4 Latest
        search_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation rotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_simple);
                rotation.setDuration(500);
                v.startAnimation(rotation);
                listMovieDB = new ArrayList<>();
                getData(pageLimit, ConfigList.MOVIES_TOP_RATED, ConfigList.DATA_TYPE_MOVIES);
                fabToolBar.hide();
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

    /**
     * SEARCH LISTENERS
     * setOnQueryChangeListener
     * setOnSearchListener
     * setOnFocusChangeListener
     * setOnMenuItemClickListener
     * setOnLeftMenuClickListener
     * setOnHomeActionClickListener
     * setOnBindSuggestionCallback
     * Drawer listeners for open, close etc.
     */

    private void setSearchListeners() {

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {

                    //this shows the top left circular progress
                    //you can call it where ever you want, but
                    //it makes sense to do it when loading something in
                    //the background.
                    mSearchView.showProgress();

                    //simulates a query call to a data source
                    //with a new query.
                    SearchDetail.findByQuery(MainActivity.this, newQuery, new SearchDetail.OnFindResultsListener() {

                        @Override
                        public void onResults(List<SearchSuggestionsMovieDB> results) {

                            //this will swap the data and
                            //render the collapse/expand animations as necessary
                            mSearchView.swapSuggestions(results);

                            //let the users know that the background
                            //process has completed
                            mSearchView.hideProgress();
                        }
                    });
                }

                Log.d(TAG, "onSearchTextChanged()");
            }
        });


        /**
         * On Suggestions click
         * if media_type is TV or movies - raise intent CardViewDetailActivity - it needs id
         */
        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

                SearchSuggestionsMovieDB searchSuggestionsMovieDB = (SearchSuggestionsMovieDB) searchSuggestion;

                if (searchSuggestion != null) {
                    Intent mIntent = new Intent(getApplicationContext(), CardViewDetailActivity.class);
                    Bundle extras = new Bundle();
                    switch (((SearchSuggestionsMovieDB) searchSuggestion).getMedia_type()) {
                        case ConfigSearch.MEDIA_TYPE_MOVIE:
                            extras.putSerializable("DBType", ConfigList.DATA_TYPE_MOVIES);
                            extras.putSerializable("movie_id", searchSuggestionsMovieDB.getId());
                            break;
                        case ConfigSearch.MEDIA_TYPE_TV:
                            extras.putSerializable("DBType", ConfigList.DATA_TYPE_TV);
                            extras.putSerializable("movie_id", searchSuggestionsMovieDB.getId());
                            break;
                        case ConfigSearch.MEDIA_TYPE_PERSON:
                            extras.putSerializable("type", "cast");
                            extras.putSerializable("castID", searchSuggestionsMovieDB.getId());
                            break;
                    }
                    mIntent.putExtras(extras);
                    startActivity(mIntent);
                }

                Log.d(TAG, "onSuggestionClicked()");

            }

            @Override
            public void onSearchAction() {

                Log.d(TAG, "onSearchAction()");
            }
        });

//        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
//            @Override
//            public void onFocus() {
//
//                //show suggestions when search bar gains focus (typically history suggestions)
//                mSearchView.swapSuggestions(SearchDetail.getHistory(MainActivity.this, 3));
//
//                Log.d(TAG, "onFocus()");
//            }
//
//            @Override
//            public void onFocusCleared() {
//
//                Log.d(TAG, "onFocusCleared()");
//            }
//        });

////        handle menu clicks the same way as you would
////        in a regular activity
//        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
//            @Override
//            public void onActionMenuItemSelected(MenuItem item) {
//
//                if (item.getItemId() == R.id.action_change_colors) {
//
//                    //demonstrate setting colors for items
//                    mSearchView.setBackgroundColor(Color.parseColor("#ECE7D5"));
//                    mSearchView.setViewTextColor(Color.parseColor("#657A81"));
//                    mSearchView.setHintTextColor(Color.parseColor("#596D73"));
//                    mSearchView.setActionMenuOverflowColor(Color.parseColor("#B58900"));
//                    mSearchView.setMenuItemIconColor(Color.parseColor("#2AA198"));
//                    mSearchView.setLeftActionIconColor(Color.parseColor("#657A81"));
//                    mSearchView.setClearBtnColor(Color.parseColor("#D30102"));
//                    mSearchView.setSuggestionRightIconColor(Color.parseColor("#BCADAD"));
//                    mSearchView.setDividerColor(Color.parseColor("#dfd7b9"));
//
//                } else {
//
//                    //just print action
//                    Toast.makeText(getApplicationContext(), item.getTitle(),
//                            Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

//        //use this listener to listen to menu clicks when app:floatingSearch_leftAction="showHamburger"
//        mSearchView.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
//            @Override
//            public void onMenuOpened() {
//                Log.d(TAG, "onMenuOpened()");
//
//                mDrawerLayout.openDrawer(GravityCompat.START);
//            }
//
//            @Override
//            public void onMenuClosed() {
//                Log.d(TAG, "onMenuClosed()");
//
//                mDrawerLayout.closeDrawer(GravityCompat.START);
//            }
//        });
//
//        //use this listener to listen to menu clicks when app:floatingSearch_leftAction="showHome"
//        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
//            @Override
//            public void onHomeClicked() {
//
//                Log.d(TAG, "onHomeClicked()");
//            }
//        });

        /*
         * Here you have access to the left icon and the text of a given suggestion
         * item when as it is bound to the suggestion list. You can utilize this
         * callback to change some properties of the left icon and the text. For example, you
         * can load left icon images using your favorite image loading library, or change text color.
         *
         * Some restrictions:
         * 1. You can modify the height, eidth, margin, or padding of the text and left icon.
         * 2. You can't modify the text's size.
         *
         * Modifications to these properties will be ignored silently.
         */
        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(IconImageView leftIcon, BodyTextView bodyText, SearchSuggestion item, int itemPosition) {

                SearchSuggestionsMovieDB searchSuggestions = (SearchSuggestionsMovieDB) item;

                if (searchSuggestions.ismIsHistory()) {
                    leftIcon.setImageDrawable(leftIcon.getResources().getDrawable(R.drawable.ic_history_black_24dp));
                    leftIcon.setAlpha(.36f);
                } else {
                    leftIcon.setScaleY(2);
                    Picasso.with(getApplicationContext()).load(searchSuggestions.getBackdrop_image()).into(leftIcon);
                }
                //leftIcon.setImageDrawable(new ColorDrawable(Color.parseColor(colorSuggestion.getColor().getHex())));
            }

        });

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {

                //since the drawer might have opened as a results of
                //a click on the left menu, we need to make sure
                //to close it right after the drawer opens, so that
                //it is closed when the drawer is  closed.
                mSearchView.closeMenu(false);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }


    /**
     * Search
     *
     * @param query
     */

    private void searchFor(String query) {
    }

    private void filterSearchFor(String query) {
    }


    //Hide FAB on back button press
    public void onBackPressed() {
        fabToolBar.hide();
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "One more BACK and I will QUIT", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    //This method will get data from the web api
    private void getData(final int page, final String fetContentSortedBy, final String dataType) {

        /**
         * Build the URL with variable value of page
         * http://api.themoviedb.org/3/movie/now_playing?api_key=f5ebdbf26f1f950bf415ff4c7d72c476";
         */
        final String builtURL = buildURLByPage(1, fetContentSortedBy, dataType);
        Log.v("URL :", builtURL);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, builtURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        try {
                            int totalPages = Integer.parseInt(response.getString("total_pages"));
                            for (int i = 1; i < page; i++) {
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                                        buildURLByPage(i, fetContentSortedBy, dataType),
                                        null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    JSONArray jsonArray = response.getJSONArray("results");
                                                    parseData(jsonArray, dataType);
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

    public String buildURLByPage(int uptilPage, String fetchFromMovieDBSortedBy, String dataType) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").
                authority(ConfigList.DATA_URL).
                appendPath("3").
                appendPath(dataType).
                appendPath(fetchFromMovieDBSortedBy).
                appendQueryParameter(ConfigList.API_KEY, ConfigList.API_KEY_VALUE).
                appendQueryParameter(ConfigList.PAGES, String.valueOf(uptilPage));
        return builder.build().toString();
    }


    //This method will parse json data
    private void parseData(JSONArray array, String dataType) {
        for (int i = 0; i < array.length(); i++) {
            MovieDBObject movieDBObject = new MovieDBObject();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                movieDBObject.setPoster_path(json.getString(ConfigList.TAG_IMAGE_URL));
                movieDBObject.setMovie_id(json.getInt(ConfigList.MOVIE_ID));
                movieDBObject.setBackdrop_path(json.getString(ConfigList.TAG_BACKDROP));

                //check if movies or TV
                if (dataType.equals(ConfigList.DATA_TYPE_MOVIES)) {
                    movieDBObject.setReleaseDate(json.getString(ConfigList.TAG_REAL_RELEASE_DATE));
                    movieDBObject.setOriginalTitle(json.getString(ConfigList.TAG_TITLE));
                } else {
                    movieDBObject.setReleaseDate(json.getString(ConfigList.TAG_FIRST_AIR_DATE));
                    movieDBObject.setOriginalTitle(json.getString(ConfigList.TV_NAME));
                }
                movieDBObject.setVote_average(json.getDouble(ConfigList.TAG_VOTER_RATING));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listMovieDB.add(movieDBObject);
        }

        //Finally initializing our adapter with the list of objects & pass the type of query type ie. Movies, TV
        adapter = new CardAdapterMovieDB(listMovieDB, dataType, this);

        //Animator
        SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(adapter);
        alphaAdapter.setDuration(100);
        alphaAdapter.setInterpolator(new OvershootInterpolator(1f));
        //Adding adapter to recyclerview
        recyclerView.setAdapter(alphaAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


}