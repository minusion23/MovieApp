package com.example.android.movieappstage2.MovieInformation;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Szymon on 18.11.2018.
 */


public class MovieItem implements Parcelable {

    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }

    };
    private String image; //movie poster image
    private String title;
    private String releaseDate;
    private String voteAverage;
    private String plotSynopsis;

//Set a constructor for the custom object
    private int movieId;

    public MovieItem(String vTitle, String vReleaseDate, String vVoteAverage, String vPlotSynopsis, String vImage, int vMovieId) {
        this.image = vImage;
        this.title = vTitle;
        this.releaseDate = vReleaseDate;
        this.voteAverage = vVoteAverage;
        this.plotSynopsis = vPlotSynopsis;
        this.movieId = vMovieId;

    }

//    Provide get methods that will allow to retrieve class item details in different places in the app

    //    Allow for parcel reading
    private MovieItem(Parcel in) {
        image = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readString();
        plotSynopsis = in.readString();
        movieId = in.readInt();
    }

    public String getImageAddress() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public int getMovieId() {
        return movieId;
    }

    //Allow for writing to parcel
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image);
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(voteAverage);
        parcel.writeString(plotSynopsis);
        parcel.writeInt(movieId);

    }

    @Override
    public int describeContents() {
        return 0;
    }
}
