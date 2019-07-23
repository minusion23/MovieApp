package com.example.android.movieappstage2.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

/**
 * Created by Szymon on 04.12.2018.
 */
@Database(entities = Movie.class, version = 2, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final String DATBASE_NAME = "movieDB";
    private static MovieDatabase instance;

    public static synchronized MovieDatabase getInstance(Context context) {

        if (instance == null) {

            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MovieDatabase.class, DATBASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
//Abstract constructor to be used throughout the code
    public abstract MovieDAO movieDAO();
}