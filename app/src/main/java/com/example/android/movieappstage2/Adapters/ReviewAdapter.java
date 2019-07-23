package com.example.android.movieappstage2.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.movieappstage2.*;
import com.example.android.movieappstage2.MovieInformation.MovieReview;
import com.example.android.movieappstage2.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Szymon on 29.11.2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MovieViewHolder> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();


    private ArrayList<MovieReview> mMovieReviews;

    private Context mContext;

//Constructor for the class, the way details are passed to the constructor


    public ReviewAdapter(ArrayList<MovieReview> item, Context context) {

        mMovieReviews = item;
        mContext = context;

    }

//    On click interface to be implemented in the activity


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MovieViewHolder(view);
    }

    //   Bind data to the inflated views
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        holder.revierUserId.setText(mMovieReviews.get(position).getreviewerId());
        holder.reviewText.setText(mMovieReviews.get(position).getreviewText());
    }

    //    Number of grids
    @Override
    public int getItemCount() {
        int a;

        if (mMovieReviews != null && !mMovieReviews.isEmpty()) {

            a = mMovieReviews.size();
        } else {

            a = 0;
        }

        Log.v("theATag", String.valueOf(a));
        return a;
    }


    public MovieReview getItem(int id) {
        return mMovieReviews.get(id);

    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView revierUserId;
        TextView reviewText;


        MovieViewHolder(View itemView) {
            super(itemView);
            revierUserId = (TextView) itemView.findViewById(R.id.review_user_tv);
            reviewText = (TextView) itemView.findViewById(R.id.review_text_tv);
        }
    }
}
