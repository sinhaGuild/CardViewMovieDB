package com.example.cardviewdemo.dialogs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cardviewdemo.CardViewDetailActivity;
import com.example.cardviewdemo.R;
import com.example.cardviewdemo.TextViewPlus;
import com.example.cardviewdemo.config.ConfigItem;
import com.example.cardviewdemo.config.ConfigList;
import com.example.cardviewdemo.detail.Genre;
import com.example.cardviewdemo.detail.MovieDBItemDetail;
import com.example.cardviewdemo.detail.ProductionCompany;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;

/**
 * Created by anuragsinha on 16-06-08.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DialogFragmentLayout extends BlurDialogFragment {

    /**
     * Bundle key used to start the blur dialog with a given scale factor (float).
     */
    private static final String BUNDLE_KEY_DOWN_SCALE_FACTOR = "bundle_key_down_scale_factor";

    /**
     * Bundle key used to start the blur dialog with a given blur radius (int).
     */
    private static final String BUNDLE_KEY_BLUR_RADIUS = "bundle_key_blur_radius";

    /**
     * Bundle key used to start the blur dialog with a given dimming effect policy.
     */
    private static final String BUNDLE_KEY_DIMMING = "bundle_key_dimming_effect";

    /**
     * Bundle key used to start the blur dialog with a given debug policy.
     */
    private static final String BUNDLE_KEY_DEBUG = "bundle_key_debug_effect";
    String dbType = "";
    ProductionCompany[] prod = new ProductionCompany[100];
    Genre[] genre;
    MovieDBItemDetail movieDBItemDetail = new MovieDBItemDetail();
    private int mRadius;
    private float mDownScaleFactor;
    private boolean mDimming;
    private boolean mDebug;
    private ImageView mPosterPath;
    private TextViewPlus mTitle;
    private TextViewPlus mLanguage;
    private TextViewPlus mGenre;
    private TextViewPlus mTagline;
    private String mDBType;
    private String mMovieID;
    private RelativeLayout mRoot;
    private ImageView mMoreArrow;
    private TextViewPlus mRuntime;
    private TextViewPlus mRuntimeTitle;


    /**
     * Retrieve a new instance of the sample fragment.
     *
     * @param radius          blur radius.
     * @param downScaleFactor down scale factor.
     * @param dimming         dimming effect.
     * @param debug           debug policy.
     * @return well instantiated fragment.
     */
    public static DialogFragmentLayout newInstance(int radius,
                                                   float downScaleFactor,
                                                   boolean dimming,
                                                   boolean debug,
                                                   String mMovieID,
                                                   String mDBType) {
        DialogFragmentLayout fragment = new DialogFragmentLayout();
        Bundle args = new Bundle();

        args.putString("movieID", mMovieID);
        args.putString("DBType", mDBType);

        args.putInt(
                BUNDLE_KEY_BLUR_RADIUS,
                radius
        );
        args.putFloat(
                BUNDLE_KEY_DOWN_SCALE_FACTOR,
                downScaleFactor
        );
        args.putBoolean(
                BUNDLE_KEY_DIMMING,
                dimming
        );
        args.putBoolean(
                BUNDLE_KEY_DEBUG,
                debug
        );

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Bundle args = getArguments();
        mDBType = args.getString("DBType");
        mMovieID = args.getString("movieID");
        mRadius = args.getInt(BUNDLE_KEY_BLUR_RADIUS);
        mDownScaleFactor = args.getFloat(BUNDLE_KEY_DOWN_SCALE_FACTOR);
        mDimming = args.getBoolean(BUNDLE_KEY_DIMMING);
        mDebug = args.getBoolean(BUNDLE_KEY_DEBUG);
        setupFragment(mMovieID, mDBType);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment, null);

        mRoot = (RelativeLayout) view.findViewById(R.id.dialog_root);
        mPosterPath = (ImageView) view.findViewById(R.id.dialog_poster_path_person);
        mTitle = (TextViewPlus) view.findViewById(R.id.dialog_name);
        mLanguage = (TextViewPlus) view.findViewById(R.id.dialog_language);
        mGenre = (TextViewPlus) view.findViewById(R.id.dialog_genre);
        mTagline = (TextViewPlus) view.findViewById(R.id.dialog_tagline);
        mMoreArrow = (ImageView) view.findViewById(R.id.dialog_more);
        mRuntime = (TextViewPlus) view.findViewById(R.id.dialog_runtime);
        mRuntimeTitle = (TextViewPlus) view.findViewById(R.id.dialog_runtime_tv);
        mMoreArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CardViewDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("movie_id", mMovieID);
                bundle.putSerializable("DBType", mDBType);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

//        TextView label = ((TextView) view.findViewById(R.id.textView));
//        label.setMovementMethod(LinkMovementMethod.getInstance());
//        Linkify.addLinks(label, Linkify.WEB_URLS);
        builder.setView(view);
        return builder.create();
    }

    @Override
    protected boolean isDebugEnable() {
        return mDebug;
    }

    @Override
    protected boolean isDimmingEnable() {
        return mDimming;
    }

    @Override
    protected boolean isActionBarBlurred() {
        return true;
    }

    @Override
    protected float getDownScaleFactor() {
        return mDownScaleFactor;
    }

    @Override
    protected int getBlurRadius() {
        return mRadius;
    }


    public void setupFragment(String movieID, final String dbType) {
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(jsonObjectRequest);

    }

    //This method will parse json data
    private void parseJsonObject(JSONObject object, String dbType) {
        JSONArray jsonGenres;

        //Extract Genres
        try {
            jsonGenres = object.getJSONArray("genres");
            genre = new Genre[jsonGenres.length() + 1];
            for (int i = 0; i < jsonGenres.length(); i++) {
                JSONObject temp = jsonGenres.getJSONObject(i);
                String id = temp.getString("id");
                String name = temp.getString("name");
                genre[i] = new Genre(id, name);
            }
            movieDBItemDetail.setGenres(genre);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            movieDBItemDetail.setPoster_path(object.getString(ConfigItem.POSTER_PATH));
            movieDBItemDetail.setBackdrop_path(object.getString(ConfigItem.TAG_BACKDROP));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (dbType.equals(ConfigList.DATA_TYPE_MOVIES)) {
                movieDBItemDetail.setOriginal_title(object.getString(ConfigItem.TAG_TITLE));
                movieDBItemDetail.setTagline(object.getString(ConfigItem.TAGLINE));
                movieDBItemDetail.setRuntime(object.getString(ConfigItem.RUNTIME));
            } else {
                movieDBItemDetail.setOriginal_title(object.getString(ConfigItem.TV_NAME));
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

        Picasso.with(getActivity()).
                load(movieDBItemDetail.getBackdrop_path()).
                into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Bitmap temp = toGrayscale(bitmap);
                        BitmapDrawable tempDrawable = new BitmapDrawable(getActivity().getResources(), temp);
                        //Set alpha. 0 is transparent, 255 is opaque
                        tempDrawable.setAlpha(150);
                        mRoot.setBackgroundDrawable(tempDrawable);
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


        //Other attributes
        if (mTitle != null) {
            mTitle.setText(item.getOriginal_title());
        }

        if (mTagline != null) {
            if (dbType.equals(ConfigList.DATA_TYPE_MOVIES)) {
                mRuntime.setText(item.getRuntime());
                if (item.getTagline().equals("")) {
                    mTagline.setVisibility(View.GONE);
                } else {
                    mTagline.setText(item.getTagline());
                }
            } else {
                mRuntime.setVisibility(View.GONE);
                mRuntimeTitle.setVisibility(View.GONE);
            }
        } else {
            String tag_line_tv = getString(R.string.tv_status_tagline) + item.getTagline();
            mTagline.setText(tag_line_tv);
        }


        Picasso.with(getActivity()).
                load(item.getPoster_path()).
                placeholder(R.drawable.placeholder).
                error(R.drawable.face_tired).
                into(mPosterPath);

        if (mGenre != null) {
            mGenre.setText(item.getGenreString());
        }
        if (mLanguage != null) {
            mLanguage.setText(item.getOriginal_language());
        }
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


    //Build image URL
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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
