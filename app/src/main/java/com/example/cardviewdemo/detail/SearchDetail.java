package com.example.cardviewdemo.detail;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Filter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cardviewdemo.config.ConfigItem;
import com.example.cardviewdemo.config.ConfigList;
import com.example.cardviewdemo.config.ConfigSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anuragsinha on 16-05-25.
 */
public class SearchDetail {

    public static final String TAG = "SearchDetail Class";
    public static final int numberOfSearchResults = 5;
    private static String queryID;
    private static List<SearchSuggestionsMovieDB> suggestions = new ArrayList<>();
    private static SearchDetail instance = null;

    public static void findByQuery(Context context, final String queryID, final OnFindResultsListener listener) {
        /**
         * get Search results
         *
         */
        String url = buildURLForSearch(queryID);
        Log.v("URL :", url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        parseSuggestions(response, queryID, listener);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        //Adding request to the queue
        requestQueue.add(jsonObjectRequest);

    }

    private static void parseSuggestions(JSONObject response, String queryID, final OnFindResultsListener listener) {

        JSONArray results;
        JSONObject searchResults;
        String name;
        String backdrop_path;
        String media_type;
        String id;

        try {
            results = response.getJSONArray("results");
            if (response != null && results != null) {

                for (int i = 0; i < results.length(); i++) {
//                for (int i = 0; i < numberOfSearchResults; i++) {
                    searchResults = results.getJSONObject(i);
                    name = searchResults.getString(ConfigSearch.NAME);
                    backdrop_path = buildImageURL(searchResults.getString(ConfigSearch.BACKDROP));
                    media_type = searchResults.getString(ConfigSearch.MEDIA_TYPE);
                    id = searchResults.getString("id");
                    Log.v(TAG, "Name " + name + "\n" + backdrop_path);
                    suggestions.add(new SearchSuggestionsMovieDB(backdrop_path, name, media_type, id));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                results.values = suggestions;
                results.count = suggestions.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (listener != null)
                    listener.onResults((List<SearchSuggestionsMovieDB>) results.values);
            }
        }.filter(queryID);
    }

    public static String buildURLForSearch(String queryID) {
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("http").
                authority(ConfigList.DATA_URL).
                appendPath("3").
                appendPath("search").
                appendPath("multi").
                appendQueryParameter(ConfigList.API_KEY, ConfigList.API_KEY_VALUE).
                appendQueryParameter(ConfigSearch.QUERY, queryID).
                appendQueryParameter(ConfigSearch.INCLUDE_ADULT, "false");
        return uri.build().toString();
    }

    public static String buildImageURL(String path) {
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("http").authority(ConfigItem.TAG_IMAGE_URL_BUILDER).
                appendPath("t").
                appendPath("p").
                appendPath("w500").appendPath(path.substring(1));
        return uri.build().toString();
    }

    public String getQueryID() {
        return queryID;
    }

//    public static List<SearchSuggestionsMovieDB> getHistory(Context context, int count){
//
//        initColorWrapperList(context);
//
//        List<ColorSuggestion> suggestionList = new ArrayList<>();
//
//        ColorSuggestion colorSuggestion;
//        for(int i=0; i<count; i++){
//            colorSuggestion = new ColorSuggestion(sColorWrappers.get(i));
//            colorSuggestion.setIsHistory(true);
//            suggestionList.add(colorSuggestion);
//        }
//
//        return suggestions;
//    }

    public void setQueryID(String queryID) {
        SearchDetail.queryID = queryID;
    }

    public interface OnFindResultsListener {

        void onResults(List<SearchSuggestionsMovieDB> results);
    }
}