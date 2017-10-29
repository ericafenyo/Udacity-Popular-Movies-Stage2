package com.example.eric.popularmovies.Layouts;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.eric.popularmovies.Adapters.FavoriteAdapter;
import com.example.eric.popularmovies.Models.MovieModel;
import com.example.eric.popularmovies.R;
import com.example.eric.popularmovies.Utils.data.FavoriteContract;
import com.example.eric.popularmovies.Utils.data.FavoriteDataLoader;

import java.util.ArrayList;
import java.util.List;

import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_BACKDROP;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_DATE;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_GENRE_ID;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_OVERVIEW;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_POSTER;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_RATE;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_TITLE;

public class Favorite extends AppCompatActivity  implements FavoriteAdapter.ItemClickListener {

    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    Toolbar toolbar;
    FavoriteAdapter adapter;
    ImageView fav_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

//        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.fav_toolbar);
        setSupportActionBar(toolbar);
        this.setTitle(R.string.fav);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = (RecyclerView) findViewById(R.id.fav_rv);

        //For an unknown reason, "ButterKnife" is not binding some Views so decided to stop using it
        fav_iv = (ImageView) findViewById(R.id.remove_fav);

        getSupportLoaderManager().initLoader(1,null,cursorLoader);

        setLayoutManager();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(1,null,cursorLoader);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fav_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.delete_all:
                deleteAllData();
                break;
        }
        return true;
    }

    public void setLayoutManager(){
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        if (!isTablet) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                layoutManager = new GridLayoutManager(Favorite.this, 2);
                recyclerView.setLayoutManager(layoutManager);
            } else {
                layoutManager = new GridLayoutManager(Favorite.this, 4);
                recyclerView.setLayoutManager(layoutManager);
            }
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                layoutManager = new GridLayoutManager(Favorite.this, 4);
                recyclerView.setLayoutManager(layoutManager);
            } else {
                layoutManager = new GridLayoutManager(Favorite.this, 6);
                recyclerView.setLayoutManager(layoutManager);
            }
        }
    }

    //ItemClickListener
    @Override
    public void onItemClickListener(int position, List<MovieModel> models) {
      sendFavIntentData(position,models);
    }

    //deletes all favorites data from the database
    public void deleteAllData(){
        Uri uri = FavoriteContract.FavEntryList.FAVORITES_CONTENT_URI;
        int fb = getContentResolver().delete(uri,null,null);
        recyclerView.invalidate();
        if (fb != 0){
            Intent intent = getIntent();
            finish();
            overridePendingTransition( 0, 0);
            startActivity(intent);
            overridePendingTransition( 0, 0);
        }
        getSupportLoaderManager().restartLoader(1,null,cursorLoader);
    }

    /**
     * @param position  the adapter position
     * @param mdata Lists of Movie data
     * @description sends data to MovieDetails class
     */
    private void sendFavIntentData(int position, List<MovieModel> mdata) {
        MovieModel movie = mdata.get(position);
        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra("original_title", movie.getOriginalTitle());
        intent.putExtra("vote_average", movie.getVoteAverage());
        intent.putExtra("overview", movie.getOverview());
        intent.putExtra("release_date", movie.getReleaseDate());
        intent.putExtra("poster_path", movie.getPosterPath());
        intent.putExtra("backdrop_path", movie.getBackdropPath());
        intent.putExtra("total_pages", movie.getTotalPages());
        intent.putExtra("genre_ids", movie.getGenreIds());
        intent.putExtra("movies_id", movie.getId());
        startActivity(intent);
    }


    //FavoriteDataLoader<Cursor> Callbacks
    private LoaderManager.LoaderCallbacks<Cursor> cursorLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new FavoriteDataLoader(Favorite.this);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            List<MovieModel> favModel = new ArrayList<>();

            if (data != null) {
                while (data.moveToNext()){
                    int id_index = data.getColumnIndex(FavoriteContract.FavEntryList._ID);
                    int title_index = data.getColumnIndex(COLUMN_TITLE);
                    int genre_index = data.getColumnIndex(COLUMN_GENRE_ID);
                    int date_index = data.getColumnIndex(COLUMN_DATE);
                    int rate_index = data.getColumnIndex(COLUMN_RATE);
                    int overview_index = data.getColumnIndex(COLUMN_OVERVIEW);
                    int poster_index = data.getColumnIndex(COLUMN_POSTER);
                    int backdrop_index = data.getColumnIndex(COLUMN_BACKDROP);

                    int mID = data.getInt(id_index);
                    String mTitle = data.getString(title_index);
                    int mGenre = data.getInt(genre_index);
                    String mDate = data.getString(date_index);
                    String mRate = data.getString(rate_index);
                    String mOverview= data.getString(overview_index);
                    String mPoster = data.getString(poster_index);
                    String mBackDrop = data.getString(backdrop_index);

                    MovieModel model =  new MovieModel(mTitle,mRate,mPoster,mOverview,mDate,mID,mGenre,mBackDrop);
                    favModel.add(model);
                    adapter = new FavoriteAdapter(Favorite.this,Favorite.this,favModel);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                }

                data.close();
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

}
