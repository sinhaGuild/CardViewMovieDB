package com.example.cardviewdemo.lists;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cardviewdemo.CardViewDetailActivity;
import com.example.cardviewdemo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by anuragsinha on 16-05-07.
 */
public class CardAdapterMovieDB extends RecyclerView.Adapter<CardAdapterMovieDB.ViewHolder> {

    //List of movieDBObject for CardView list
    public static final String TAG = "CardAdapterMovieDB Class ";
    List<MovieDBObject> movieDBObject;
    int[] movieID;
    //ParallaxViewController parallax = new ParallaxViewController();

    private Context context;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    //Query type = Movie, TV
    private String dBType;


    public CardAdapterMovieDB(List<MovieDBObject> movieDBObject, String dBType, Context context) {
        super();
        //Getting all the cards
        this.movieDBObject = movieDBObject;
        this.context = context;
        this.dBType = dBType;
        if (movieDBObject != null) {
            if (movieDBObject.size() != 0) {
                movieID = new int[movieDBObject.size()];
                for (int i = 0; i < movieDBObject.size(); i++) {
                    this.movieID[i] = movieDBObject.get(i).getMovie_id();
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
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //parallax.registerImageParallax(recyclerView);
    }

    @Override
    public int getItemCount() {
        return movieDBObject.size();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Initialize Card List
        MovieDBObject movieDBObject1 = movieDBObject.get(position);

        //Set poster path
        Picasso.with(context).
                load(movieDBObject1.getPoster_path()).
                placeholder(R.drawable.placeholder).
                error(R.drawable.face_tired).
                into(holder.poster_path);

        //Set backdrop path
        Picasso.with(context).
                load(movieDBObject1.getBackdrop_path()).
                placeholder(R.drawable.placeholder).
                error(R.drawable.face_tired).
                into(holder.backdrop_path);

        holder.original_title.setText(movieDBObject1.getOriginalTitle());
        holder.vote_average.setText(movieDBObject1.getVote_average());
        holder.release_date.setText(movieDBObject1.getReleaseDate());

        setAnimation(holder.container, position);
    }

    /**
     * Animate list as its loading while scrolling down
     *
     * @param viewToAnimate
     * @param position
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.scroll_down);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        holder.clearAnimation();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView poster_path;
        public ImageView backdrop_path;
        public TextView original_title;
        public TextView release_date;
        public TextView language;
        public TextView vote_average;
        public LinearLayout container;

        public ViewHolder(final View itemView) {
            super(itemView);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            //Recycler onClick event
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), CardViewDetailActivity.class);
                    int movie_id = movieID[getAdapterPosition()];
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("movie_id", String.valueOf(movie_id));
                    bundle.putSerializable("DBType", String.valueOf(dBType));
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });

            poster_path = (ImageView) itemView.findViewById(R.id.poster_path);
            backdrop_path = (ImageView) itemView.findViewById(R.id.backdrop_path);

            //Set background Image to Grey
            setImageToGreyScale(poster_path);

            original_title = (TextView) itemView.findViewById(R.id.original_title);
            vote_average = (TextView) itemView.findViewById(R.id.vote_average);
            release_date = (TextView) itemView.findViewById(R.id.release_date);

        }

        /**
         * Remove Movie card from RecyclerView
         *
         * @param position
         */

        public void removeMovieCard(int position) {
            movieDBObject.remove(position);
            notifyItemRemoved(position);
        }

        //Convert ImageView to greyscale
        public void setImageToGreyScale(ImageView img) {
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
            img.setColorFilter(filter);
        }

        public void clearAnimation() {
            container.clearAnimation();
        }
    }
}
