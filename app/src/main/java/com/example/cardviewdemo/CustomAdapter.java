package com.example.cardviewdemo;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Integer>{

	private ArrayList<Integer> images;
	private LayoutInflater inflater;
	Typeface tp = Typeface.createFromAsset(getContext().getAssets(), "fonts/segoeuil.ttf");
	
	public CustomAdapter(Context c, ArrayList<Integer> imgs) {
		super(c, R.layout.single_card, imgs);
		images = imgs;
		inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View vi = convertView;
		if(convertView == null) {
			vi = inflater.inflate(R.layout.single_card, parent, false);
		}
		
		TextView title = (TextView) vi.findViewById(R.id.original_title);
		title.setTypeface(tp);
		ImageView imgView = (ImageView) vi.findViewById(R.id.some_image);
		
		title.setText("Number " + (position + 1));
		imgView.setImageResource(images.get(position));
		
		return vi;
	}

}
