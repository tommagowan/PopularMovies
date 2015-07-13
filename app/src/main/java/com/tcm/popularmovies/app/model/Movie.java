package com.tcm.popularmovies.app.model;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

@Table(name = Movie.TABLE_NAME, id = BaseColumns._ID)
public class Movie extends Model {

    public static final String TABLE_NAME = "MOVIE";
    public static final String COL_MOVIE_ID = "MOVIE_ID";
    public static final String COL_TITLE = "TITLE";
    public static final String COL_RELEASE_DATE = "RELEASE_DATE";
    public static final String COL_POSTER_PATH = "POSTER_PATH";
    public static final String COL_VOTE_AVERAGE = "VOTE_AVERAGE";
    public static final String COL_OVERVIEW = "OVERVIEW";

    @Column(name = COL_MOVIE_ID, unique = true)
    private long id;

    @Column(name = COL_TITLE)
    private String title;

    @Column(name = COL_RELEASE_DATE)
    private Date releaseDate;

    @Column(name = COL_POSTER_PATH)
    private String posterPath;

    @Column(name = COL_VOTE_AVERAGE)
    private double voteAverage;

    @Column(name = COL_OVERVIEW)
    private String overview;

    public String getTitle() {
        return title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }
}
