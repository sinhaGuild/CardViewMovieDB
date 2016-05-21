package com.example.cardviewdemo;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.mxn.soul.slidingcard_core.ContainerView;
import com.mxn.soul.slidingcard_core.SlidingCard;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private List<String> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.person_detail);
        sliderShow = (SliderLayout) findViewById(R.id.slider);

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

            setPersonDetailPage(cast);
        }

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
