package com.example.cardviewdemo;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.GridView;
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
import com.example.cardviewdemo.data.Cast;
import com.example.cardviewdemo.data.ConfigItem;
import com.example.cardviewdemo.data.ConfigList;
import com.example.cardviewdemo.data.Crew;
import com.example.cardviewdemo.data.Genre;
import com.example.cardviewdemo.data.GridViewAdapter;
import com.example.cardviewdemo.data.GridViewDetail;
import com.example.cardviewdemo.data.MovieDBItemDetail;
import com.example.cardviewdemo.data.ProductionCompany;
import com.example.cardviewdemo.data.Videos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anuragsinha on 16-05-13.
 */
public class CardViewDetailActivity extends AppCompatActivity {

    ProductionCompany[] prod = new ProductionCompany[100];
    Genre[] genre;
    Videos[] videos;

    Cast[] cast;
    Crew[] crew;
    ArrayList<GridViewDetail> castListAdapter;

    String movieID = null;
    WebView displayYoutubeVideo;
    GridView castAndCrew;
    GridViewAdapter castAndCrewAdapter;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail_view);
        displayYoutubeVideo = (WebView) findViewById(R.id.video_view);
        castAndCrew = (GridView) findViewById(R.id.gridView);
        castListAdapter = new ArrayList<>();

        //get MovieID from Intent extras and pass
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String movie_id_temp = bundle.getString("movie_id");
            movieID = movie_id_temp;
        } else {
            Toast.makeText(this, "Intent did not pass movie ID", Toast.LENGTH_SHORT).show();
        }

        getDataForMovieDetail(movieID);

    }


    public List<GridViewDetail> getDataForCastCrew(String movieID) {

        return null;
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
                appendQueryParameter(ConfigList.API_KEY, ConfigList.API_KEY_VALUE).
                appendQueryParameter(ConfigList.VIDEO, "videos,credits");
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
        JSONArray jsonProductionCompanies;
        JSONArray jsonGenres;
        JSONArray jsonVideoArray;
        JSONArray jsonCast;
        JSONArray jsonCrew;
        try {
            //Extract Cast
            jsonCast = object.getJSONObject(ConfigItem.CREDITS).getJSONArray(ConfigItem.CAST);
            cast = new Cast[jsonCast.length() + 1];
            for (int i = 0; i < jsonCast.length(); i++) {
                JSONObject tempcast = jsonCast.getJSONObject(i);
                String character = tempcast.getString("character");
                String name = tempcast.getString("name");
                String profilePath = tempcast.getString("profile_path");
                cast[i] = new Cast(character, name, profilePath);
                castListAdapter.add(new GridViewDetail(buildURL(profilePath), name));
            }

            //Extract Crew
            jsonCrew = object.getJSONObject(ConfigItem.CREDITS).getJSONArray(ConfigItem.CREW);
            crew = new Crew[jsonCrew.length() + 1];
            for (int i = 0; i < jsonCrew.length(); i++) {
                JSONObject tempcrew = jsonCrew.getJSONObject(i);
                String job = tempcrew.getString("job");
                String profilePath = tempcrew.getString("profile_path");
                String name = tempcrew.getString("name");
                crew[i] = new Crew(job, profilePath, name);
            }

            //Extract videos
            jsonVideoArray = object.getJSONObject(ConfigItem.VIDEO).getJSONArray(ConfigItem.RESULTS);
            videos = new Videos[jsonVideoArray.length() + 1];
            for (int i = 0; i < jsonVideoArray.length(); i++) {
                JSONObject temp = jsonVideoArray.getJSONObject(i);
                String id = temp.getString("id");
                String name = temp.getString("name");
                String key = temp.getString("key");
                videos[i] = new Videos(id, name, key);
            }

            //Extract production companies
            jsonProductionCompanies = object.getJSONArray("production_companies");
            prod = new ProductionCompany[jsonProductionCompanies.length() + 1];
            for (int i = 0; i < jsonProductionCompanies.length(); i++) {
                JSONObject temp = jsonProductionCompanies.getJSONObject(i);
                String id = temp.getString("id");
                String name = temp.getString("name");
                prod[i] = new ProductionCompany(id, name);
            }


            //Extract Genres
            jsonGenres = object.getJSONArray("genres");
            genre = new Genre[jsonGenres.length() + 1];
            for (int i = 0; i < jsonGenres.length(); i++) {
                JSONObject temp = jsonGenres.getJSONObject(i);
                String id = temp.getString("id");
                String name = temp.getString("name");
                genre[i] = new Genre(id, name);
            }

            movieDBItemDetail.setProduction_companies(prod);
            movieDBItemDetail.setGenres(genre);
            movieDBItemDetail.setVideo(videos);

            //Cast & Crew
            movieDBItemDetail.setCast(cast);
            movieDBItemDetail.setCrew(crew);

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
        } catch (JSONException e1) {
            e1.printStackTrace();
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
        setImageToGreyScale(backdrop_path);
        WebView displayYoutubeVideo = (WebView) findViewById(R.id.video_view);

        //Grid View
        GridView grid = (GridView) findViewById(R.id.gridView);

        TextView original_title = (TextView) findViewById(R.id.original_title_detail);
        TextView tagline = (TextView) findViewById(R.id.tagline);
        TextView genre = (TextView) findViewById(R.id.genre_detail);
        TextView language = (TextView) findViewById(R.id.language_detail);
        TextView production_company = (TextView) findViewById(R.id.production_company_detail);
        TextView overview = (TextView) findViewById(R.id.overview_detail);

        //Make Textview scrollable
        overview.setMovementMethod(new ScrollingMovementMethod());

        //Adapter setting for Cast & Crew
        castAndCrewAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, castListAdapter);
        castAndCrew.setAdapter(castAndCrewAdapter);


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
        tagline.setText(item.getTagline());
        genre.setText(item.getGenreString());
        language.setText(item.getOriginal_language());
        production_company.setText(item.getProductionCompanyString());
        overview.setText(item.getOverview());


        //Video
        displayYoutubeVideo.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        //Webview Settings
        WebSettings webSettings = displayYoutubeVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        displayYoutubeVideo.setBackgroundColor(Color.TRANSPARENT);
        displayYoutubeVideo.loadData(item.buildYoutubeiFrame(), "text/html", "utf-8");
    }

    //Convert ImageView to greyscale
    public void setImageToGreyScale(NetworkImageView img) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        img.setColorFilter(filter);
    }


    //Build image UR
    public String buildURL(String path) {
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("http").authority(ConfigItem.TAG_IMAGE_URL_BUILDER).
                appendPath("t").
                appendPath("p").
                appendPath("w500").appendPath(path.substring(1));
        return uri.build().toString();
    }


    //Youtube Housekeeping - Close when back etc.

    @Override
    protected void onPause() {
        displayYoutubeVideo.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        displayYoutubeVideo.onResume();
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        displayYoutubeVideo.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        displayYoutubeVideo.destroy();
        displayYoutubeVideo = null;
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            displayYoutubeVideo.loadUrl("");
            displayYoutubeVideo.stopLoading();

            finish();

        }
        return super.onKeyDown(keyCode, event);
    }
}
