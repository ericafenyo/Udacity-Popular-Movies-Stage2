package com.example.eric.popularmovies.Layouts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eric.popularmovies.Adapters.MovieAdapter;
import com.example.eric.popularmovies.Models.MovieModel;
import com.example.eric.popularmovies.R;
import com.example.eric.popularmovies.Utils.data.MovieDataLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @disclaimer This app as it is a simple project supports only mobile and phablets;
 *
 *It needs a working TMDb API ;
 * I will replace AsyncTask with Google Volley and use ButterKnife in Popular Movie Stage 2
 * */

//TODO: Add a key with name "MyToken" to the gradle.properties and set it's value to your valid TMDb Api Key;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    //String values for TMDB Movie Json Data
    private static final String TITLE = "original_title";
    private static final String POSTER_PATH = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String ARRAY_ROOT = "results";

    //Shared preference variables
    private static final String SORTS = "sort_by";
    private static final String KEY = "order";
    private static final String DEFAULT_ORDER = "popular";
    private static final String TOP_RATED_ORDER = "top_rated";

    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;
    List<MovieModel> mdata;

    RecyclerView recyclerView;
    TextView error_tv;
    ProgressBar progressBar;
    private static String PARCEL_KEY = "parcel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Loader Callback
        getSupportLoaderManager().initLoader(1, null, loaderCallbacks);

//        if (!getSortPreferences(KEY,DEFAULT_ORDER).equals(DEFAULT_ORDER)){
//            MovieAsyncTask altTask  = new MovieAsyncTask(getSortPreferences(KEY,TOP_RATED_ORDER),this);
//            altTask.execute(API_KEY);
//            this.setTitle(R.string.top_rated);
//        }else {
//            MovieAsyncTask defaultTask= new MovieAsyncTask(DEFAULT_ORDER,this);
//            defaultTask.execute(API_KEY);
//        }
        //casting into views
        recyclerView = (RecyclerView) findViewById(R.id.movie_rv);
//        error_tv = (TextView) findViewById(R.id.error_tv);
//        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

//        //setting adapter
//        adapter = new MovieAdapter(movieData,MainActivity.this);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setHasFixedSize(true);

    }



    /**
     * end of onCreate
     */

    /*Inflates menu.xml*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /*Handles menu.xml -> menuItem selections*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id){
//            //sorts by popularity
//            case R.id.sort_popular:
//                if (getSortPreferences(KEY, DEFAULT_ORDER).equals(TOP_RATED_ORDER)){
//                    MovieAsyncTask popular_task = new MovieAsyncTask(DEFAULT_ORDER,this);
//                    popular_task.execute(API_KEY);
//                    finish();
//                    startActivity(getIntent());
//                    storeSortPreferences(KEY,DEFAULT_ORDER);
//                }
//                storeSortPreferences(KEY,DEFAULT_ORDER);
//                break;
//
//            //sorts by Top Rated
//            case R.id.sort_top_rated:
//            if (getSortPreferences(KEY, TOP_RATED_ORDER).equals(DEFAULT_ORDER)){
//                MovieAsyncTask top_rated_task = new MovieAsyncTask(TOP_RATED_ORDER,this);
//                top_rated_task.execute(API_KEY);
//                this.finish();
//                startActivity(getIntent());
//               }
//
//            if (getSortPreferences(KEY, DEFAULT_ORDER).equals(DEFAULT_ORDER)){
//                    this.finish();
//                    startActivity(getIntent());
//                    storeSortPreferences(KEY,TOP_RATED_ORDER);
//                }
//                storeSortPreferences(KEY,TOP_RATED_ORDER);
//            }

        return super.onOptionsItemSelected(item);
    }

    //shows error message
    public void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        error_tv.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    //OnclickItemListener
    @Override
    public void OnListItemClick(int position, List<MovieModel> movieModels) {
        sendIntentData(position, movieModels);
    }

    //Displays data if all conditions are favorable
    private void showData() {
        recyclerView.setVisibility(View.VISIBLE);
        error_tv.setVisibility(View.INVISIBLE);
    }

    /**
     * @param key   the SharedPreference Key for holding related value
     * @param value the SharedPreference value
     * @description stores a key:value data used to configure Movie sorting type
     */
    public void storeSortPreferences(String key, String value) {
        preferences = getSharedPreferences(SORTS, MODE_PRIVATE);
        preferenceEditor = preferences.edit();
        preferenceEditor.putString(key, value);
        preferenceEditor.apply();
    }

    /**
     * @param key  the SharedPreference Key for getting it's value
     * @param deft the SharedPreference default value to return if the value is not found
     * @description returns values paired with their related keys
     */
    public String getSortPreferences(String key, String deft) {
        preferences = getSharedPreferences(SORTS, MODE_PRIVATE);
        return preferences.getString(key, deft);
    }

    public void sendIntentData(int position, List<MovieModel> mdata) {
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
        Log.v("MainExtre", String.valueOf(movie.getId()));

        startActivity(intent);
    }

    private LoaderManager.LoaderCallbacks<List<MovieModel>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<MovieModel>>() {
        @Override
        public Loader<List<MovieModel>> onCreateLoader(int id, Bundle args) {
            return new MovieDataLoader(MainActivity.this);
        }

        @Override
        public void onLoadFinished(Loader<List<MovieModel>> loader, List<MovieModel> data) {

            MovieAdapter adapter = new MovieAdapter(data, MainActivity.this, MainActivity.this);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            //setting layoutManager
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
            }
            mdata = new ArrayList<>();
            mdata = data;
        }

        @Override
        public void onLoaderReset(Loader<List<MovieModel>> loader) {
        }
    };

}