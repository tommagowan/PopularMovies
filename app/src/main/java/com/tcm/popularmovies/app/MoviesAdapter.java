package com.tcm.popularmovies.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tcm.popularmovies.app.model.Movie;
import com.tcm.popularmovies.app.tmdb.TMDbImageService;

public class MoviesAdapter extends CursorAdapter {
    private TMDbImageService tmDbImageService;

    public MoviesAdapter(Context context) {
        super(context, null, 0);
        tmDbImageService = new TMDbImageService(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_grid_item, parent, false);
        view.setTag(new ViewHolder(view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        try {
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            Movie movie = new Movie();
            movie.loadFromCursor(cursor);
            viewHolder.id = movie.getId();
            tmDbImageService.getImageResource(movie.getPosterPath(), viewHolder.imageView );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder {
        private final ImageView imageView;
        public long id;

        public ViewHolder(View parent) {
            imageView = (ImageView) parent.findViewById(R.id.movie_list_item_image);
        }
    }
}
