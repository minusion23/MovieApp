package com.example.android.movieappstage2.MovieInformation;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.movieappstage2.Constants;

/**
 * Created by Szymon on 30.11.2018.
 */

public class MovieVideo implements Parcelable {

    String mTrailerLink;
    private String mVideoIdentifier;
    private String mVideoType;
    private int mVideoId;

    public MovieVideo(String videoIdentifier, String videoType, int videoId){

        mVideoIdentifier = videoIdentifier;
        mVideoType = videoType;
        mVideoId = videoId;
        mTrailerLink = Constants.YOUTUBE_ACCESS_STRING + mVideoIdentifier + "";
    }

    public String getVideoIdentifier() {return mVideoIdentifier;}
    public String getVideoType () {return mVideoType;}
    public int getVideoId (){return mVideoId;}
    public String getmTrailerLink() {return mTrailerLink;}

    private MovieVideo(Parcel in){
        mVideoIdentifier = in.readString();
        mVideoType = in.readString();
        mVideoId = in.readInt();
        mTrailerLink=in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mVideoIdentifier);
        parcel.writeString(mVideoType);
        parcel.writeInt(mVideoId);
        parcel.writeString(mTrailerLink);

    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<MovieVideo> CREATOR = new Parcelable.Creator<MovieVideo>() {
        @Override
        public MovieVideo createFromParcel(Parcel in) {
            return new MovieVideo(in);
        }

        @Override
        public MovieVideo[] newArray(int size) {
            return new MovieVideo[size];
        }

    };

}
