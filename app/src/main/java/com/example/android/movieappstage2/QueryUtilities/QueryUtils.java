package com.example.android.movieappstage2.QueryUtilities;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.movieappstage2.MovieInformation.MovieItem;
import com.example.android.movieappstage2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;

/**
 * Created by Szymon on 18.11.2018.
 */
//Helper class used for internet data retrieval and json parsing


public final class QueryUtils {

    private static final int readTimeout = 10000;
    private static final int connectionTimeout = 15000;
    private static final int isOk = 200;

    private static List<MovieItem> extractFeatureFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        List<MovieItem> movieItems = new ArrayList<>();

//        get JSONResponse details
        try {
            JSONObject baseJSONResponse = new JSONObject(jsonResponse);
            JSONArray jsonArrayResults = baseJSONResponse.getJSONArray("results");

//            Iterate through the response to fill all the items with details
            for (int i = 0; i < jsonArrayResults.length(); i++) {

                JSONObject currentMovieItem = jsonArrayResults.getJSONObject(i);
                String movieTitle;
                String movieReleaseDate;
                String movieVoteAverage;
                String moviePlotSynopsis;
                String movieReleaseDateLong;
                String imageHttp;
                int movieId;
                movieTitle = currentMovieItem.optString("title");
                movieReleaseDateLong = currentMovieItem.optString("release_date");
                movieVoteAverage = currentMovieItem.optString("vote_average");
                moviePlotSynopsis = currentMovieItem.optString("overview");
                imageHttp = currentMovieItem.optString("poster_path");
                movieReleaseDate = movieReleaseDateLong.substring(0, 4);
                movieVoteAverage += " /10";
                movieId = currentMovieItem.optInt("id");
                Log.v("movieId ", String.valueOf(movieId));
                MovieItem movieItem = new MovieItem(movieTitle, movieReleaseDate, movieVoteAverage, moviePlotSynopsis, imageHttp, movieId);
                movieItems.add(movieItem);
            }
        } catch (JSONException e) {
            Log.e("Query Utils", "Problem parsing JSON Response", e);
        }
        return movieItems;
    }
//    Open an HTTP connection and try to make the request successfully

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(readTimeout);
            urlConnection.setConnectTimeout(connectionTimeout);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == isOk) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("Log_TAG", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("Exception", "An Exception was thrown", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //    Read from the Http response
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String line = bufferReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferReader.readLine();
            }
        }
        return output.toString();
    }

    //    Query API return movieitems
    public static List<MovieItem> fetchMovieItems(URL url) {
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("Query utils", "Problem making the HTTP request.", e);
        }
        // Extract relevant fields from the JSON response and create a list of {@link NewsItems}s
        Log.e("LOG e", jsonResponse);
        return extractFeatureFromJson(jsonResponse);
    }

    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e("LOG_TAG", String.valueOf(R.string.URL_error_string), exception);

            return null;
        }
        return url;
    }
}
