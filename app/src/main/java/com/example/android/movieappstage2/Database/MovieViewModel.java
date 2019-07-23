package com.example.android.movieappstage2.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Szymon on 04.12.2018.
 */
//View model implemented for favorites
public class MovieViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;

    private LiveData<List<Movie>> allMovies;

    private Movie movie;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        allMovies = movieRepository.getAllMovies();

    }

    public void insert(Movie movie) {
        movieRepository.insert(movie);
    }

    public void update(Movie movie) {
        movieRepository.update(movie);
    }

    public void delete(Movie movie) {
        movieRepository.delete(movie);
    }

    public Movie getMovie(int movieID) {
        Movie movie = movieRepository.GetMovieItem(movieID);
        return movie;
    }




    public LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }
}
