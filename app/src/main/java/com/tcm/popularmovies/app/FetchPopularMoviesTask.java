package com.tcm.popularmovies.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.tcm.popularmovies.app.db.ContentProvider;
import com.tcm.popularmovies.app.model.Movie;
import com.tcm.popularmovies.app.model.MoviesPage;
import com.tcm.popularmovies.app.model.SortOption;
import com.tcm.popularmovies.app.tmdb.TMDbService;

import java.util.Date;

class FetchPopularMoviesTask extends AsyncTask<Void, Void, Exception> {

    private final TMDbService tmDbService;
    private Context context;
    private SortOption sortOption;
    private ProgressDialog progressDialog;

    public FetchPopularMoviesTask(TMDbService tmDbService, Context context, SortOption sortOption) {
        this.tmDbService = tmDbService;
        this.context = context;
        this.sortOption = sortOption;
        this.progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setTitle("Refreshing");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }

    @Override
    protected Exception doInBackground(Void... params) {
        try {
            checkNetworkAvailable();
            MoviesPage movies = tmDbService.listPopularMovies(sortOption, TMDbService.TMDB_DATE_FORMAT.format(new Date()));
            saveMovies(movies);
        } catch (Exception e) {
            Log.e("FetchPopularMoviesTask", "Error fetching movie information", e);
            return e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Exception e) {
        if (e == null) {
            // Not updating using content resolver, therefore explicity change
            context.getContentResolver().notifyChange(ContentProvider.createUri(Movie.class, null), null);
        } else {
            Toast toast = Toast.makeText(context, "Error occurred while fetching movie data: " + e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
        progressDialog.dismiss();
    }

    private void saveMovies(MoviesPage movies) {
        ActiveAndroid.beginTransaction();
        try {
            ActiveAndroid.getDatabase().execSQL("delete from " + Movie.TABLE_NAME);
            for (Movie movie : movies.getResults()) {
                Log.d("FetchPopularMoviesTask", "Movie: " + movie.getTitle());
                movie.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    //Based on a stackoverflow snippet
    private void checkNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            throw new IllegalStateException("Network is unavailable");
        }
    }
}
