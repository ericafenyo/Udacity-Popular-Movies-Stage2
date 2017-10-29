package com.example.eric.popularmovies.Layouts;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.eric.popularmovies.Adapters.ObjectAdapter;
import com.example.eric.popularmovies.Notify;
import com.example.eric.popularmovies.R;
import com.example.eric.popularmovies.Utils.DateUtils;
import com.example.eric.popularmovies.Utils.GenreUtils;
import com.example.eric.popularmovies.Utils.NetworkUtils;
import com.example.eric.popularmovies.Utils.data.FavoriteContract;
import com.example.eric.popularmovies.Utils.data.ObjectDataLoader;
import com.example.eric.popularmovies.Utils.data.SingleFavoriteDataLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.eric.popularmovies.R.id.movie_genre;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_BACKDROP;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_DATE;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_GENRE_ID;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_OVERVIEW;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_POSTER;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_RATE;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.COLUMN_TITLE;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList.FAVORITES_CONTENT_URI;
import static com.example.eric.popularmovies.Utils.data.FavoriteContract.FavEntryList._ID;

public class MovieDetails extends AppCompatActivity {

    Toolbar toolbar;
    TextView originalTitle_tv;
    TextView voteAverage_tv;
    ImageView poster_iv;
    TextView overview_tv;
    TextView releaseDate_tv;
    ImageView backdrop_iv;
    TextView genre_tv;
    TextView overview_header;

    RelativeLayout relativeLayout;
    ScrollView scrollView;

    ObjectAdapter mainAdapter;
    RecyclerView mainRecyclerView;
    FloatingActionButton favBn;
    Notify notify = new Notify(this);

    private  CollapsingToolbarLayout collapsingToolbarLayout;
    private String title;
    private String rate;
    private String overview;
    private String releaseDate;
    private String poster;
    private String backdrop;
    private int genre;
    private int pageNumber;
    private int movieId;

    CoordinatorLayout coordinatorLayout;
    AppBarLayout appBarLayout;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private String DF = "favKey";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // setting toolbar
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //setting up the CollapsingToolbarLayout
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        setToolbarConf();

        //would have replaced them with ButterKnife but it's having problems binding some Views
        originalTitle_tv = (TextView) findViewById(R.id.movie_title);
        voteAverage_tv = (TextView) findViewById(R.id.movie_user_rating);
        overview_tv = (TextView) findViewById(R.id.movie_overview);
        releaseDate_tv = (TextView) findViewById(R.id.movie_release_date);
        poster_iv = (ImageView) findViewById(R.id.detail_poster);
        backdrop_iv = (ImageView) findViewById(R.id.backdrop_image);
        genre_tv = (TextView) findViewById(movie_genre);
        relativeLayout = (RelativeLayout) findViewById(R.id.rlayout);
        overview_header = (TextView) findViewById(R.id.overview_header);
        favBn = (FloatingActionButton) findViewById(R.id.fab);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        mainRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.detail_coordinator);

        //retries data from PutExtra initiated in Main or Favorite Activity;
        getMovieItems();

        //LoaderCallbacks
        getSupportLoaderManager().initLoader(0,null,objectCallbackTest);
        getSupportLoaderManager().initLoader(4,null,checkColor);


        //Favorite FloatingActionButton
        favBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFav(String.valueOf(movieId));
            }
        });
    }


    //retrieving data from PutExtra initiated in Main and Favorite Activity;
    public void getMovieItems(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            title = intent.getExtras().getString("original_title");
            rate = intent.getExtras().getString("vote_average");
            overview = intent.getExtras().getString("overview");
            releaseDate = intent.getExtras().getString("release_date");
            poster = intent.getExtras().getString("poster_path");
            backdrop = intent.getExtras().getString("backdrop_path");
            genre = intent.getExtras().getInt("genre_ids");
            pageNumber = intent.getExtras().getInt("total_pages");
            movieId = intent.getExtras().getInt("movies_id");

            //Setting the data onto  this'.views
            String posterUrl = String.valueOf(NetworkUtils.buildImageUrl(poster));
            String backdropUrl = String.valueOf(NetworkUtils.buildImageUrl(backdrop));
            Picasso.with(this).load(backdropUrl).placeholder(R.drawable.poster_placeholder).error(R.drawable.error_placeholder).into(backdrop_iv);
//          Picasso.with(this).load(posterUrl).placeholder(R.drawable.poster_placeholder).error(R.drawable.error_placeholder).into(poster_iv);

            //slower than Picasso but easier to assess the Bitmaps
            Glide.with(this).load(posterUrl).asBitmap().into(new BitmapImageViewTarget(poster_iv) {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                    super.onResourceReady(bitmap, glideAnimation);
                    setColorFromImage(bitmap);
                }
            });

            //setting values to their respective Views
            String convectedDate = new DateUtils().getDate(releaseDate);
            String movie_genre = new GenreUtils().convertGengre(genre);
            genre_tv.setText(movie_genre);
            releaseDate_tv.setText(convectedDate);
            originalTitle_tv.setText(title);
            originalTitle_tv.setSelected(true);
            overview_tv.setText(overview);
            voteAverage_tv.setText(rate);

        }else {
            notify.makeToast("NO DATA FOUND");
        }
    }

    //CollapsingToolbarLayout Config.
    public void setToolbarConf(){
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;


            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                collapsingToolbarLayout.setTitle("");
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(title);
                    collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(getApplicationContext(),R.color.cardview_dark_background));
                    favBn.setVisibility(View.INVISIBLE);
                    isShow = true;
                    final Drawable upArrow = ContextCompat.getDrawable(getApplicationContext(),R.drawable.abc_ic_ab_back_material);
                    upArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.cardview_dark_background), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);
                } else if(isShow) {
                    favBn.setVisibility(View.VISIBLE);
                    collapsingToolbarLayout.setTitle(" ");
                    final Drawable upArrow = ContextCompat.getDrawable(getApplicationContext(),R.drawable.abc_ic_ab_back_material);
                    upArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.white), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);

                    isShow = false;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(4,null,checkColor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            case R.id.fav:
               Intent intent = new Intent(this, Favorite.class);
                startActivity(intent);
                break;

        case R.id.about:
        Intent sIntent = new Intent(this,AboutActivity.class);
        startActivity(sIntent);
        break;
        }
        return true;
    }

    //Inserts data Into the Database
    public void insertData(){
        if (title.length() == 0){
            return;
        }

        ContentValues values = new ContentValues();
        values.put(_ID,movieId);
        values.put(COLUMN_TITLE,title);
        values.put(COLUMN_GENRE_ID,genre);
        values.put(COLUMN_DATE,releaseDate);
        values.put(COLUMN_RATE,rate);
        values.put(COLUMN_OVERVIEW,overview);
        values.put(COLUMN_POSTER,poster);
        values.put(COLUMN_BACKDROP,backdrop);

        Uri uri = getContentResolver().insert(FAVORITES_CONTENT_URI,values);
        if (uri != null){
            getContentResolver().notifyChange(uri,null);
            notify.makeSnack(coordinatorLayout,"Added to Favorites");
            setIconRed();
        }
    }

    //Deletes data From the Database
    private void deleteData(){
        Uri uri = FavoriteContract.FavEntryList.FAVORITES_CONTENT_URI;
        Uri nUri = uri.buildUpon().appendPath(String.valueOf(movieId)).build();
        int fb = getContentResolver().delete(nUri,null,null);

        if (fb > 0)
            getContentResolver().notifyChange(nUri,null);
        notify.makeSnack(coordinatorLayout,"Removed From Favorites");
            setIconGray();
    }

    //Using Google.Pallets To extract from Bitmaps
    public void setColorFromImage(final Bitmap bitmap){
        if (bitmap != null) {
            Palette.from(bitmap).maximumColorCount(1000).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch swatch = palette.getDarkVibrantSwatch();
                    if (swatch != null) {
                        relativeLayout.setBackgroundColor(swatch.getRgb());
                        genre_tv.setTextColor(swatch.getBodyTextColor());
                        overview_tv.setTextColor(swatch.getBodyTextColor());
                        voteAverage_tv.setTextColor(swatch.getBodyTextColor());
                        overview_header.setTextColor(swatch.getTitleTextColor());
                        genre_tv.setTextColor(swatch.getBodyTextColor());
                        originalTitle_tv.setTextColor(swatch.getTitleTextColor());
                        releaseDate_tv.setTextColor(swatch.getBodyTextColor());

                    }
                }
            });
        }
    }

    //Object LoaderCallbacks
    private LoaderManager.LoaderCallbacks<List<Object>> objectCallbackTest = new LoaderManager.LoaderCallbacks<List<Object>>() {

        @Override
        public Loader<List<Object>> onCreateLoader(int id, Bundle args) {
            Intent i = getIntent();
            int movie_id = i.getExtras().getInt("movies_id");
            return new ObjectDataLoader(MovieDetails.this,movie_id);
        }

        @Override
        public void onLoadFinished(Loader<List<Object>> loader, List<Object> data) {

            if (data !=null) {
                mainAdapter = new ObjectAdapter(MovieDetails.this,data);
                mainRecyclerView.setAdapter(mainAdapter);
                mainRecyclerView.setLayoutManager(new LinearLayoutManager(MovieDetails.this));
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Object>> loader) {
        }

    };

    /**Boolean LoaderCallbacks
    * Returns a boolean to whether a specific row of data is present in the database or not */

    private LoaderManager.LoaderCallbacks<Boolean> checkColor = new LoaderManager.LoaderCallbacks<Boolean>() {
        int mId;
        @Override
        public Loader<Boolean> onCreateLoader(int id, Bundle args) {
            Intent intent = getIntent();
            mId = intent.getExtras().getInt("movies_id");
            return new SingleFavoriteDataLoader(MovieDetails.this,String.valueOf(mId));
        }

        @Override
        public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
            if (!data){
                setIconGray();
            }
            else setIconRed();
        }

        @Override
        public void onLoaderReset(Loader<Boolean> loader) {

        }
    };

    //stores Favorite Custom Preferences
    public void storeFavPreferences(String key, int value) {
        preferences = getSharedPreferences(DF, MODE_PRIVATE);
        editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    //Retrieves stored SharedPreferences value
    public int getFavPreferences(String key, int deft) {
        preferences = getSharedPreferences(DF, MODE_PRIVATE);
        return preferences.getInt(key, deft);
    }

//   method for setting favorites
    public void setFav(String id){
        int pref = getFavPreferences(String.valueOf(id),1);
        if (pref == 1) {
            storeFavPreferences(String.valueOf(id),0);
            setIconGray();
            insertData();

        }
        else if (pref == 0) {
            storeFavPreferences(String.valueOf(id),1);
            setIconRed();
            deleteData();
        }
    }

    public void setIconRed (){
        Drawable myFabSrc = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_fav);
        Drawable willBeRed = myFabSrc.getConstantState().newDrawable();
        willBeRed.mutate().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        favBn.setImageDrawable(willBeRed);

    }

    public void setIconGray (){
        Drawable myFabSrc = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_fav);
        Drawable willBeGray = myFabSrc.getConstantState().newDrawable();
        willBeGray.mutate().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        favBn.setImageDrawable(willBeGray);

    }

}
