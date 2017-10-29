package com.example.eric.popularmovies.Utils.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.eric.popularmovies.Utils.data.FavoriteContract.AUTHORITY;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.FAVORITES_CONTENT_URI;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.TABLE_NAME;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.PATH_FAVORITES;

/**
 * Created by eric on 24/10/2017.
 */

public class MovieContentProvider extends ContentProvider {
    private static final int FAVORITES = 100;
    private static final int FAVORITES_WITH_ID = 101;

    private FavoriteSqliteHelper helper;
    private static final UriMatcher sUriMatcher = BuildUriMatcher();

    public static UriMatcher BuildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,PATH_FAVORITES,FAVORITES);
        uriMatcher.addURI(AUTHORITY,PATH_FAVORITES + "/#",FAVORITES_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        helper = new FavoriteSqliteHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOder) {
        final SQLiteDatabase database = helper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        Cursor retCursor;
        switch (match){
            case FAVORITES:
                retCursor = database.query(TABLE_NAME,projection,selection,selectionArgs,null,null,sortOder);

                break;
            case FAVORITES_WITH_ID:
                retCursor = database.query(TABLE_NAME,projection,selection,selectionArgs,null,null,sortOder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
       final SQLiteDatabase database = helper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        Uri returnedUri;
        switch (match){
            case FAVORITES:
                long id = database.insertWithOnConflict(TABLE_NAME,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
                if (id > 0){

                    returnedUri = ContentUris.withAppendedId(FAVORITES_CONTENT_URI,id);

                }else {
                    throw new android.database.SQLException("Failed to insert into " + uri  );
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnedUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int tasksDeleted;

        switch (match) {
            case FAVORITES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            case FAVORITES:
                tasksDeleted = db.delete(TABLE_NAME,null,null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (tasksDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

}
