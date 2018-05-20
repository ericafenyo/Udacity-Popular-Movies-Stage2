package com.example.eric.popularmovies.Layouts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eric.popularmovies.Adapters.MovieAdapter;
import com.example.eric.popularmovies.Models.Movie;
import com.example.eric.popularmovies.Utils.ActivityUtil;
import com.example.eric.popularmovies.R;
import com.example.eric.popularmovies.Utils.data.MovieDataLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    //SharedPreferences' variables
    private static final String SORTS = "sort_by";
    private static final String KEY = "order";
    private static final String DEFAULT_ORDER = "popular";
    private static final String TOP_RATED_ORDER = "top_rated";
    SharedPreferences preferences;
    SharedPreferences.Editor preferenceEditor;

    GridLayoutManager layoutManager;

    @BindView(R.id.main_toolbar) Toolbar toolbar;
    @BindView(R.id.movie_rv)RecyclerView recyclerView;
    @BindView(R.id.error_tv)TextView error_tv;
    @BindView(R.id.progress_bar)ProgressBar progressBar;
    @BindView(R.id.retry_button)Button retryBn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //binding views
        ButterKnife.bind(this);

        //setting toolbar
        setSupportActionBar(toolbar);

        recyclerView.setFocusable(false);

        //checks for sort order
        checkSortOrder();

        //setting LayoutManager
        setLayoutManager();

        retryBn.setOnClickListener(conRetry);

    }/** end of onCreate **/

    //OnclickItemListener
    @Override
    public void onClick(int position, List<Movie> movies, ImageView poster) {
        sendIntentData(position, movies);
    }

    //LayoutManager settings depending on device type and orientation
    public void setLayoutManager(){
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (!isTablet){
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                layoutManager = new GridLayoutManager(MainActivity.this, 2);
                recyclerView.setLayoutManager(layoutManager);
            } else {
                layoutManager = new GridLayoutManager(MainActivity.this, 4);
                recyclerView.setLayoutManager(layoutManager);
            }
        }else if (isTablet){
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                layoutManager = new GridLayoutManager(MainActivity.this, 4);
                recyclerView.setLayoutManager(layoutManager);
            } else {
                layoutManager = new GridLayoutManager(MainActivity.this, 6);
                recyclerView.setLayoutManager(layoutManager);
            }
        }
    }

    //revalidate the sorting value in sharedPreference and calls "sortBy" method
    public void checkSortOrder(){
        if (!getSortPreferences(KEY,DEFAULT_ORDER).equals(DEFAULT_ORDER)){
            sortBy(getSortPreferences(KEY,TOP_RATED_ORDER));
            this.setTitle(R.string.top_rated);
        }else {
            sortBy(DEFAULT_ORDER);
        }
    }

    /*calls when there is no internet connectivity
     It Refreshes the Activity once there is a connection */
    private View.OnClickListener conRetry = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isConnected = checkConnectivity();
            if (isConnected){
                finish();
                startActivity(getIntent());
            }else {
                ActivityUtil.makeToast(MainActivity.this,getString(R.string.msg_no_internet_connection));
            }
        }
    };

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

        int id = item.getItemId();
        switch (id){
           // sorts by popularity
            case R.id.sort_popular:
                if (getSortPreferences(KEY, DEFAULT_ORDER).equals(TOP_RATED_ORDER)){
                    finish();
                    startActivity(getIntent());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sortBy(DEFAULT_ORDER);
                        }
                    },100);
                    storeSortPreferences(KEY,DEFAULT_ORDER);
                }
                storeSortPreferences(KEY,DEFAULT_ORDER);
                break;

            //sorts by Top Rated
            case R.id.sort_top_rated:
            if (getSortPreferences(KEY, TOP_RATED_ORDER).equals(DEFAULT_ORDER)){
                this.finish();
                startActivity(getIntent());
                final Handler handler= new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sortBy(TOP_RATED_ORDER);
                    }
                },100);
               }

            if (getSortPreferences(KEY, DEFAULT_ORDER).equals(DEFAULT_ORDER)){
                    storeSortPreferences(KEY,TOP_RATED_ORDER);
                finish();
                startActivity(getIntent());
                }
                storeSortPreferences(KEY,TOP_RATED_ORDER);
                break;

            case R.id.fav:
                Intent intent = new Intent(this,FavoriteActivity.class);
                startActivity(intent);

                break;

            case R.id.about:
                Intent sIntent = new Intent(this,AboutActivity.class);
                startActivity(sIntent);

                break;
            }

        return super.onOptionsItemSelected(item);
    }

    //shows error message
    public void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        error_tv.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        retryBn.setVisibility(View.VISIBLE);
    }

    //Displays data if all conditions are favorable
    private void showData() {
        recyclerView.setVisibility(View.VISIBLE);
        error_tv.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
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

    /**
     * @param position  the adapter position
     * @param mdata Lists of Movie data
     * @description sends data to MovieDetails class
     */

    public void sendIntentData(int position, List<Movie> mdata) {
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

    /**
     * @param sort  type of sorting order
     * @description populates the recyclerView based on the sorting order given
     */
    public void sortBy(final String sort){
        getSupportLoaderManager().initLoader(1, null, new LoaderManager.LoaderCallbacks<List<Movie>>() {
            @Override
            public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
                return new MovieDataLoader(MainActivity.this,6,sort);
            }

            @Override
            public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
               boolean isConnected = checkConnectivity();
                if (!isConnected){
                    error_tv.setText("No Connection");
                    showErrorMessage();
                }else if (isConnected && data == null){
                    showErrorMessage();
                }
                else {
                    showData();
                    MovieAdapter adapter = new MovieAdapter(MainActivity.this,data, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                }
            }

            @Override
            public void onLoaderReset(Loader<List<Movie>> loader) {

            }
        });
    }

    //returns a boolean based on the internet connectivity of the device
    public boolean checkConnectivity(){
        ConnectivityManager cm =(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}