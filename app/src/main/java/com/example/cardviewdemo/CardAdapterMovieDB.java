package com.example.cardviewdemo;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by anuragsinha on 16-05-07.
 */
public class CardAdapterMovieDB extends RecyclerView.Adapter<CardAdapterMovieDB.ViewHolder>{

    private ImageLoader imageLoader;
    private Context context;
    //List of movieDBAdapter
    List<MovieDBAdapter> movieDBAdapter;

    public CardAdapterMovieDB(List<MovieDBAdapter> movieDBAdapter, Context context){
        super();
        //Getting all the superheroes
        this.movieDBAdapter = movieDBAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_card_new2, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MovieDBAdapter movieDBAdapter1 =  movieDBAdapter.get(position);

        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(movieDBAdapter1.getPoster_path(), ImageLoader.getImageListener(holder.poster_path, R.drawable.loading, android.R.drawable.ic_dialog_alert));
        imageLoader.get(movieDBAdapter1.getBackdrop_path(), ImageLoader.getImageListener(holder.backdrop_path, R.drawable.loading, android.R.drawable.ic_dialog_alert));

        holder.poster_path.setImageUrl(movieDBAdapter1.getPoster_path(), imageLoader);
        holder.backdrop_path.setImageUrl(movieDBAdapter1.getBackdrop_path(), imageLoader);
        holder.original_title.setText(movieDBAdapter1.getOriginalTitle());
        holder.vote_average.setText(movieDBAdapter1.getVote_average());
//        holder.popularity.setText(String.valueOf(movieDBAdapter1.getPopularity()));
        holder.release_date.setText(movieDBAdapter1.getReleaseDate());
//        holder.language.setText(movieDBAdapter1.getLanguage());
//        holder.overview.setText(movieDBAdapter1.getOverview());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return movieDBAdapter.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public NetworkImageView poster_path;
        public NetworkImageView backdrop_path;
        public TextView original_title;
        public TextView popularity;
        public TextView release_date;
        public TextView language;
        public TextView overview;
        public TextView vote_average;
        Typeface tp = Typeface.createFromAsset(context.getAssets(),"fonts/segoeuil.ttf");

        public ViewHolder(View itemView) {
            super(itemView);
            poster_path = (NetworkImageView) itemView.findViewById(R.id.poster_path);
            backdrop_path = (NetworkImageView) itemView.findViewById(R.id.backdrop_path);
            original_title = (TextView) itemView.findViewById(R.id.original_title);
            vote_average = (TextView) itemView.findViewById(R.id.vote_average);
//            popularity = (TextView) itemView.findViewById(R.id.popularity);
            release_date = (TextView) itemView.findViewById(R.id.release_date);
//            language = (TextView) itemView.findViewById(R.id.original_language);
//            overview = (TextView) itemView.findViewById(R.id.overview2);
//            original_title.setTypeface(tp);
//            popularity.setTypeface(tp);
        }
    }
}
