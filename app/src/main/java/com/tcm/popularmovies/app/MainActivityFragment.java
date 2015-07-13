package com.tcm.popularmovies.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tcm.popularmovies.app.db.ContentProvider;
import com.tcm.popularmovies.app.model.Movie;

public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private MoviesAdapter moviesAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        moviesAdapter = new MoviesAdapter(getActivity());
        View fragment = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) fragment.findViewById(R.id.movies_gridview);
        gridView.setAdapter(moviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    MoviesAdapter.ViewHolder viewHolder = (MoviesAdapter.ViewHolder) view.getTag();
                    Intent intent = new Intent(getActivity(), MovieDetailsActivity.class)
                            .setData(ContentProvider.createUri(Movie.class, viewHolder.id));
                    startActivity(intent);
                }
            }
        });
        return fragment;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                ContentProvider.createUri(Movie.class, null),
                new String[]{BaseColumns._ID, Movie.COL_POSTER_PATH}, null, null, null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        moviesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        moviesAdapter.swapCursor(null);
    }
}
