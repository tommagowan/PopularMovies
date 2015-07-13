package com.tcm.popularmovies.app.model;

public enum SortOption {
    popularity_desc("popularity.desc");

    private String optionString;

    SortOption(String optionString) {
        this.optionString = optionString;
    }


    @Override
    public String toString() {
        return optionString;
    }
}
