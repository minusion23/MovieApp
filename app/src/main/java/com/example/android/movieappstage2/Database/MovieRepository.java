package com.example.android.movieappstage2.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.os.ConditionVariable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.android.movieappstage2.Adapters.ReviewAdapter;
import com.example.android.movieappstage2.MovieDetails;
import com.example.android.movieappstage2.MovieInformation.MovieReview;
import com.example.android.movieappstage2.QueryUtilities.QueryUtilsReview;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Szymon on 04.12.2018.
 */
//Class used as primary contact with the DAO/Database for the majority of instances
public class MovieRepository implements AsyncResult {

    private Movie queryMovie;

    private MovieDAO movieDAO;

    private LiveData<List<Movie>> allMovies;

    private Boolean isRunning = true;

    public MovieRepository(Application application)

    {
        MovieDatabase database = MovieDatabase.getInstance(application);
        movieDAO = database.movieDAO();
        allMovies = movieDAO.getAllMovies();
    }


    public Boolean getRunning() {
        return isRunning;
    }

    public Movie getQueryMovie() {
        return queryMovie;
    }

    public void insert(Movie movie) {
        new InsertMovieAsyncTask(movieDAO).execute(movie);
    }

    public void update(Movie movie) {

        new UpdateMovieAsyncTask(movieDAO).execute(movie);
    }

    public void delete(Movie movie) {

        new DeleteMovieAsyncTask(movieDAO).execute(movie);
    }

    public void deleteItem(int dMovieId) {
        new DeleteMovieItemAsyncTask(movieDAO).execute(dMovieId);
    }

    public Movie GetMovieItem(int dMovieId) {

        GetMovieItemAsyncTask task = new GetMovieItemAsyncTask(movieDAO);
        task.delegate = this;
        task.execute(dMovieId);
        return queryMovie;
    }

    public LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }

    @Override
    public void asyncFinished(Movie result) {
        queryMovie = result;
        Log.v("TestAsync", String.valueOf(queryMovie));
        isRunning = false;
        String Plot = result.getPlotSynopsis();
        Log.v("Plot", Plot);
    }

    private static class InsertMovieAsyncTask extends AsyncTask<Movie, Void, Void> {

        private MovieDAO movieDao;

        private InsertMovieAsyncTask(MovieDAO movieDAO) {
            this.movieDao = movieDAO;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.insert(movies[0]);
            return null;
        }
    }

    private static class UpdateMovieAsyncTask extends AsyncTask<Movie, Void, Void> {

        private MovieDAO movieDao;

        private UpdateMovieAsyncTask(MovieDAO movieDAO) {
            this.movieDao = movieDAO;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.update(movies[0]);
            return null;
        }
    }

    private static class DeleteMovieAsyncTask extends AsyncTask<Movie, Void, Void> {

        private MovieDAO movieDao;

        private DeleteMovieAsyncTask(MovieDAO movieDAO) {
            this.movieDao = movieDAO;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.delete(movies[0]);
            return null;
        }
    }

    private static class DeleteMovieItemAsyncTask extends AsyncTask<Integer, Void, Void> {

        private MovieDAO movieDao;

        private DeleteMovieItemAsyncTask(MovieDAO movieDAO) {
            this.movieDao = movieDAO;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            movieDao.deleteItem(integers[0]);
            return null;
        }
    }

    public static class GetMovieItemAsyncTask extends AsyncTask<Integer, Void, Movie> {

        private MovieDAO movieDao;
        private MovieRepository delegate = null;

        public GetMovieItemAsyncTask(MovieDAO movieDAO) {
            this.movieDao = movieDAO;

        }

        @Override
        protected Movie doInBackground(final Integer... integers) {
            Log.v("integers", integers[0].toString());
            return movieDao.getMoviesItem(integers[0]);

        }

        @Override
        protected void onPostExecute(Movie movie) {
            delegate.asyncFinished(movie);
            super.onPostExecute(movie);

        }
    }

}
