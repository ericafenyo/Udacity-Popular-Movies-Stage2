package com.example.eric.popularmovies.Utils.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import static android.provider.BaseColumns._ID;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.FAVORITES_CONTENT_URI;

/**
 *
 * Created by eric on 24/10/2017.
 * rReturns a boolean to whether a specific row of data is present in the database or not
 */

public class SingleFavoriteDataLoader extends AsyncTaskLoader<Boolean> {
    private String id;
    public SingleFavoriteDataLoader(Context context, String id) {
        super(context);
        this.id = id;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Boolean loadInBackground() {
        Cursor cursor;
        Uri uri = FAVORITES_CONTENT_URI;
        try {
            cursor = getContext().getContentResolver().query(uri,null,_ID +" = " + id, null, null);
                cursor.close();
                if (cursor.getCount() > 0){
                    return true;
                }
                else {
                    return false;
                }
        } catch (Exception e) {
            Log.e("Error querying data: ",e.toString());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deliverResult(Boolean data) {
        super.deliverResult(data);
    }
}
