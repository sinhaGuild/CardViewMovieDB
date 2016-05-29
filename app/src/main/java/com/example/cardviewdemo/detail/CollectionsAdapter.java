package com.example.cardviewdemo.detail;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cardviewdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anuragsinha on 16-05-28.
 */
public class CollectionsAdapter extends ArrayAdapter<CollectionsObject> {

    Context mContext;
    int layoutResourceId;
    List<CollectionsObject> collections = new ArrayList<CollectionsObject>();

    public CollectionsAdapter(Context context, int resource, List<CollectionsObject> objects) {
        super(context, resource, objects);

        mContext = context;
        layoutResourceId = resource;
        collections = objects;
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }
        // object item based on the position
        CollectionsObject objectItem = collections.get(position);
        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem = (TextView) convertView.findViewById(R.id.collections_details_title);
        TextView collections_text = (TextView) convertView.findViewById(R.id.collections_details_text);
        ImageView collections_image = (ImageView) convertView.findViewById(R.id.collections_details_image);

        return convertView;

    }


}
