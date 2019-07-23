package com.example.android.movieappstage2.MovieInformation;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Szymon on 29.11.2018.
 */

public class MovieReview implements Parcelable {

    private String reviewerId;
    private String reviewText;
    private int movieId;


        public MovieReview (String mReviewerId, String mReviewText,  int mMovieId){
            this.reviewerId = mReviewerId;
            this.reviewText = mReviewText;
            this.movieId = mMovieId;
        }


        //    Allow for parcel reading
        private MovieReview(Parcel in){
            reviewerId = in.readString();
            reviewText = in.readString();
            movieId = in.readInt();
        }

//    Provide get methods that will allow to retrieve class item details in different places in the app

        public String getreviewerId() {return reviewerId;}
        public String getreviewText() {return reviewText;}
        public int getMovieId() {return movieId;}

        //Allow for writing to parcel
        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(reviewerId);
            parcel.writeString(reviewText);
            parcel.writeInt(movieId);

        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Parcelable.Creator<MovieReview> CREATOR = new Parcelable.Creator<MovieReview>() {
            @Override
            public MovieReview createFromParcel(Parcel in) {
                return new MovieReview(in);
            }

            @Override
            public MovieReview[] newArray(int size) {
                return new MovieReview[size];
            }

        };
    }