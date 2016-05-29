package com.example.cardviewdemo;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.example.cardviewdemo.config.ConfigItem;
import com.example.cardviewdemo.config.ConfigList;
import com.example.cardviewdemo.config.ConfigPerson;
import com.example.cardviewdemo.detail.Cast;
import com.example.cardviewdemo.detail.CastThumb;
import com.example.cardviewdemo.detail.CrewThumb;
import com.example.cardviewdemo.detail.GridViewAdapter;
import com.example.cardviewdemo.detail.GridViewDetail;
import com.mxn.soul.slidingcard_core.ContainerView;
import com.mxn.soul.slidingcard_core.SlidingCard;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anuragsinha on 16-05-20.
 */
public class PersonDetailActivity extends AppCompatActivity implements ContainerView.ContainerInterface {

    public static final String TAG_PERSON = "PersonDetailActivity";
    String castID;
    String crewID;
    Cast cast;
    SliderLayout sliderShow;
    CastThumb[] castThumb;
    CrewThumb[] crewThumb;
    ArrayList<GridViewDetail> castList;
    ArrayList<GridViewDetail> crewList;
    GridView castGridView;
    GridView crewGridView;
    GridViewAdapter castAdapter;
    GridViewAdapter crewAdapter;
    private List<String> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.person_detail);
        sliderShow = (SliderLayout) findViewById(R.id.slider);

        castGridView = (GridViewPlus) findViewById(R.id.gridView_castedin);
        crewGridView = (GridViewPlus) findViewById(R.id.gridView_crewin);

        castList = new ArrayList<>();
        crewList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String type = (String) bundle.getSerializable("type");
            if (type.equals("cast")) {
                castID = (String) bundle.getSerializable("castID");
                Log.v(TAG_PERSON, "Passed by intent " + castID);
                getPersonDetail(castID, "cast");
            } else {
                crewID = (String) bundle.getSerializable("crewID");
                getPersonDetail(crewID, "crew");
            }
        } else {
            Toast.makeText(this, "Intent did not pass castThumb & crewThumb ID", Toast.LENGTH_SHORT).show();
        }

    }

    public void getPersonDetail(final String personID, final String personType) {

        /**
         * Build the URL with variable value of page
         * http://api.themoviedb.org/3/person/8783?api_key=f5ebdbf26f1f950bf415ff4c7d72c476&append_to_response=combined_credits,images
         */
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").
                authority(ConfigItem.DATA_URL).
                appendPath("3").
                appendPath("person").
                appendPath(personID).
                appendQueryParameter(ConfigList.API_KEY, ConfigList.API_KEY_VALUE).
                appendQueryParameter(ConfigPerson.APPEND, "combined_credits,images");
        Log.v(TAG_PERSON, "URL :" + builder.build().toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, builder.build().toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        parseJsonObject(response, personType);
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

    private void parseJsonObject(JSONObject response, String personType) {

        cast = new Cast();
        JSONObject jsonImageOb;
        JSONArray jsonImages;
        images = cast.getCastImages();
        JSONArray jsonCast;
        JSONArray jsonCrew;

        if (response != null) {

            try {
                cast.setName(response.getString(ConfigPerson.NAME));
                cast.setBiography(response.getString(ConfigPerson.BIOGRAPHY));
                cast.setBirthday(response.getString(ConfigPerson.BIRTHDAY));
                cast.setPlaceOfBirth(response.getString(ConfigPerson.PLACE_OF_BIRTH));
                Log.v(TAG_PERSON, "Poster " + buildURL(response.getString(ConfigPerson.PROFILE_PATH)));
                cast.setProfilePath(buildURL(response.getString(ConfigPerson.PROFILE_PATH)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (response.getJSONObject(ConfigPerson.IMAGES).getJSONArray(ConfigPerson.PROFILES) != null) {
                    jsonImages = response.getJSONObject(ConfigPerson.IMAGES).getJSONArray(ConfigPerson.PROFILES);
                    for (int i = 0; i < jsonImages.length(); i++) {
                        jsonImageOb = jsonImages.getJSONObject(i);
                        images.add(buildURL(jsonImageOb.getString(ConfigPerson.PERSON_POSTER_PATH)));
                        Log.v(TAG_PERSON, images.get(i) + " added.");
                    }
                    cast.setCastImages(images);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //Extract CastThumb
            try {
                jsonCast = response.getJSONObject(ConfigPerson.COMBINED_CREDITS).getJSONArray(ConfigItem.CAST);
                castThumb = new CastThumb[jsonCast.length() + 1];
                for (int i = 0; i < jsonCast.length(); i++) {
                    JSONObject tempcast = jsonCast.getJSONObject(i);
                    String media_type = tempcast.getString("media_type");
                    if (media_type.equals(ConfigList.DATA_TYPE_MOVIES)) {
                        String character = tempcast.getString("character");
                        String name = tempcast.getString("original_title");
                        String profilePath = tempcast.getString("poster_path");
                        String castID = tempcast.getString("id");
                        castThumb[i] = new CastThumb(character, name, profilePath, castID, media_type);
                        castList.add(new GridViewDetail(buildURL(profilePath), name));
                    } else {
                        String character = tempcast.getString("character");
                        String name = tempcast.getString("name");
                        String profilePath = tempcast.getString("poster_path");
                        String castID = tempcast.getString("id");
                        castThumb[i] = new CastThumb(character, name, profilePath, castID, media_type);
                        castList.add(new GridViewDetail(buildURL(profilePath), name));
                    }
                }

                //Extract CrewThumb
                jsonCrew = response.getJSONObject(ConfigPerson.COMBINED_CREDITS).getJSONArray(ConfigItem.CREW);
                crewThumb = new CrewThumb[jsonCrew.length() + 1];
                for (int i = 0; i < jsonCrew.length(); i++) {
                    JSONObject tempcrew = jsonCrew.getJSONObject(i);
                    String media_type = tempcrew.getString("media_type");
                    if (media_type.equals(ConfigList.DATA_TYPE_MOVIES)) {
                        String job = tempcrew.getString("job");
                        String profilePath = tempcrew.getString("poster_path");
                        String name = tempcrew.getString("original_title");
                        String crewID = tempcrew.getString("id");
                        crewThumb[i] = new CrewThumb(job, profilePath, name, crewID, media_type);
                        crewList.add(new GridViewDetail(buildURL(profilePath), name));

                    } else {
                        String job = tempcrew.getString("job");
                        String profilePath = tempcrew.getString("poster_path");
                        String name = tempcrew.getString("name");
                        String crewID = tempcrew.getString("id");
                        crewThumb[i] = new CrewThumb(job, profilePath, name, crewID, media_type);
                        crewList.add(new GridViewDetail(buildURL(profilePath), name));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setPersonDetailPage(cast);
            setPersonDetailBio(crewList, castList);
        }

    }

    private void setPersonDetailBio(ArrayList<GridViewDetail> crewList, ArrayList<GridViewDetail> castList) {

        //Adapter setting for CastThumb & CrewThumb
        castAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, castList);
        castGridView.setAdapter(castAdapter);
        castGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), CardViewDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("movie_id", String.valueOf(castThumb[position].getCastId()));
                bundle.putSerializable("DBType", String.valueOf(castThumb[position].getMedia_type()));
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        crewAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, crewList);
        crewGridView.setAdapter(crewAdapter);
        crewGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), CardViewDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("movie_id", String.valueOf(crewThumb[position].getCrewID()));
                bundle.putSerializable("DBType", String.valueOf(crewThumb[position].getMedia_type()));
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });


    }


    private void setPersonDetailPage(Cast cast) {

        ImageView person_poster = (ImageView) findViewById(R.id.poster_path_person);
        TextViewPlus name = (TextViewPlus) findViewById(R.id.name_person);
        TextViewPlus age = (TextViewPlus) findViewById(R.id.age_person);
        TextViewPlus birth_country = (TextViewPlus) findViewById(R.id.born_in_person);
        TextViewPlus bio = (TextViewPlus) findViewById(R.id.bio_person);

        Picasso.with(this).load(cast.getProfilePath()).into(person_poster);
        name.setText(cast.getName());
        age.setText(cast.getBirthday());
        birth_country.setText(cast.getPlaceOfBirth());
        bio.setText(cast.getBiography());


        for (int i = 0; i < images.size(); i++) {
            DefaultSliderView sliderView = new DefaultSliderView(this);
            Log.v(TAG_PERSON, "Image " + i + " " + images.get(i));
            sliderView.
                    image(images.get(i)).
                    setScaleType(BaseSliderView.ScaleType.Fit);
            sliderShow.addSlider(sliderView);
        }
        sliderShow.setPresetTransformer(SliderLayout.Transformer.FlipHorizontal);
        sliderShow.setDuration(4000);
    }

    @Override
    protected void onStop() {
        sliderShow.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void initCard(SlidingCard card, int index) {
        ImageView mImageView = (ImageView) card.findViewById(R.id.sliding_card_poster_path);
        TextView mTextView = (TextView) card.findViewById(R.id.sliding_card_poster_title);
        if (cast.getCastImages().get(index) != null) {
            mTextView.setText("BLANK");
            Log.v(TAG_PERSON, "InitCard " + cast.getCastImages().get(index));
            Picasso.with(this).load(cast.getCastImages().get(index)).into(mImageView);
        }

    }

    @Override
    public void exChangeCard() {
        if (images != null) {
            String first = images.get(0);
            images.remove(0);
            images.add(first);
        }

    }


    //Build image URL
    public String buildURL(String path) {
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("http").authority(ConfigItem.TAG_IMAGE_URL_BUILDER).
                appendPath("t").
                appendPath("p").
                appendPath("w500").appendPath(path.substring(1));
        return uri.build().toString();
    }

    //Convert ImageView to greyscale
    public void setImageToGreyScale(ImageView img) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        img.setColorFilter(filter);
    }

}
