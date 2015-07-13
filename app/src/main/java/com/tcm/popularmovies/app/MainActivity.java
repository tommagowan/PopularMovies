package com.tcm.popularmovies.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.tcm.popularmovies.app.model.SortOption;
import com.tcm.popularmovies.app.tmdb.TMDbService;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class MainActivity extends ActionBarActivity {

    private RestAdapter restAdapter;
    private TMDbService tmDbService;

    @Override
    protected void onStart() {
        super.onStart();
        final SortOption sortOption = getSortOptionFromPreferences();
        final long now = System.currentTimeMillis();
        if (shouldRefresh(now, sortOption)) {
            new FetchPopularMoviesTask(tmDbService, this, sortOption){
                @Override
                protected void onPostExecute(Exception e) {
                    super.onPostExecute(e);
                    getPreferences().edit()
                            .putLong(MainActivity.this.getString(R.string.pref_lastRefreshTs_key), now)
                            .putString(MainActivity.this.getString(R.string.pref_lastSortMoviesBy_key), sortOption.name())
                            .apply();
                }
            }.execute();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateJsonDeserializer()).create();

        restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3")
                .setConverter(new GsonConverter(gson)).build();

        tmDbService = restAdapter.create(TMDbService.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class DateJsonDeserializer implements JsonDeserializer<Date> {
        private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
                throws JsonParseException {
            if (json.getAsString() != null && !json.getAsString().isEmpty()) {
                try {
                    return DATE_FORMAT.parse(json.getAsString());
                } catch (ParseException e) {
                    throw new JsonParseException(e);
                }
            }
            return null;
        }
    }

    private SortOption getSortOptionFromPreferences() {
        String key = getString(R.string.pref_sortMoviesBy_key);
        String defaultValue = getString(R.string.pref_sortMoviesBy_default);
        String sortOptionString = getPreferences().getString(key, defaultValue);
        return SortOption.valueOf(sortOptionString);
    }

    private SortOption getLastSortOptionFromPreferences() {
        String key = getString(R.string.pref_lastSortMoviesBy_key);
        String sortOptionString = getPreferences().getString(key, "");
        return sortOptionString.isEmpty() ? null : SortOption.valueOf(sortOptionString);
    }


    private boolean shouldRefresh(long nowTimestamp, SortOption sortOption) {
        SortOption lastSortOption = getLastSortOptionFromPreferences();
        long lastRefreshTs = getPreferences().getLong(getString(R.string.pref_lastRefreshTs_key), -1);
        boolean refresh = lastSortOption == null;
        refresh = refresh || !sortOption.equals(lastSortOption);
        // TODO I'm sure this could be improved using a scheduler
        return refresh || (nowTimestamp > (lastRefreshTs + (24 * 60 * 60 * 1000)));
    }

    private SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

}
