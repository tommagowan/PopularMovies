package com.tcm.popularmovies.app.model;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

@Table(name = Movie.TABLE_NAME, id = BaseColumns._ID)
public class Movie extends Model {

    public static final String TABLE_NAME = "MOVIE";
    @Column(name = "MOVIE_ID", unique = true)
    private long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "RELEASE_DATE")
    private Date releaseDate;

    @Column(name = "POSTER_PATH")
    private String posterPath;

    @Column(name = "VOTE_AVERAGE")
    private double voteAverage;

    @Column(name = "OVERVIEW")
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
