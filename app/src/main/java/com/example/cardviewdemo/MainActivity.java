package com.example.cardviewdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	//Creating a List of superheroes
	private List<MovieDBAdapter> listMovieDB;
	Toolbar toolbar;

	//Creating Views
	private RecyclerView recyclerView;
	private RecyclerView.LayoutManager layoutManager;
	private RecyclerView.Adapter adapter;
	private EditText user_input;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Initializing Views
		recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		recyclerView.setHasFixedSize(true);
		layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(this.toolbar);

		//Initializing our superheroes list
		listMovieDB = new ArrayList<>();

//		//Initialize user input
//		user_input = (EditText) findViewById(R.id.user_input);

		//Calling method to get data
		getData();
	}

//	private Button.OnClickListener getEditViewClickListner = new Button.OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			EditText editText = (EditText) findViewById(R.id.user_input);
//			CharSequence editTextValue = editText.getText().toString();
//			setTitle(editTextValue);
//
//		}
//	};


	//This method will get data from the web api
	private void getData(){
		//Showing a progress dialog
//		final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Config.DATA_URL, null,
				new Response.Listener<JSONObject>()
				{
					@Override
					public void onResponse(JSONObject response) {
						// display response
						Log.d("Response", response.toString());
						try {
							JSONArray jsonArray = response.getJSONArray("results");
							parseData(jsonArray);
							Log.v("Response is:", jsonArray.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				},
				new Response.ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d("Error.Response", error.toString());
					}
				}
		);

//		//Creating a json array request
//		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL,
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

		//Creating request queue
		RequestQueue requestQueue = Volley.newRequestQueue(this);

		//Adding request to the queue
		requestQueue.add(jsonObjectRequest);
//		requestQueue.add(jsonArrayRequest);
	}

	//This method will parse json data
	private void parseData(JSONArray array){
		for(int i = 0; i<array.length(); i++) {
			MovieDBAdapter movieDBAdapter = new MovieDBAdapter();
			JSONObject json = null;
			try {
				json = array.getJSONObject(i);
				movieDBAdapter.setPoster_path(json.getString(Config.TAG_IMAGE_URL));
				movieDBAdapter.setBackdrop_path(json.getString(Config.TAG_BACKDROP));
				movieDBAdapter.setOriginalTitle(json.getString(Config.TAG_TITLE));
				movieDBAdapter.setVote_average(json.getInt(Config.TAG_VOTER_RATING));
//				movieDBAdapter.setPopularity(json.getInt(Config.TAG_POPULARITY));
//				movieDBAdapter.setLanguage(json.getString(Config.TAG_LANGUAGE));
				movieDBAdapter.setReleaseDate(json.getString(Config.TAG_REAL_RELEASE_DATE));
//				movieDBAdapter.setOverview(json.getString(Config.TAG_OVERVIEW));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			listMovieDB.add(movieDBAdapter);
		}

		//Finally initializing our adapter
		adapter = new CardAdapterMovieDB(listMovieDB, this);

		//Adding adapter to recyclerview
		recyclerView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main,menu);
		return true;
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		int id = item.getItemId();
//		if (id == R.id.action_MovieDB){
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}


}