package com.example.android.movieappstage2;

/**
 * Created by Szymon on 29.11.2018.
 */

public class Constants {
    //Please fill the String API_KEY with your IDM developer key to se the app correctly

    public static final String API_KEY = BuildConfig.API_KEY;
    public static final String IMDB_URL_POPULAR = "http://api.themoviedb.org/3/movie/popular?api_key=";
    public static final String IMDB_URL_TOP_RATED = "http://api.themoviedb.org/3/movie/top_rated?api_key=";
    public static final String IMDB_URL_MOVIE_REVIEW_PART1 = "http://api.themoviedb.org/3/movie/";
    public static final String IMDB_URL_MOVIE_REVIEW_PART2 = "/reviews?api_key=";
    public static final String IMDB_URL_MOVIE_TRAILERS_PART1 = "http://api.themoviedb.org/3/movie/";
    public static final String IMDB_URL_MOVIE_TRAILERS_PART2 = "/videos?api_key=";
    public static final String YOUTUBE_ACCESS_STRING = "https://www.youtube.com/watch?v=";
    public static final String IMDB_IMAGE_STRING = "http://image.tmdb.org/t/p/w185";
}
