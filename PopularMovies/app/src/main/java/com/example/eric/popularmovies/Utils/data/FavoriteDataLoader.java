package com.example.eric.popularmovies.Utils.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.FAVORITES_CONTENT_URI;

/**
 * Created by eric on 24/10/2017.
 */

public class FavoriteDataLoader extends AsyncTaskLoader<Cursor> {

    public FavoriteDataLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Cursor loadInBackground() {
        try {
            return getContext().getContentResolver().query(FAVORITES_CONTENT_URI,null,null,null,null);
        } catch (Exception e) {
            Log.e("Error querying data: ",e.toString());
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void deliverResult(Cursor data) {
        super.deliverResult(data);
    }
}
