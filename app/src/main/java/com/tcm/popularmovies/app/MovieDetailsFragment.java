package com.tcm.popularmovies.app;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcm.popularmovies.app.model.Movie;
import com.tcm.popularmovies.app.tmdb.TMDbImageService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieDetailsFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy");
    private TextView title;
    private TextView releaseDate;
    private TextView voteAverage;
    private TextView summary;
    private ImageView poster;
    private TMDbImageService tmDbImageService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tmDbImageService = new TMDbImageService(getActivity());

        View fragment = inflater.inflate(R.layout.movie_details_fragment, container, false);
        title = (TextView) fragment.findViewById(R.id.movie_details_title);
        releaseDate = (TextView) fragment.findViewById(R.id.movie_details_release_date);
        voteAverage = (TextView) fragment.findViewById(R.id.movie_details_vote_average);
        summary = (TextView) fragment.findViewById(R.id.movie_details_summary);
        poster = (ImageView) fragment.findViewById(R.id.movie_details_poster);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = getActivity().getIntent().getData();
        return new CursorLoader(getActivity(), uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            Movie movie = new Movie();
            movie.loadFromCursor(data);
            title.setText(movie.getTitle());
            releaseDate.setText(getReleaseDate(movie));
            voteAverage.setText(movie.getVoteAverage() + "/10"); // TODO use resource
            summary.setText(movie.getOverview());
            tmDbImageService.getImageResource(movie.getPosterPath(), poster);
        }
    }

    private String getReleaseDate(Movie movie) {
        Date releaseDate = movie.getReleaseDate();
        return releaseDate == null ? "Release date unkown" : DATE_FORMAT.format(releaseDate);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
