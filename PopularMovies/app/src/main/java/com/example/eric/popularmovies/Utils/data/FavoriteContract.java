package com.example.eric.popularmovies.Utils.data;

import android.provider.BaseColumns;

/**
 * Created by eric on 20/10/2017.
 */

public class FavoriteContract {

    public static final class FavEntryList implements BaseColumns{
        public static final String TABLE_NAME = "favorites";
        public   static final String COLUMN_TITLE ="originalTitle";
        public   static final String COLUMN_OVERVIEW ="overview";
        public   static final String COLUMN_GENRE_ID ="genreIds";
        public   static final String COLUMN_RATE="releaseDate";
        public   static final String COLUMN_DATE="releaseDate";
        public   static final String COLUMN_POSTER ="posterPath";
    }
}
