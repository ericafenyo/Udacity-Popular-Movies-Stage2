package com.example.eric.popularmovies.Utils.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_DATE;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_GENRE_ID;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_OVERVIEW;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_POSTER;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_RATE;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_TITLE;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.TABLE_NAME;

/**
 *
 * Created by eric on 20/10/2017.
 */

public class FavoriteSqliteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fav.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteSqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE =

                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER NOT NULL, " +
                        COLUMN_TITLE + " TEXT NOT NULL, " +
                        COLUMN_GENRE_ID + " INTEGER NOT NULL, " +
                        COLUMN_DATE + " TEXT NOT NULL, " +
                        COLUMN_RATE + " TEXT NOT NULL, " +
                        COLUMN_OVERVIEW + " REAL NOT NULL, " +
                        COLUMN_POSTER + " BLOB , " +
        ");";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int vieux, int nouveau) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public boolean insertFavorite(int id , String originalTitle,int genreId,String date,String voteAverage,String overview, byte[] poster) {
     SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(_ID,id);
        values.put(COLUMN_TITLE,originalTitle);
        values.put(COLUMN_GENRE_ID,genreId);
        values.put(COLUMN_DATE,date);
        values.put(COLUMN_RATE,voteAverage);
        values.put(COLUMN_OVERVIEW,overview);
        values.put(COLUMN_POSTER,poster);
        long feedback = db.insert(TABLE_NAME,null,values);
        if (feedback == -1){
            return false;
        }else {
            return true;
        }
    }
}
