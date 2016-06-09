package com.example.cardviewdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.example.cardviewdemo.detail.Reviews;
import com.example.cardviewdemo.detail.Videos;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    Reviews reviews = new Reviews();
    String movieID = null;
    WebView displayYoutubeVideo;
    GridViewPlus castGridView;
    GridViewPlus crewGridView;
    GridViewAdapter castAdapter;
    GridViewAdapter crewAdapter;
    MovieDBItemDetail movieDBItemDetail = new MovieDBItemDetail();
    List<Reviews> reviewsList;
    private boolean isCollections = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail_view);
        displayYoutubeVideo = (WebView) findViewById(R.id.trailer_video_view);
        castGridView = (GridViewPlus) findViewById(R.id.gridView_castedin);
        crewGridView = (GridViewPlus) findViewById(R.id.gridView_crewin);

        castList = new ArrayList<>();
        crewList = new ArrayList<>();
        reviewsList = new ArrayList<>();

        //get MovieID from Intent extras and pass
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            dbType = (String) bundle.getSerializable("DBType");
            movieID = (String) bundle.getSerializable("movie_id");
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
                appendQueryParameter(ConfigList.APPEND_TO_RESPONSE, "videos,credits,similar,reviews");
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

    }

    //This method will parse json data
    private void parseJsonObject(JSONObject object, String dbType) {
        JSONArray jsonProductionCompanies;
        JSONArray jsonGenres;
        JSONArray jsonVideoArray;
        JSONArray jsonCast;
        JSONArray jsonReviews;
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

                //Reviews
                try {
                    jsonReviews = object.getJSONObject(ConfigItem.REVIEWS).getJSONArray(ConfigItem.RESULTS);
                    boolean isReview = false;
                    for (int i = 0; i < jsonReviews.length(); i++) {
                        JSONObject tempcast = jsonReviews.getJSONObject(i);
                        String author = tempcast.getString(ConfigItem.AUTHOR);
                        String content = tempcast.getString(ConfigItem.CONTENT);
                        reviewsList.add(new Reviews(author, content));
                        if (reviewsList.get(i) != null && !(isReview)) {
                            movieDBItemDetail.setReviews(reviewsList.get(i));
                            isReview = true;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

        setCardDetail(movieDBItemDetail, dbType);
    }

    public void setCardDetail(MovieDBItemDetail item, String dbType) {

        final LinearLayout cardviewRoot = (LinearLayout) findViewById(R.id.card_detail_root);

        ImageView poster_path = (ImageView) findViewById(R.id.poster_path_person);
////        ImageView backdrop_path = (ImageView) findViewById(R.id.backdrop_path_detail);
//        setImageToGreyScale(backdrop_path);

        //Collections
        ImageView collections_poster = (ImageView) findViewById(R.id.collections_poster);
        ImageView collections_backdrop = (ImageView) findViewById(R.id.collections_backdrop);
        setImageToGreyScale(collections_backdrop);

        //Reviews
        TextViewPlus review_tv = (TextViewPlus) findViewById(R.id.reviews_tv);
        TextViewPlus review_author = (TextViewPlus) findViewById(R.id.review_author);
        TextViewPlus review_detail = (TextViewPlus) findViewById(R.id.reviews_detail);

        WebView displayYoutubeVideo = (WebView) findViewById(R.id.trailer_video_view);

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


        //Reviews
        if (dbType.equals(ConfigList.DATA_TYPE_MOVIES)) {
            if (item.getReviews() != null) {
                review_author.setText(item.getReviews().getAuthor());
                review_detail.setText(item.getReviews().getContent());
            } else {
                review_author.setVisibility(View.INVISIBLE);
                review_detail.setVisibility(View.INVISIBLE);
                review_tv.setVisibility(View.INVISIBLE);
            }
        }

        //Other attributes
        if (original_title != null) {
            original_title.setText(item.getOriginal_title());
        }
        if (tagline != null) {
            if (dbType.equals(ConfigList.DATA_TYPE_MOVIES)) {
                if (item.getTagline().equals("")) {
                    tagline.setVisibility(View.GONE);
                } else {
                    tagline.setText(item.getTagline());
                }
            }
        } else {
            String tag_line_tv = getString(R.string.tv_status_tagline) + item.getTagline();
            tagline.setText(tag_line_tv);
        }

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

//                //Transitions
//                View transitionView = castGridView;
//                String transitionName = getString(R.string.cast_transition);
//                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(CardViewDetailActivity.this,
//                        transitionView, transitionName);

                //Start intent
                startActivity(intent);
//                startActivity(intent, transitionActivityOptions.toBundle());
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


        //Reviews


        //Collections posters & attrs
        if (isCollections) {

            //Set Name
            if (collections_name != null) {
                collections_name.setText(item.getCollection().getName());
            }

            Picasso.with(this).
                    load(item.getCollection().getCollections_backdrop_path()).
                    placeholder(R.drawable.placeholder).
                    error(R.drawable.face_tired).
                    into(collections_backdrop);

            Picasso.with(this).
                    load(item.getCollection().getCollections_poster_path()).
                    placeholder(R.drawable.placeholder).
                    error(R.drawable.face_tired).
                    into(collections_poster);

            //Set onClick for Collections
            collections_backdrop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "COMING SOON!", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            collections_backdrop.setVisibility(View.INVISIBLE);
            collections_poster.setVisibility(View.INVISIBLE);
            collections_name.setVisibility(View.INVISIBLE);
            collections_error.setText("Oops! No Collections found.");
            collections_error.setVisibility(View.VISIBLE);
        }

        Picasso.with(this).
                load(item.getBackdrop_path()).
                into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Bitmap temp = toGrayscale(bitmap);
                        BitmapDrawable tempDrawable = new BitmapDrawable(getApplicationContext().getResources(), temp);
                        //Set alpha. 0 is transparent, 255 is opaque
                        tempDrawable.setAlpha(150);
                        cardviewRoot.setBackgroundDrawable(tempDrawable);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        Log.d("TAG", "ScrollView background Failed");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Log.d("TAG", "On Prepare Load");
                    }
                });

        Picasso.with(this).
                load(item.getPoster_path()).
                placeholder(R.drawable.placeholder).
                resize(poster_path.getWidth(), poster_path.getHeight()).
                error(R.drawable.face_tired).
                into(poster_path);

//        Picasso.with(this).
//                load(item.getBackdrop_path()).
//                error(R.drawable.face_tired).
//                into(backdrop_path);


        if (genre != null) {
            genre.setText(item.getGenreString());
        }
        if (language != null) {
            language.setText(item.getOriginal_language());
        }
        if (production_company != null) {
            production_company.setText(item.getProductionCompanyString());
        }
        overview.setText(item.getOverview());


        //Video
        if (displayYoutubeVideo != null) {
            displayYoutubeVideo.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }
            });
        }
        //Webview Settings
        WebSettings webSettings = displayYoutubeVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        displayYoutubeVideo.setBackgroundColor(Color.TRANSPARENT);
        displayYoutubeVideo.loadData(item.buildYoutubeiFrame(), "text/html", "utf-8");
    }

    //Convert ImageView to greyscale
    public void setImageToGreyScale(ImageView img) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        img.setColorFilter(filter);
    }

    //set bitmap to greyscale
    public Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
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
