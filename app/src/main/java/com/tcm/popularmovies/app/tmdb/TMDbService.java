package com.tcm.popularmovies.app.tmdb;

import com.tcm.popularmovies.app.model.MoviesPage;
import com.tcm.popularmovies.app.model.SortOption;

import java.text.SimpleDateFormat;

import retrofit.http.GET;
import retrofit.http.Query;

import static java.util.Locale.getDefault;

public interface TMDbService {
    String API_KEY = "b8ba303fd9b20657d60c39bc5119ca2d";
    SimpleDateFormat TMDB_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", getDefault());

    @GET("/discover/movie?api_key=" + API_KEY + "&vote_count.gte=10")
    MoviesPage listPopularMovies(@Query("sort_by") SortOption sortBy, @Query("release_date.lte") String releaseDate);
}
