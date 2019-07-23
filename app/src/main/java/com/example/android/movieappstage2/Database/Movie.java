package com.example.android.movieappstage2.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.movieappstage2.Constants;
import com.example.android.movieappstage2.MovieInformation.MovieItem;
import com.example.android.movieappstage2.MovieInformation.MovieReview;
import com.example.android.movieappstage2.MovieInformation.MovieVideo;
import com.example.android.movieappstage2.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Szymon on 04.12.2018.
 */
@Entity(tableName = "movie_details_table")
//Room class with movie details
public class Movie {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int movieId;

    private String image; //movie poster image

    private String title;

    private String releaseDate;

    private String voteAverage;

    private String plotSynopsis;

    private Boolean isFavorite;

    @Ignore
    public Movie(String vTitle, String releaseDate, String voteAverage, String plotSynopsis, String image, int movieId, Boolean isFavorite) {

        this.title = vTitle;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
        this.image = image;
        this.movieId = movieId;
        this.isFavorite = isFavorite;
    }

    public Movie( int id, String title, String releaseDate, String voteAverage, String
        plotSynopsis, String image,int movieId, Boolean isFavorite){
            this.id = id;
            this.title = title;
            this.releaseDate = releaseDate;
            this.voteAverage = voteAverage;
            this.plotSynopsis = plotSynopsis;
            this.image = image;
            this.movieId = movieId;
            this.isFavorite = isFavorite;
        }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }
}
