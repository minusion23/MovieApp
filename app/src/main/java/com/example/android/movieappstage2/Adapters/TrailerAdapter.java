package com.example.android.movieappstage2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.movieappstage2.MovieInformation.MovieVideo;
import com.example.android.movieappstage2.R;

import java.util.ArrayList;

/**
 * Created by Szymon on 02.12.2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MovieVideoHolder> {


    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private int mNumberItems;

    private ArrayList<MovieVideo> mMovieVideos;

    private Context mContext;

    public TrailerAdapter(ArrayList<MovieVideo> item, Context context) {

        mMovieVideos = item;
        mContext = context;
        if (mMovieVideos != null) {
            mNumberItems = mMovieVideos.size();
        }
    }


    @Override
    public TrailerAdapter.MovieVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MovieVideoHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.MovieVideoHolder holder, int position) {

        holder.trailerTV.setText(mContext.getString(R.string.trailerTVbase) + String.valueOf(position));

    }

    //    Number of videos
    @Override
    public int getItemCount() {

        int a;

        if (mMovieVideos != null && !mMovieVideos.isEmpty()) {

            a = mMovieVideos.size();
        } else {

            a = 0;

        }

        return a;
    }


    public class MovieVideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageButton playImageButton;
        private TextView trailerTV;

        MovieVideoHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ImageButton playImageButton = itemView.findViewById(R.id.play_button_trailer);
            playImageButton.setOnClickListener(this);
            trailerTV = itemView.findViewById(R.id.Trailer_number_tv);

        }

        @Override
        public void onClick(View v) {
            Context mContext = itemView.getContext();
            Intent openVideoIntent = new Intent();
            mMovieVideos.get(getAdapterPosition()).getmTrailerLink();
            openVideoIntent.setAction(Intent.ACTION_VIEW);
            openVideoIntent.setData(Uri.parse(mMovieVideos.get(getAdapterPosition()).getmTrailerLink()));
            mContext.startActivity(openVideoIntent);

        }
    }

}
