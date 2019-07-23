package com.example.android.movieappstage2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import com.example.android.movieappstage2.Adapters.FavoriteMovieAdapter;
import com.example.android.movieappstage2.Adapters.RecViewMovieAdapter;
import com.example.android.movieappstage2.Database.Movie;
import com.example.android.movieappstage2.Database.MovieDAO;
import com.example.android.movieappstage2.Database.MovieViewModel;
import com.example.android.movieappstage2.MovieInformation.MovieItem;
import com.example.android.movieappstage2.QueryUtilities.QueryUtils;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//Please fill the String API_KEY with your IDM developer key to se the app correctly

public class MainActivity extends AppCompatActivity implements RecViewMovieAdapter.GridViewClickListener, FavoriteMovieAdapter.GridViewClickListener {
    private ArrayList<MovieItem> movieItems;
    private NetworkInfo networkInfo;
    private GridView gridView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecViewMovieAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int orientation = getResources().getConfiguration().orientation;
//        Prepare to check internet connection

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connManager != null;
        networkInfo = connManager.getActiveNetworkInfo();

//        Load differently if there was a saved instance state

        if (savedInstanceState == null || !savedInstanceState.containsKey("movieItems")) {
            setContentView(R.layout.activity_main);
            updateUIPopular();
        } else {
            setContentView(R.layout.activity_main);

            movieItems = savedInstanceState.getParcelableArrayList("movieItems");
            mRecyclerView =(RecyclerView) findViewById(R.id.rv);

            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(),3));
            }

            else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(),2));
            }

            adapter = new RecViewMovieAdapter(movieItems, getApplicationContext(),MainActivity.this);
            mRecyclerView.setAdapter(adapter);
        }

    }

//    Sort by rating check if there is an internet connection if not show an error to the user

    private void updateUIRating() {
        if (networkInfo != null && networkInfo.isConnected()) {
            URL fetchURL = createUrl(Constants.IMDB_URL_TOP_RATED + Constants.API_KEY);
            new movieQueryTask(this).execute(fetchURL);
        } else {
            setContentView(R.layout.internet_error);
        }

    }

//    Sort by popularity check if there is an internet connection if not show an error to the user

    private void updateUIPopular() {
        if (networkInfo != null && networkInfo.isConnected()) {
            URL fetchURL = createUrl(Constants.IMDB_URL_POPULAR+ Constants.API_KEY);
            new movieQueryTask(this).execute(fetchURL);
        } else {
            setContentView(R.layout.internet_error);
        }

    }

    private void updateUIFavorite(){
        Intent favoriteIntent = new Intent(getApplicationContext(), FavoriteActivity.class);
        startActivity(favoriteIntent);
    }


//    Create a bona fide url from a string URL
    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e("LOG_TAG", getString(R.string.URL_error_string), exception);

            return null;
        }
        return url;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.movie_items_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort_popular) {
            mRecyclerView.setAdapter(null);
            updateUIPopular();
            return true;
        }

        if (id == R.id.sort_rating) {
            mRecyclerView.setAdapter(null);
            updateUIRating();
            return true;
        }

        if (id == R.id.favorite) {
            mRecyclerView.setAdapter(null);
            updateUIFavorite();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle OutState) {
        OutState.putParcelableArrayList("movieItems", movieItems);
        super.onSaveInstanceState(OutState);
    }

    @Override
    public void onGridItemClick(int clickedItemIndex) {
        MovieItem currentMovieItem = movieItems.get(clickedItemIndex);
                    Intent intent = new Intent(getApplicationContext(), MovieDetails.class);
                    intent.putExtra("movieItemClickedDetails", movieItems.get(clickedItemIndex));
                    startActivity(intent);
    }

//    Load movie details in the background and fill the movie class

     private static class movieQueryTask extends AsyncTask<URL, Void, List<MovieItem>> {
        private WeakReference<MainActivity> activityReference;

        movieQueryTask (MainActivity context){
             activityReference = new WeakReference<MainActivity>(context);
         }
        @Override
        protected List<MovieItem> doInBackground(URL... urls) {
            URL searchURL = urls[0];
            return QueryUtils.fetchMovieItems(searchURL);

        }


        @Override
        protected void onPostExecute(List<MovieItem> movieList) {
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            activity.movieItems = new ArrayList<MovieItem>(movieList);

            activity.mRecyclerView =(RecyclerView) activity.findViewById(R.id.rv);
            int orientation = activity.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                activity.mRecyclerView.setLayoutManager(new GridLayoutManager(activity.mRecyclerView.getContext(),3));
            }

            else{
                activity.mRecyclerView.setLayoutManager(new GridLayoutManager(activity.mRecyclerView.getContext(),2));
            }
            activity.adapter = new RecViewMovieAdapter(activity.movieItems, activity.getApplicationContext(),activity);
            activity.mRecyclerView.setAdapter(activity.adapter);
        }

    }

}

