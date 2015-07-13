package com.tcm.popularmovies.app.tmdb;

import com.tcm.popularmovies.app.model.MoviesPage;
import com.tcm.popularmovies.app.model.SortOption;

import retrofit.http.GET;
import retrofit.http.Query;

public interface TMDbService {
    String API_KEY = /* insert your tmdb key here */;

    @GET("/discover/movie?api_key=" + API_KEY)
    MoviesPage listPopularMovies(@Query("sort_by") SortOption sortBy);
}
