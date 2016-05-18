package com.example.cardviewdemo.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.cardviewdemo.CardViewDetailActivity;
import com.example.cardviewdemo.CustomVolleyRequest;
import com.example.cardviewdemo.R;

import java.util.List;

/**
 * Created by anuragsinha on 16-05-07.
 */
public class CardAdapterMovieDB extends RecyclerView.Adapter<CardAdapterMovieDB.ViewHolder> {

    //List of movieDBAdapter for CardView list
    public static final String TAG = "CardAdapterMovieDB Class ";
    List<MovieDBAdapter> movieDBAdapter;
    int[] movieID;
    //ParallaxViewController parallax = new ParallaxViewController();

    private ImageLoader imageLoader;
    private Context context;

    //Query type = Movie, TV
    private String dBType;


    public CardAdapterMovieDB(List<MovieDBAdapter> movieDBAdapter, String dBType, Context context) {
        super();
        //Getting all the cards
        this.movieDBAdapter = movieDBAdapter;
        this.context = context;
        this.dBType = dBType;
        if (movieDBAdapter != null) {
            if (movieDBAdapter.size() != 0) {
                movieID = new int[movieDBAdapter.size()];
                for (int i = 0; i < movieDBAdapter.size(); i++) {
                    this.movieID[i] = movieDBAdapter.get(i).getMovie_id();
                }
            } else {
                Toast.makeText(context, " is empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "is Null", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_card_new2, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        //parallax.imageParallax(viewHolder.poster_path);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Initialize Card List
        MovieDBAdapter movieDBAdapter1 = movieDBAdapter.get(position);
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(movieDBAdapter1.getPoster_path(), ImageLoader.getImageListener(holder.poster_path, R.drawable.loading, android.R.drawable.ic_dialog_alert));
        imageLoader.get(movieDBAdapter1.getBackdrop_path(), ImageLoader.getImageListener(holder.backdrop_path, R.drawable.loading, android.R.drawable.ic_dialog_alert));

        //Set default image if the API return is null
        holder.poster_path.setErrorImageResId(R.drawable.update);
        holder.backdrop_path.setErrorImageResId(R.drawable.update);

        //Set all other attributes
        holder.poster_path.setImageUrl(movieDBAdapter1.getPoster_path(), imageLoader);
        holder.backdrop_path.setImageUrl(movieDBAdapter1.getPoster_path(), imageLoader);
        holder.original_title.setText(movieDBAdapter1.getOriginalTitle());
        holder.vote_average.setText(movieDBAdapter1.getVote_average());
        holder.release_date.setText(movieDBAdapter1.getReleaseDate());

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //parallax.registerImageParallax(recyclerView);
    }

    @Override
    public int getItemCount() {
        return movieDBAdapter.size();
    }

    /**
     * Remove Movie card from RecyclerView
     *
     * @param position
     */

    public void removeMovieCard(int position) {
        movieDBAdapter.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView poster_path;
        public NetworkImageView backdrop_path;
        public TextView original_title;
        public TextView popularity;
        public TextView release_date;
        public TextView language;
        public TextView overview;
        public TextView vote_average;
        public WebView video_view;
        Typeface tp = Typeface.createFromAsset(context.getAssets(), "fonts/segoeuil.ttf");


        public ViewHolder(final View itemView) {
            super(itemView);
            poster_path = (NetworkImageView) itemView.findViewById(R.id.poster_path);
            backdrop_path = (NetworkImageView) itemView.findViewById(R.id.backdrop_path);

            //Set background Image to Grey
            setImageToGreyScale(poster_path);

            //Recycler onClick event
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), CardViewDetailActivity.class);
                    int position = getAdapterPosition();
                    int movie_id = movieID[position];
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("movie_id", String.valueOf(movie_id));
                    bundle.putSerializable("DBType", String.valueOf(dBType));
                    intent.putExtras(bundle);
//                    intent.putExtra("movie_id", String.valueOf(movie_id));
//                    intent.putExtra("DBType", String.valueOf(dBType));
                    v.getContext().startActivity(intent);
                }
            });

            original_title = (TextView) itemView.findViewById(R.id.original_title);
            vote_average = (TextView) itemView.findViewById(R.id.vote_average);
            release_date = (TextView) itemView.findViewById(R.id.release_date);

        }

        //Convert ImageView to greyscale
        public void setImageToGreyScale(NetworkImageView img) {
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            img.setColorFilter(filter);
        }
    }
}
