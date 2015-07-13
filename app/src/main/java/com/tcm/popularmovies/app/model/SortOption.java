package com.tcm.popularmovies.app.model;

public enum SortOption {
    popularity_desc("popularity.desc"),
    voteAverage_desc("vote_average.desc");

    private String tmdbSortAttributeValue;

    SortOption(String tmdbSortAttributeValue) {
        this.tmdbSortAttributeValue = tmdbSortAttributeValue;
    }

    @Override
    public String toString() {
        return tmdbSortAttributeValue;
    }

}
