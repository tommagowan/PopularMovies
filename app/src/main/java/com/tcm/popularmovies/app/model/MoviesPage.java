package com.tcm.popularmovies.app.model;

import com.tcm.popularmovies.app.model.Movie;

import java.util.List;

public class MoviesPage {
    private int page;
    private int totalPages;
    private int totalResults;
    private List<Movie> results;

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<Movie> getResults() {
        return results;
    }
}
