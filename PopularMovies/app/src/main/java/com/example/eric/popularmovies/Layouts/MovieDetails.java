package com.example.eric.popularmovies.Layouts;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.eric.popularmovies.Adapters.ObjectAdapter;
import com.example.eric.popularmovies.R;
import com.example.eric.popularmovies.Utils.GenreUtils;
import com.example.eric.popularmovies.Utils.NetworkUtils;
import com.example.eric.popularmovies.Utils.data.ObjectDataLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.eric.popularmovies.R.id.movie_genre;

public class MovieDetails extends AppCompatActivity{

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

    private SQLiteDatabase database;
    private  CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
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
                    collapsingToolbarLayout.setTitle("Movie Details");
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        getSupportLoaderManager().initLoader(0,null,objectCallbackTest);

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


        //retrieve data from PutExtra initiated in the Adapter class
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String title = intent.getExtras().getString("original_title");
            String rate = intent.getExtras().getString("vote_average");
            String overview = intent.getExtras().getString("overview");
            String releaseDate = intent.getExtras().getString("release_date");
            String poster = intent.getExtras().getString("poster_path");
            String backdrop = intent.getExtras().getString("backdrop_path");
            int genre = intent.getExtras().getInt("genre_ids");
            int page_number = intent.getExtras().getInt("total_pages");
//            Log.d("This id", String.valueOf(movie_id));


            //Setting the data onto  this'.views
            String posterUrl = String.valueOf(NetworkUtils.buildImageUrl(poster));
            String backdropUrl = String.valueOf(NetworkUtils.buildImageUrl(backdrop));
            Picasso.with(this).load(backdropUrl).placeholder(R.drawable.poster_placeholder).error(R.drawable.error_placeholder).into(backdrop_iv);
//            Picasso.with(this).load(posterUrl).placeholder(R.drawable.poster_placeholder).error(R.drawable.error_placeholder).into(poster_iv);

                Glide.with(this).load(posterUrl).asBitmap().into(new BitmapImageViewTarget(poster_iv) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        super.onResourceReady(bitmap, glideAnimation);
                        setColorFromImage(bitmap);
                    }
                });

            originalTitle_tv.setText(title);
            originalTitle_tv.setSelected(true);
            releaseDate_tv.setText(releaseDate);
            overview_tv.setText(overview);
            voteAverage_tv.setText(rate);
            String movie_genre = new GenreUtils().convertGengre(genre);
            genre_tv.setText(movie_genre);

        }else {
            Toast.makeText(this,"NO DATA FOUND",Toast.LENGTH_LONG).show();
        }

        NetworkUtils.buildYoutubeUrl("");

    }


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



    private LoaderManager.LoaderCallbacks<List<Object>> objectCallbackTest = new LoaderManager.LoaderCallbacks<List<Object>>() {

        @Override
        public Loader<List<Object>> onCreateLoader(int id, Bundle args) {
            Intent i = getIntent();
            int movie_id = i.getExtras().getInt("movies_id");
            return new ObjectDataLoader(MovieDetails.this,movie_id);
        }

        @Override
        public void onLoadFinished(Loader<List<Object>> loader, List<Object> data) {

            mainAdapter = new ObjectAdapter(MovieDetails.this,data);
            mainRecyclerView.setAdapter(mainAdapter);
            mainRecyclerView.setLayoutManager(new LinearLayoutManager(MovieDetails.this));
        }

        @Override
        public void onLoaderReset(Loader<List<Object>> loader) {

        }

    };


}
