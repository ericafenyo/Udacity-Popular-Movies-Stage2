package com.example.eric.popularmovies.Utils.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.AsyncTaskLoader;

import com.example.eric.popularmovies.Models.Review;
import com.example.eric.popularmovies.Models.Video;
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
 * Created by eric on 18/10/2017.
 * Gets reviews and Video responses from server
 */

public class ObjectDataLoader extends AsyncTaskLoader<List<Object>> {

    private static final String VIDEO_NAME ="name";
    private static final String VIDEO_KEY ="key";
    private static final String VIDEO_SIZE ="size";
    private static final String ARRAY_ROOT = "results";

    private static final String REVIEW_AUTHOR ="author";
    private static final String REVIEW_CONTENT ="content";
    private static final String REVIEW_URL ="url";
    private static final String NUMBER_OF_PAGES = "total_pages";
    private int id;
    private URL videoUrl;
    private URL reviewUrl;
    private List<Object> cached;

    public ObjectDataLoader(Context context, int id) {

        super(context);
        this.id = id;
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
    public List<Object> loadInBackground() {

        ConnectivityManager connectivityManager = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = false;

        if (activeNetwork != null) {
            isConnected = activeNetwork != null & activeNetwork.isConnectedOrConnecting();
        }
        List<Object> objects = new ArrayList<>();

        //getting and parsing the response from server
        if (isConnected) {
            videoUrl = NetworkUtils.buildVideosUrl(id);
            reviewUrl = NetworkUtils.buildReviewsUrl(id, 1);
            try {
                String v_Response = NetworkUtils.getHttpResponse(videoUrl);
                String r_Response = NetworkUtils.getHttpResponse(reviewUrl);

                //getting data for Videos;
                JSONObject v_root = new JSONObject(v_Response);
                JSONArray v_results = v_root.getJSONArray(ARRAY_ROOT);

                List<Video> v_data = new ArrayList<>();
                for (int i = 0; i < v_results.length(); i++) {
                    JSONObject object = v_results.getJSONObject(i);
                    String name = object.getString(VIDEO_NAME);
                    String key = object.getString(VIDEO_KEY);
                    int size = object.getInt(VIDEO_SIZE);
                    v_data.add(new Video(name, key, size));
                }

                JSONObject root = new JSONObject(r_Response);
                JSONArray results = root.getJSONArray(ARRAY_ROOT);

                List<Review> r_data = new ArrayList<>();
                for (int i = 0; i < results.length(); i++) {
                    JSONObject object = results.getJSONObject(i);
                    String author = object.getString(REVIEW_AUTHOR);
                    String content = object.getString(REVIEW_CONTENT);
                    String link = object.getString(REVIEW_URL);
                    r_data.add(new Review(author, content, link));

                }

                //Adding Videos at position 0
                objects.add(v_data);
                //Adding Reviews at position 1
                objects.add(r_data);

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }else {
            return null;
        }
            return objects;

}
    @Override
    public void deliverResult(List<Object> data) {
        cached = data;
        super.deliverResult(data);
    }
}
