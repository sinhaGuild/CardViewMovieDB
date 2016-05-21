package com.example.cardviewdemo;

import android.content.Intent;
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
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
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
import com.example.cardviewdemo.config.ConfigItem;
import com.example.cardviewdemo.config.ConfigList;
import com.example.cardviewdemo.detail.CastThumb;
import com.example.cardviewdemo.detail.Collections;
import com.example.cardviewdemo.detail.CrewThumb;
import com.example.cardviewdemo.detail.Genre;
import com.example.cardviewdemo.detail.GridViewAdapter;
import com.example.cardviewdemo.detail.GridViewDetail;
import com.example.cardviewdemo.detail.MovieDBItemDetail;
import com.example.cardviewdemo.detail.ProductionCompany;
import com.example.cardviewdemo.detail.Videos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by anuragsinha on 16-05-13.
 */
public class CardViewDetailActivity extends AppCompatActivity {

    String dbType = "";
    Collections collection = new Collections();
    ProductionCompany[] prod = new ProductionCompany[100];
    Genre[] genre;
    Videos[] videos;
    CastThumb[] castThumb;
    CrewThumb[] crewThumb;
    ArrayList<GridViewDetail> castList;
    ArrayList<GridViewDetail> crewList;
    String movieID = null;
    WebView displayYoutubeVideo;
    GridView castGridView;
    GridView crewGridView;
    GridViewAdapter castAdapter;
    GridViewAdapter crewAdapter;
    MovieDBItemDetail movieDBItemDetail = new MovieDBItemDetail();
    private boolean isCollections = true;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail_view);
        displayYoutubeVideo = (WebView) findViewById(R.id.video_view);
        castGridView = (GridViewPlus) findViewById(R.id.gridView_cast);
        crewGridView = (GridViewPlus) findViewById(R.id.gridView_crew);
        castList = new ArrayList<>();
        crewList = new ArrayList<>();

        //get MovieID from Intent extras and pass
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            dbType = (String) bundle.getSerializable("DBType");
            movieID = (String) bundle.getSerializable("movie_id");
//            String movie_id_temp = bundle.getString("movie_id");
//            dbType = bundle.getString("DBType");
//            movieID = movie_id_temp;
        } else {
            Toast.makeText(this, "Intent did not pass movie ID", Toast.LENGTH_SHORT).show();
        }

        getDataForMovieDetail(movieID, dbType);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //Get Data and Set Data here

    public void getDataForMovieDetail(String movieID, final String dbType) {
        /**
         * Build the URL with variable value of page
         * http://api.themoviedb.org/3/movie/now_playing?api_key=f5ebdbf26f1f950bf415ff4c7d72c476";
         */
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https").
                authority(ConfigItem.DATA_URL).
                appendPath("3").
                appendPath(dbType).
                appendPath(movieID).
                appendQueryParameter(ConfigList.API_KEY, ConfigList.API_KEY_VALUE).
                appendQueryParameter(ConfigList.VIDEO, "videos,credits,similar");
        Log.v("URL :", builder.build().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, builder.build().toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        parseJsonObject(response, dbType);
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
    private void parseJsonObject(JSONObject object, String dbType) {
        JSONArray jsonProductionCompanies;
        JSONArray jsonGenres;
        JSONArray jsonVideoArray;
        JSONArray jsonCast;
        JSONArray jsonCrew;
        JSONObject jsonCollections;


        //Extract Collections
        try {
            if (object.getJSONObject(ConfigItem.BELONGS_TO_COLLECTION) != null) {
                jsonCollections = object.getJSONObject(ConfigItem.BELONGS_TO_COLLECTION);
                collection.setCollections_backdrop_path(jsonCollections.getString(ConfigItem.TAG_BACKDROP));
                collection.setCollections_poster_path(jsonCollections.getString(ConfigItem.POSTER_PATH));
                collection.setName(jsonCollections.getString(ConfigItem.NAME));
                isCollections = true;
            } else {
                collection.setName("Oops! No Collections found.");
                isCollections = false;
            }
        } catch (JSONException e) {
            isCollections = false;
            e.printStackTrace();
        }


        try {

            //Extract CastThumb
            jsonCast = object.getJSONObject(ConfigItem.CREDITS).getJSONArray(ConfigItem.CAST);
            castThumb = new CastThumb[jsonCast.length() + 1];
            for (int i = 0; i < jsonCast.length(); i++) {
                JSONObject tempcast = jsonCast.getJSONObject(i);
                String character = tempcast.getString("character");
                String name = tempcast.getString("name");
                String profilePath = tempcast.getString("profile_path");
                String castID = tempcast.getString("id");
                castThumb[i] = new CastThumb(character, name, profilePath, castID);
                castList.add(new GridViewDetail(buildURL(profilePath), name));
            }

            //Extract CrewThumb
            jsonCrew = object.getJSONObject(ConfigItem.CREDITS).getJSONArray(ConfigItem.CREW);
            crewThumb = new CrewThumb[jsonCrew.length() + 1];
            for (int i = 0; i < jsonCrew.length(); i++) {
                JSONObject tempcrew = jsonCrew.getJSONObject(i);
                String job = tempcrew.getString("job");
                String profilePath = tempcrew.getString("profile_path");
                String name = tempcrew.getString("name");
                String crewID = tempcrew.getString("id");
                crewThumb[i] = new CrewThumb(job, profilePath, name, crewID);
                crewList.add(new GridViewDetail(buildURL(profilePath), name));
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
            movieDBItemDetail.setVideos(videos);

            //CastThumb & CrewThumb
            movieDBItemDetail.setCastThumb(castThumb);
            movieDBItemDetail.setCrewThumb(crewThumb);

            movieDBItemDetail.setPoster_path(object.getString(ConfigItem.POSTER_PATH));
            movieDBItemDetail.setBackdrop_path(object.getString(ConfigItem.TAG_BACKDROP));

            if (dbType.equals(ConfigList.DATA_TYPE_MOVIES)) {
                movieDBItemDetail.setOriginal_title(object.getString(ConfigItem.TAG_TITLE));
                movieDBItemDetail.setRelease_date(object.getString(ConfigItem.TAG_REAL_RELEASE_DATE));
                movieDBItemDetail.setTagline(object.getString(ConfigItem.TAGLINE));
                movieDBItemDetail.setCollection(collection);
            } else {
                movieDBItemDetail.setOriginal_title(object.getString(ConfigItem.TV_NAME));
                movieDBItemDetail.setRelease_date(object.getString(ConfigItem.TV_AIR_DATE));
                movieDBItemDetail.setTagline(object.getString(ConfigItem.TV_STATUS));
            }

            movieDBItemDetail.setOriginal_language(object.getString(ConfigItem.TAG_LANGUAGE));
            movieDBItemDetail.setOverview(object.getString(ConfigItem.TAG_OVERVIEW));

        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        setCardDetail(movieDBItemDetail);
    }

    public void setCardDetail(MovieDBItemDetail item) {

        NetworkImageView poster_path = (NetworkImageView) findViewById(R.id.poster_path_person);
        NetworkImageView backdrop_path = (NetworkImageView) findViewById(R.id.backdrop_path_detail);
        setImageToGreyScale(backdrop_path);
        WebView displayYoutubeVideo = (WebView) findViewById(R.id.video_view);

        //Collections
        NetworkImageView collections_poster = (NetworkImageView) findViewById(R.id.collections_poster);
        NetworkImageView collections_backdrop = (NetworkImageView) findViewById(R.id.collections_backdrop);
        setImageToGreyScale(collections_backdrop);
        collections_poster.setImageResource(R.drawable.error_default);
        collections_backdrop.setImageResource(R.drawable.error_default);
        TextViewPlus collections_name = (TextViewPlus) findViewById(R.id.collection_title);
        TextViewPlus collections_error = (TextViewPlus) findViewById(R.id.collections_error);

        TextView original_title = (TextView) findViewById(R.id.name_person);
        TextView tagline = (TextView) findViewById(R.id.tagline);
        TextView genre = (TextView) findViewById(R.id.age_person);
        TextView language = (TextView) findViewById(R.id.born_in_person);
        TextView production_company = (TextView) findViewById(R.id.production_company_detail);
        TextView overview = (TextView) findViewById(R.id.bio_person);

        //Make Textview scrollable
        overview.setMovementMethod(new ScrollingMovementMethod());

        //Adapter setting for CastThumb & CrewThumb
        castAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, castList);
        castGridView.setAdapter(castAdapter);
        castGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), PersonDetailActivity.class);
                Bundle bundle = new Bundle();
                Log.e("Person item click : ", String.valueOf(castThumb[position].getCastId()));
                bundle.putSerializable("castID", String.valueOf(castThumb[position].getCastId()));
                bundle.putSerializable("type", "cast");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        crewAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, crewList);
        crewGridView.setAdapter(crewAdapter);
        crewGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), PersonDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("crewID", String.valueOf(crewThumb[position].getCrewID()));
                bundle.putSerializable("type", "crew");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //Image loaders
        imageLoader = CustomVolleyRequest.getInstance(this).getImageLoader();
        imageLoader.get(item.getPoster_path(), ImageLoader.getImageListener(poster_path, R.drawable.loading, android.R.drawable.ic_dialog_alert));
        imageLoader.get(item.getBackdrop_path(), ImageLoader.getImageListener(backdrop_path, R.drawable.loading, android.R.drawable.ic_dialog_alert));

        //Collections posters & attrs
        if (isCollections) {
            imageLoader.get(item.getPoster_path(), ImageLoader.getImageListener(collections_backdrop, R.drawable.loading, android.R.drawable.ic_dialog_alert));
            imageLoader.get(item.getBackdrop_path(), ImageLoader.getImageListener(collections_poster, R.drawable.loading, android.R.drawable.ic_dialog_alert));
            collections_name.setText(item.getCollection().getName());
            collections_backdrop.setImageUrl(item.getCollection().getCollections_backdrop_path(), imageLoader);
            collections_poster.setImageUrl(item.getCollection().getCollections_poster_path(), imageLoader);
            collections_backdrop.setErrorImageResId(R.drawable.update);
            collections_poster.setErrorImageResId(R.drawable.update);
        } else {
            collections_backdrop.setVisibility(View.INVISIBLE);
            collections_poster.setVisibility(View.INVISIBLE);
            collections_name.setVisibility(View.INVISIBLE);
            collections_error.setText("Oops! No Collections found.");
            collections_error.setVisibility(View.VISIBLE);
        }

        //Set default image for all images if the API return is null
        poster_path.setErrorImageResId(R.drawable.update);
        backdrop_path.setErrorImageResId(R.drawable.update);


        //Set art attributes
        poster_path.setImageUrl(item.getPoster_path(), imageLoader);
        backdrop_path.setImageUrl(item.getBackdrop_path(), imageLoader);



        //Other attributes
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
