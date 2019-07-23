package com.example.android.movieappstage2.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.movieappstage2.Database.Movie;
import com.example.android.movieappstage2.MovieInformation.MovieItem;
import com.example.android.movieappstage2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Szymon on 05.12.2018.
 */

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieHolder> {

    final private GridViewClickListener mOnClickListener;

    private Context mContext;

    private List<Movie> movies = new ArrayList<>();

// Creating a constructor allowing a listener
    public FavoriteMovieAdapter(GridViewClickListener listener, Context context) {

        mContext = context;
        mOnClickListener = listener;
    }

    // Creating the object
    @NonNull
    @Override
    public FavoriteMovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new FavoriteMovieHolder(view);
    }
//Binding data to the holder

    @Override
    public void onBindViewHolder(@NonNull FavoriteMovieHolder holder, int position) {

        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185" + movies.get(position).getImage()).fit().into(holder.myImageView);

    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {

        this.movies = movies;
        notifyDataSetChanged();
    }

//    Getting the number of items to be drawn

    @Override
    public int getItemCount() {
        int a;

        if (movies != null && !movies.isEmpty()) {

            a = movies.size();
        } else {

            a = 0;

        }

        return a;
    }


    public interface GridViewClickListener {
        void onGridItemClick(int clickedItemIndex);
    }

    class FavoriteMovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView myImageView;

        FavoriteMovieHolder(View itemView) {
            super(itemView);
            myImageView = itemView.findViewById(R.id.posterImageItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = movies.get(getAdapterPosition()).getMovieId();
            mOnClickListener.onGridItemClick(clickedPosition);
        }
    }

}
