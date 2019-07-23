package com.example.android.movieappstage2.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Szymon on 04.12.2018.
 */
@Dao
public interface MovieDAO {

    @Insert
    void insert(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("SELECT * FROM movie_details_table")
    LiveData<List<Movie>> getAllMovies();

    @Query("DELETE FROM movie_details_table WHERE movieId = :dMovieId")
    void deleteItem(int dMovieId);

    @Query("SELECT * FROM movie_details_table WHERE movieId = :dMovieId")
    Movie getMoviesItem(int dMovieId);

    @Query("SELECT isFavorite FROM movie_details_table WHERE movieId = :dMovieId")
    Boolean getIfInDatabase(int dMovieId);


}
