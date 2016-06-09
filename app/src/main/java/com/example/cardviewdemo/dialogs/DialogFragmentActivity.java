package com.example.cardviewdemo.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.cardviewdemo.R;

/**
 * Created by anuragsinha on 16-06-08.
 */
public class DialogFragmentActivity extends Activity implements View.OnClickListener {

    String mDBType;
    String mMovieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity);

        //get MovieID from Intent extras and pass
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mDBType = (String) bundle.getSerializable("DBType");
            mMovieID = (String) bundle.getSerializable("movie_id");
        } else {
            Toast.makeText(this, "Intent did not pass movie ID", Toast.LENGTH_SHORT).show();
        }

        DialogFragmentLayout fragment
                = DialogFragmentLayout.newInstance(8, 3, true, false, mMovieID, mDBType);
        fragment.show(getFragmentManager(), "blur_sample");
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
