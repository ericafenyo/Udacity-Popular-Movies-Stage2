package com.example.eric.popularmovies.Utils.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.AsyncTaskLoader;

import com.example.eric.popularmovies.Models.MovieModel;
import com.example.eric.popularmovies.Utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by eric on 17/10/2017.
 */

public class MovieDataLoader extends AsyncTaskLoader<List<MovieModel>>{
    private static List<MovieModel> data;
    MovieModel model;
    private static final String TITLE ="original_title";
    private static final String BACKDROP_PATH ="backdrop_path";
    private static final String POSTER_PATH = "poster_path";
    private static final String OVERVIEW ="overview";
    private static final String RELEASE_DATE ="release_date";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String ARRAY_ROOT = "results";
    private static final String GENRE_ID = "genre_ids";
    private static final String MOVIE_ID = "id";
    private static final String TOTAL_PAGES = "total_pages";
    private String title;
    private String posterPath;
    private String overview;
    private String voteAverage;
    private String releaseDate;
    private String backdropPath;
    private int page;
    private String sort;
    private List<MovieModel> cached;

    public MovieDataLoader(Context context, int page,String sort) {

        super(context);
        this.page = page; // the request page number
        this.sort = sort; // the movie sort order
    }
    @Override
    protected void onStartLoading() {

        if (cached == null){
            forceLoad();
        }else {
            super.deliverResult(cached);
        }
    }

    @Override
    public List<MovieModel> loadInBackground() {

        ConnectivityManager connectivityManager = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = false;

        if (activeNetwork != null) {
            isConnected = activeNetwork != null & activeNetwork.isConnectedOrConnecting();
        }

        if (isConnected){

        URL url = NetworkUtils.buildSortUrl(sort,page);
        model = new MovieModel();
        data = new ArrayList<>();

            //getting and parsing the response from server
        try {
            String response = NetworkUtils.getHttpResponse(url);
            JSONObject root = new JSONObject(response);
            JSONArray results = root.getJSONArray(ARRAY_ROOT);
            int totalPages = root.getInt(TOTAL_PAGES);

            for (int i = 0; i < results.length(); i++){
                JSONObject object = results.getJSONObject(i);

                int genre = 0;
                JSONArray genreArray = object.getJSONArray(GENRE_ID);
                for (int x = 0;x < genreArray.length(); x++){
                    genre = genreArray.getInt(x);
                }
                title = object.getString(TITLE);
                posterPath = object.getString(POSTER_PATH);
                overview = object.getString(OVERVIEW);
                voteAverage = object.getString(VOTE_AVERAGE);
                releaseDate = object.getString(RELEASE_DATE);
                backdropPath = object.getString(BACKDROP_PATH);
                int id = object.getInt(MOVIE_ID);
                data.add(new MovieModel(title,backdropPath,voteAverage,posterPath,overview,releaseDate,id,totalPages,genre));
            }
            return data;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        }
        return null;
    }

    @Override
    public void deliverResult(List<MovieModel> data) {
        cached = data;
        super.deliverResult(data);
    }




}
