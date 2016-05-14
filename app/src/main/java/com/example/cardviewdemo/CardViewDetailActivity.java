package com.example.cardviewdemo;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
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

/**
 * Created by anuragsinha on 16-05-13.
 */
public class CardViewDetailActivity extends AppCompatActivity {

    MovieDBItemDetail movieDBItemDetailList;
    CardAdapterMovieDBDetail adapter;
    ProductionCompany[] prod = new ProductionCompany[100];
    Genre[] genre = new Genre[100];
    String movieID = null;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail_view);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String movie_id_temp = bundle.getString("movie_id");
            movieID = movie_id_temp;
        } else {
            Toast.makeText(this, "Intent did not pass movie ID", Toast.LENGTH_SHORT).show();
        }
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
//            movieDBItemDetail.setVote_average(object.getDouble(ConfigItem.TAG_VOTER_RATING));
//            movieDBItemDetail.setPopularity(object.getInt(ConfigItem.TAG_POPULARITY));
            movieDBItemDetail.setOriginal_language(object.getString(ConfigItem.TAG_LANGUAGE));
            movieDBItemDetail.setRelease_date(object.getString(ConfigItem.TAG_REAL_RELEASE_DATE));
            movieDBItemDetail.setOverview(object.getString(ConfigItem.TAG_OVERVIEW));
            movieDBItemDetail.setTagline(object.getString(ConfigItem.TAGLINE));
//            movieDBItemDetail.setRevenue(object.getString(ConfigItem.BUDGET));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setCardDetail(movieDBItemDetail);

//        //Finally initializing our adapter
//        adapter = new CardAdapterMovieDB(listMovieDB, this);
//
//        //Adding adapter to recyclerview
//        recyclerView.setAdapter(adapter);
    }

    public void setCardDetail(MovieDBItemDetail item) {

        NetworkImageView poster_path = (NetworkImageView) findViewById(R.id.poster_path_detail);
        NetworkImageView backdrop_path = (NetworkImageView) findViewById(R.id.backdrop_path_detail);
        TextView original_title = (TextView) findViewById(R.id.original_title_detail);
//        TextView release_date = (TextView) findViewById(R.id.release_date_detail);
//        TextView vote_average = (TextView) findViewById(R.id.vote_average_detail);
        TextView tagline = (TextView) findViewById(R.id.tagline);
        TextView genre = (TextView) findViewById(R.id.genre_detail);
        TextView language = (TextView) findViewById(R.id.language_detail);
//        TextView runtime = (TextView) findViewById(R.id.runtime_detail);
        TextView production_company = (TextView) findViewById(R.id.production_company_detail);
//        TextView budget = (TextView) findViewById(R.id.budget_detail);
//        TextView revenue = (TextView) findViewById(R.id.revenue_detail);
        TextView overview = (TextView) findViewById(R.id.overview_detail);

        imageLoader = CustomVolleyRequest.getInstance(this).getImageLoader();
        imageLoader.get(item.getPoster_path(), ImageLoader.getImageListener(poster_path, R.drawable.loading, android.R.drawable.ic_dialog_alert));
        imageLoader.get(item.getBackdrop_path(), ImageLoader.getImageListener(backdrop_path, R.drawable.loading, android.R.drawable.ic_dialog_alert));


        //Set default image if the API return is null
        poster_path.setErrorImageResId(R.drawable.update);
        backdrop_path.setErrorImageResId(R.drawable.update);

        //Set all other attributes
        poster_path.setImageUrl(item.getPoster_path(), imageLoader);
        backdrop_path.setImageUrl(item.getBackdrop_path(), imageLoader);
        original_title.setText(item.getOriginal_title());
//        release_date.setText(item.getRelease_date());
//        vote_average.setText(item.getVote_average());
        tagline.setText(item.getTagline());
        genre.setText(item.getGenreString());
        language.setText(item.getOriginal_language());
//        runtime.setText(item.getRuntime());
        production_company.setText(item.getProductionCompanyString());
//        budget.setText(item.getBudget());
//        revenue.setText(item.getRevenue());
        overview.setText(item.getOverview());

    }

}
