package com.example.android.movieappstage2.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.movieappstage2.Constants;
import com.example.android.movieappstage2.MovieInformation.MovieItem;
import com.example.android.movieappstage2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import android.view.View.OnClickListener;
/**
 * Created by Szymon on 29.11.2018.
 */

public class RecViewMovieAdapter extends RecyclerView.Adapter<RecViewMovieAdapter.MovieViewHolder> {

    private static final String TAG = RecViewMovieAdapter.class.getSimpleName();

   final private GridViewClickListener mOnClickListener;

    private int mNumberItems;

    private MovieItem mItem;

    private ArrayList<MovieItem> mMovieItems;

    private Context mContext;

//Constructor for the class, the way details are passed to the constructor

    public RecViewMovieAdapter(ArrayList<MovieItem> item, Context context, GridViewClickListener listener) {

        mMovieItems = item;
        mContext = context;
//        mNumberItems = mMovieItems.size();
        mOnClickListener = listener;
    }

//    On click interface to be implemented in the activity

    public interface GridViewClickListener {
        void onGridItemClick(int clickedItemIndex);
    }

    //    Inflate the singe views to be used for the app
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.grid_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MovieViewHolder(view);
    }

    //   Bind data to the inflated views
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

//        assert mItem != null;
        Picasso.with(mContext).load(Constants.IMDB_IMAGE_STRING + mMovieItems.get(position).getImageAddress()).fit().into(holder.myImageView);

    }


    @Override
    public int getItemCount() {
        int a ;

        if(mMovieItems != null && !mMovieItems.isEmpty()) {

            a = mMovieItems.size();
        }
        else {

            a = 0;

        }

        return a;
    }


    // stores and recycles views as they are scrolled off screen

    public class MovieViewHolder extends RecyclerView.ViewHolder implements OnClickListener {


        ImageView myImageView;

        MovieViewHolder(View itemView) {
            super(itemView);
            myImageView = itemView.findViewById(R.id.posterImageItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onGridItemClick(clickedPosition);
        }
    }

    public MovieItem getItem(int id) {
        return mMovieItems.get(id);

    }
}


