package com.example.android.movieappstage2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movieappstage2.Adapters.FavoriteMovieAdapter;
import com.example.android.movieappstage2.Adapters.ReviewAdapter;
import com.example.android.movieappstage2.Adapters.TrailerAdapter;
import com.example.android.movieappstage2.Database.Movie;
import com.example.android.movieappstage2.Database.MovieDAO;
import com.example.android.movieappstage2.Database.MovieDatabase;
import com.example.android.movieappstage2.Database.MovieRepository;
import com.example.android.movieappstage2.MovieInformation.MovieItem;
import com.example.android.movieappstage2.MovieInformation.MovieReview;
import com.example.android.movieappstage2.MovieInformation.MovieVideo;
import com.example.android.movieappstage2.QueryUtilities.QueryUtils;
import com.example.android.movieappstage2.QueryUtilities.QueryUtilsReview;
import com.example.android.movieappstage2.QueryUtilities.QueryVideo;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Szymon on 18.11.2018.
 */
public class MovieDetails extends AppCompatActivity {
    int movieIdentification;
    private TextView titleTv;
    private NetworkInfo networkInfo;
    private TextView releaseDateTv;
    private TextView ratingTv;
    private TextView descriptionTv;
    private ImageView posterIv;
    private RecyclerView reviewRecyclerView;
    private ReviewAdapter rAdapter;
    private TrailerAdapter vAdapter;
    private RecyclerView trailerRecyclerView;
    private ArrayList<MovieReview> reviewItems;
    private ArrayList<MovieVideo> movieVideos;
    private MovieItem detailsMovieItem;
    private Button favoriteButton;
    private TextView reviewsTextView;
    private MovieDatabase mDB;
    private Boolean isFavorited;
    private String titleString;
    private String releaseDateString;
    private String ratingString;
    private String descriptionString;
    private String imageString;
    private MovieRepository movieRepository;
    private Movie favoriteMovieDetails;
    private MovieDAO movieDAO;
    private Intent intentShareDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        isFavorited = false;
        mDB = MovieDatabase.getInstance(getApplicationContext());
        movieDAO = mDB.movieDAO();
        movieRepository = new MovieRepository(getApplication());

//        Get ready to check the internet connection

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connManager != null;
        networkInfo = connManager.getActiveNetworkInfo();
        intentShareDetails = getIntent();
        setUpUI();
        favoriteButton = findViewById(R.id.favorite_button);
        if (savedInstanceState == null || !savedInstanceState.containsKey("ReviewItems")) {
            if (intentShareDetails != null) {

//        Retrieve the clicked movie details sent as parcel if any intent was sent fill the fields in the view

                if (intentShareDetails.hasExtra("movieItemClickedDetails")) {
                    detailsMovieItem = intentShareDetails.getParcelableExtra("movieItemClickedDetails");
                    titleString = detailsMovieItem.getTitle();
                    releaseDateString = detailsMovieItem.getReleaseDate();
                    ratingString = detailsMovieItem.getVoteAverage();
                    descriptionString = detailsMovieItem.getPlotSynopsis();
                    imageString = detailsMovieItem.getImageAddress();
                    movieIdentification = detailsMovieItem.getMovieId();
//                    Check if the item is in the database already which means it has been favorited

                    new isInDatabasedTask(this, movieRepository, movieDAO).execute(movieIdentification);

                }

                if (intentShareDetails.hasExtra("movieItemClickedDetailsFavorite")) {
                    int favoriteMovieId = intentShareDetails.getIntExtra("movieItemClickedDetailsFavorite", 0);
                    movieIdentification = favoriteMovieId;
                    movieRepository.GetMovieItem(favoriteMovieId);
                    favoriteButton.setText(R.string.FavoritedButton);
                    isFavorited = true;

//              Get the details from the database for the favorited item since we are coming from a favorite
                    new FavoritedTask(this, movieRepository, movieDAO).execute(movieIdentification);
                }
            }
        }

//        If no intent run the saved instance state if the keys are present
        else {


//        Retrieve the clicked movie details sent as parcel if any intent was sent fill the fields in the view
            if (savedInstanceState.containsKey("ReviewItems")) {
                reviewItems = savedInstanceState.getParcelableArrayList("ReviewItems");
                Log.v("Test review items", reviewItems.toString());
            }

            if (savedInstanceState.containsKey("MovieVideos")) {
                movieVideos = savedInstanceState.getParcelableArrayList("MovieVideos");
                Log.v("Test review items", movieVideos.toString());
            }

            if (savedInstanceState.containsKey("MovieItem")) {
                detailsMovieItem = savedInstanceState.getParcelable("MovieItem");
                Log.v("Test review items", detailsMovieItem.toString());

            }

            if (savedInstanceState.containsKey("Favorited")) {
                isFavorited = savedInstanceState.getBoolean("Favorited");
                Log.v("Test review items", detailsMovieItem.toString());
            }

//            Reload the UI with data
            ReloadUI();

        }

//        Fetch the reviews and videos

        fetchReviews(movieIdentification);
        fetchMovieVideos(movieIdentification);

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFavorited) {
                    favoriteButton.setText(R.string.FavoritedButton);
                    isFavorited = true;

                    Movie movie = new Movie(titleString, releaseDateString, ratingString, descriptionString, imageString, movieIdentification, isFavorited);
                    movieRepository.insert(movie);
                    return;
                } else {
                    favoriteButton.setText("Mark as Favorite");
                    movieRepository.deleteItem(movieIdentification);
                    isFavorited = false;

                }

            }
        });

        fetchReviews(movieIdentification);
        fetchMovieVideos(movieIdentification);

    }

//    Load the UI if not from savedInstance state or from the favorites activity
    private void LoadUI(Intent intent) {
        if (isFavorited) {
            favoriteButton.setText(R.string.FavoritedButton);
        }
        releaseDateTv = findViewById(R.id.release_date_tv);
        ratingTv = findViewById(R.id.rating_tv);
        descriptionTv = findViewById(R.id.description_tv);
        posterIv = findViewById(R.id.imageView);
        detailsMovieItem = intent.getParcelableExtra("movieItemClickedDetails");
        titleString = detailsMovieItem.getTitle();
        releaseDateString = detailsMovieItem.getReleaseDate();
        ratingString = detailsMovieItem.getVoteAverage();
        descriptionString = detailsMovieItem.getPlotSynopsis();
        imageString = detailsMovieItem.getImageAddress();
        movieIdentification = detailsMovieItem.getMovieId();
        Log.v("movieIdentification", String.valueOf(movieIdentification));
        titleTv.setText(titleString);
        releaseDateTv.setText(releaseDateString);
        ratingTv.setText(ratingString);
        descriptionTv.setText(descriptionString);
        Log.v("ImageSt", imageString);
        Picasso.with(this).load(Constants.IMDB_IMAGE_STRING + imageString).fit().centerInside().into(posterIv);

    }
//Load UI if activity is coming from favorites
    private void LoadUIFavorite(Movie favoriteMovie) {
        if (favoriteMovie != null) {
            releaseDateTv = findViewById(R.id.release_date_tv);
            ratingTv = findViewById(R.id.rating_tv);
            descriptionTv = findViewById(R.id.description_tv);
            posterIv = findViewById(R.id.imageView);
            titleString = favoriteMovie.getTitle();
            releaseDateString = favoriteMovie.getReleaseDate();
            ratingString = favoriteMovie.getVoteAverage();
            descriptionString = favoriteMovie.getPlotSynopsis();
            imageString = favoriteMovie.getImage();
            movieIdentification = favoriteMovie.getMovieId();
            Log.v("movieIdentification", String.valueOf(movieIdentification));
            titleTv.setText(titleString);
            releaseDateTv.setText(releaseDateString);
            ratingTv.setText(ratingString);
            descriptionTv.setText(descriptionString);
            Log.v("ImageSt", imageString);
            Picasso.with(this).load(Constants.IMDB_IMAGE_STRING + imageString).fit().centerInside().into(posterIv);
        }

    }
//    Reload UI with data when recreated
    private void ReloadUI() {
        if (isFavorited) {
            favoriteButton.setText(R.string.FavoritedButton);

        }
        reviewRecyclerView = (RecyclerView) findViewById(R.id.reviewListView);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rAdapter = new ReviewAdapter(reviewItems, this);
        reviewRecyclerView.setAdapter(rAdapter);
        trailerRecyclerView = (RecyclerView) findViewById(R.id.trailers_lv);
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        vAdapter = new TrailerAdapter(movieVideos, this);
        trailerRecyclerView.setAdapter(vAdapter);
        releaseDateTv = findViewById(R.id.release_date_tv);
        ratingTv = findViewById(R.id.rating_tv);
        descriptionTv = findViewById(R.id.description_tv);
        posterIv = findViewById(R.id.imageView);
        String titleString = detailsMovieItem.getTitle();
        Log.v("title string test", titleString);
        releaseDateString = detailsMovieItem.getReleaseDate();
        ratingString = detailsMovieItem.getVoteAverage();
        descriptionString = detailsMovieItem.getPlotSynopsis();
        imageString = detailsMovieItem.getImageAddress();
        movieIdentification = detailsMovieItem.getMovieId();
        Log.v("movieIdentification", String.valueOf(movieIdentification));
        titleTv.setText(titleString);
        releaseDateTv.setText(releaseDateString);
        ratingTv.setText(ratingString);
        descriptionTv.setText(descriptionString);
        Log.v("ImageSt", imageString);
        Picasso.with(this).load(Constants.IMDB_IMAGE_STRING + imageString).fit().centerInside().into(posterIv);

    }

    public void fetchReviews(int id) {
        if (networkInfo != null && networkInfo.isConnected()) {
            URL fetchURL = QueryUtils.createUrl(Constants.IMDB_URL_MOVIE_REVIEW_PART1 + id + Constants.IMDB_URL_MOVIE_REVIEW_PART2 + Constants.API_KEY);
            Log.v("FetchURL", fetchURL.toString());
            new ReviewQueryTask(this).execute(fetchURL);
        } else {
            setContentView(R.layout.internet_error);
        }

    }

    public void fetchMovieVideos(int id) {
        if (networkInfo != null && networkInfo.isConnected()) {
            URL fetchURL = QueryUtils.createUrl(Constants.IMDB_URL_MOVIE_TRAILERS_PART1 + id + Constants.IMDB_URL_MOVIE_TRAILERS_PART2 + Constants.API_KEY);
            Log.v("FetchURL", fetchURL.toString());
            new TrailerVideoQueryTask(this).execute(fetchURL);
        } else {
            setContentView(R.layout.internet_error);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle OutState) {
        if (favoriteMovieDetails != null) {
            detailsMovieItem = new MovieItem(favoriteMovieDetails.getTitle(), favoriteMovieDetails.getReleaseDate(),
                    favoriteMovieDetails.getVoteAverage(), favoriteMovieDetails.getPlotSynopsis(),
                    favoriteMovieDetails.getImage(), favoriteMovieDetails.getMovieId());
        }

        OutState.putParcelableArrayList("ReviewItems", reviewItems);
        OutState.putParcelableArrayList("MovieVideos", movieVideos);
        OutState.putParcelable("MovieItem", detailsMovieItem);
        OutState.putBoolean("Favorited", isFavorited);
        Log.v("ReviewItems", reviewItems.toString());
        Log.v("MovieVideos", movieVideos.toString());
        Log.v("ouststatetext", OutState.toString());
        super.onSaveInstanceState(OutState);
    }
//Setup UI to be ready to load items
    private void setUpUI() {

        titleTv = findViewById(R.id.title_tv);
        releaseDateTv = findViewById(R.id.release_date_tv);
        ratingTv = findViewById(R.id.rating_tv);
        descriptionTv = findViewById(R.id.description_tv);
        posterIv = findViewById(R.id.imageView);
        reviewsTextView = findViewById(R.id.reviewHeader);
        intentShareDetails = getIntent();
        reviewRecyclerView = (RecyclerView) findViewById(R.id.reviewListView);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rAdapter = new ReviewAdapter(reviewItems, this);
        reviewRecyclerView.setAdapter(null);
        trailerRecyclerView = (RecyclerView) findViewById(R.id.trailers_lv);
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        vAdapter = new TrailerAdapter(movieVideos, this);
        trailerRecyclerView.setAdapter(null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.movie_items_detail, menu);
        return true;
    }

//    Set up Menu that allows the user to go back to favorites or use the back button to go back to pop movies main
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_back_favorites) {
            Intent intent = new Intent(this, FavoriteActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class FavoritedTask extends AsyncTask<Integer, Void, Movie> {
        private WeakReference<MovieDetails> activityReference;
        private MovieRepository movieRepository;
        private MovieDAO movieDao;

//        Get favorited item details

        FavoritedTask(MovieDetails context, MovieRepository movieRepository, MovieDAO movieDao) {
            activityReference = new WeakReference<MovieDetails>(context);
            this.movieRepository = movieRepository;
            this.movieDao = movieDao;
        }

        @Override
        protected Movie doInBackground(Integer... integers) {

            return movieDao.getMoviesItem(integers[0]);

        }

        @Override
        protected void onPostExecute(Movie movie) {
            MovieDetails activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;
            activity.favoriteMovieDetails = movie;
            activity.LoadUIFavorite(activity.favoriteMovieDetails);
        }
    }

//    Get reviews for the item

    private static class ReviewQueryTask extends AsyncTask<URL, Void, List<MovieReview>> {
        private WeakReference<MovieDetails> activityReference;

        ReviewQueryTask(MovieDetails context) {
            activityReference = new WeakReference<MovieDetails>(context);
        }

        @Override
        protected List<MovieReview> doInBackground(URL... urls) {
            URL searchURL = urls[0];
            return QueryUtilsReview.fetchMovieReivews(searchURL);

        }


        @Override
        protected void onPostExecute(List<MovieReview> reviewList) {
            MovieDetails activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;
            activity.reviewItems = new ArrayList<MovieReview>(reviewList);
            activity.reviewRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
            activity.rAdapter = new ReviewAdapter(activity.reviewItems, activity);
            activity.reviewRecyclerView.setNestedScrollingEnabled(false);
            activity.reviewRecyclerView.setAdapter(activity.rAdapter);
        }
    }

//    Get trailers
    private static class TrailerVideoQueryTask extends AsyncTask<URL, Void, List<MovieVideo>> {
        private WeakReference<MovieDetails> activityReference;

        TrailerVideoQueryTask(MovieDetails context) {
            activityReference = new WeakReference<MovieDetails>(context);
        }

        @Override
        protected List<MovieVideo> doInBackground(URL... urls) {
            URL searchURL = urls[0];
            return QueryVideo.fetchMovieVideos(searchURL);

        }

        @Override
        protected void onPostExecute(List<MovieVideo> movieVideos) {
            MovieDetails activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;
            activity.movieVideos = new ArrayList<MovieVideo>(movieVideos);
            activity.trailerRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
            activity.vAdapter = new TrailerAdapter(activity.movieVideos, activity);
            activity.trailerRecyclerView.setAdapter(activity.vAdapter);

        }
    }
//Check if item is in database if such it has been favorited and UI needs to changed accordingly
    private static class isInDatabasedTask extends AsyncTask<Integer, Void, Boolean> {
        private WeakReference<MovieDetails> activityReference;
        private MovieRepository movieRepository;
        private MovieDAO movieDao;

        isInDatabasedTask(MovieDetails context, MovieRepository movieRepository, MovieDAO movieDao) {
            activityReference = new WeakReference<MovieDetails>(context);
            this.movieRepository = movieRepository;
            this.movieDao = movieDao;
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {

            return movieDao.getIfInDatabase(integers[0]);

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            MovieDetails activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;
            if (aBoolean != null) {
                activity.isFavorited = aBoolean;
            }

            activity.LoadUI(activity.intentShareDetails);
        }
    }

}

