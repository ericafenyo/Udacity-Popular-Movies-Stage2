package com.example.eric.popularmovies.Utils.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by eric on 20/10/2017.
 */

public class FavoriteContract {

    public   static final String PATH_FAVORITES ="favorites";
    public   static final String AUTHORITY ="com.example.eric.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class FavEntryList implements BaseColumns{
        // Fav content URI = base content URI + path
        public static final Uri FAVORITES_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorites";
        public   static final String COLUMN_TITLE ="originalTitle";
        public   static final String COLUMN_OVERVIEW ="overview";
        public   static final String COLUMN_GENRE_ID ="genreIds";
        public   static final String COLUMN_RATE="movieRating";
        public   static final String COLUMN_DATE="releaseDate";
        public   static final String COLUMN_POSTER ="posterPath";
        public   static final String COLUMN_BACKDROP ="backdropPath";
    }
}
