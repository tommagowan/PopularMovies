package com.tcm.popularmovies.app;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.tcm.popularmovies.app.model.Movie;
import com.tcm.popularmovies.app.model.MoviesPage;
import com.tcm.popularmovies.app.model.SortOption;
import com.tcm.popularmovies.app.tmdb.TMDbService;

class FetchPopularMoviesTask extends AsyncTask<Void, Void, Void> {
    public static final String LAST_REFRESH_TS = "LAST_REFRESH_TS";

    private final TMDbService tmDbService;
    private SharedPreferences preferences;

    public FetchPopularMoviesTask(TMDbService tmDbService, SharedPreferences preferences) {
        this.tmDbService = tmDbService;
        this.preferences = preferences;
    }

    @Override
    protected Void doInBackground(Void[] params) {
        long now = System.currentTimeMillis();
        if (shouldRefresh(now)) {
            MoviesPage movies = tmDbService.listPopularMovies(SortOption.popularity_desc);
            saveMovies(movies);
            preferences.edit().putLong(LAST_REFRESH_TS, now).apply();
        }
        return null;
    }

    private void saveMovies(MoviesPage movies) {
        ActiveAndroid.beginTransaction();
        try {
            ActiveAndroid.getDatabase().execSQL("delete from " + Movie.TABLE_NAME);
            for (Movie movie : movies.getResults()) {
                Log.d("TAG", "Movie: " + movie.getTitle());
                movie.save();
            }
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    private boolean shouldRefresh(long nowTimestamp) {
        long lastRefresh = preferences.getLong(LAST_REFRESH_TS, -1);
        return nowTimestamp > (lastRefresh + (24 * 60 * 60 * 1000));
    }

}
