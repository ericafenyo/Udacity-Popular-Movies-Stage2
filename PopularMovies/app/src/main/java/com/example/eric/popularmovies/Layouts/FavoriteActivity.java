package com.example.eric.popularmovies.Layouts;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.eric.popularmovies.Models.Movie;
import com.example.eric.popularmovies.R;
import com.example.eric.popularmovies.Utils.data.FavoriteContract;
import com.example.eric.popularmovies.Utils.data.FavoriteDataLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_BACKDROP;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_DATE;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_GENRE_ID;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_OVERVIEW;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_POSTER;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_RATE;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_TITLE;

public class FavoriteActivity extends AppCompatActivity implements FavoriteAdapter.ListItemClickListener {

    @BindView(R.id.fav_rv)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.fav_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            setTitle(R.string.title_favorites);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getSupportLoaderManager().initLoader(1, null, cursorLoader);

        setLayoutManager();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(1, null, cursorLoader);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.delete_all:
                deleteAllData();
                break;
        }
        return true;
    }

    public void setLayoutManager() {
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        GridLayoutManager layoutManager;
        if (!isTablet) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                layoutManager = new GridLayoutManager(FavoriteActivity.this, 2);
                recyclerView.setLayoutManager(layoutManager);
            } else {
                layoutManager = new GridLayoutManager(FavoriteActivity.this, 4);
                recyclerView.setLayoutManager(layoutManager);
            }
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                layoutManager = new GridLayoutManager(FavoriteActivity.this, 4);
                recyclerView.setLayoutManager(layoutManager);
            } else {
                layoutManager = new GridLayoutManager(FavoriteActivity.this, 6);
                recyclerView.setLayoutManager(layoutManager);
            }
        }
    }


    //deletes all favorites data from the database
    public void deleteAllData() {
        Uri uri = FavoriteContract.FavEntryList.FAVORITES_CONTENT_URI;
        int fb = getContentResolver().delete(uri, null, null);
        recyclerView.invalidate();
        if (fb != 0) {
            Intent intent = getIntent();
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
        getSupportLoaderManager().restartLoader(1, null, cursorLoader);
    }

    /**
     * @param position the adapter position
     * @param mdata    Lists of Movie data
     * @description sends data to MovieDetails class
     */
    private void sendFavIntentData(int position, List<Movie> mdata) {
        Movie movie = mdata.get(position);
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
            return new FavoriteDataLoader(FavoriteActivity.this);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            List<Movie> favModel = new ArrayList<>();

            if (data != null) {
                while (data.moveToNext()) {
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
                    String mOverview = data.getString(overview_index);
                    String mPoster = data.getString(poster_index);
                    String mBackDrop = data.getString(backdrop_index);

                    Movie model = new Movie(mTitle, mRate, mPoster, mOverview, mDate, mID, mGenre, mBackDrop);
                    favModel.add(model);
                    FavoriteAdapter adapter = new FavoriteAdapter(FavoriteActivity.this, FavoriteActivity.this, favModel);
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

    @Override
    public void onClick(int position, List<Movie> movies) {
        sendFavIntentData(position, movies);
    }
}
