package com.tcm.popularmovies.app.tmdb;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class TMDbImageService {
    public static final String TMDB_IMAGE_URL = "http://image.tmdb.org/t/p/%s/%s";
    public static final String IMAGE_SIZE_W185 = "w185";
    private Context context;

    public TMDbImageService(Context context) {
        this.context = context;
    }

    public void getImageResource(String tmdbImageResource, ImageView imageView) {
        if (tmdbImageResource != null) {
            Picasso.with(context).load(String.format(TMDB_IMAGE_URL, IMAGE_SIZE_W185, tmdbImageResource)).into(imageView);
        }
    }
}
