package com.example.android.movieappstage2;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.GridView;

import com.example.android.movieappstage2.Adapters.FavoriteMovieAdapter;
import com.example.android.movieappstage2.Database.Movie;
import com.example.android.movieappstage2.Database.MovieDatabase;
import com.example.android.movieappstage2.Database.MovieRepository;
import com.example.android.movieappstage2.Database.MovieViewModel;

import java.util.List;

/**
 * Created by Szymon on 05.12.2018.
 */

public class FavoriteActivity extends AppCompatActivity implements FavoriteMovieAdapter.GridViewClickListener {

    private NetworkInfo networkInfo;
    private GridView gridView;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MovieViewModel movieViewModel;
    private LiveData<List<Movie>> movies;
    private MovieDatabase mDB;
    private MovieRepository movieRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int orientation = getResources().getConfiguration().orientation;
        setContentView(R.layout.activity_main);
        movieRepository = new MovieRepository(getApplication());

        mRecyclerView = (RecyclerView) findViewById(R.id.rv);

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(), 3));
        } else {

            mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(), 2));
        }

        final FavoriteMovieAdapter fAdapter = new FavoriteMovieAdapter(this, this);
        mRecyclerView.setAdapter(fAdapter);
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                fAdapter.setMovies(movies);
            }
        });

    }

    @Override
    public void onGridItemClick(int clickedItemIndex) {

        Movie movie = movieRepository.getQueryMovie();
        Intent intent = new Intent(getApplicationContext(), MovieDetails.class);
        intent.putExtra("movieItemClickedDetailsFavorite", clickedItemIndex);
        startActivity(intent);
    }


}
