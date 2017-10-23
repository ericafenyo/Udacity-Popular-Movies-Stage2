package com.example.eric.popularmovies.Utils;

import android.net.Uri;

import com.example.eric.popularmovies.BuildConfig;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by eric on 01/10/2017.
 */

//TODO: comment your codes
public class NetworkUtils {
    private static OkHttpClient client = new OkHttpClient();



    //Movie variables
    private static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String TYPE = "movie";
    private static final String API_QUERY = "api_key";
    private static final String PAGE_NUMBER = "page";

    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE = "w500";

    private static final String VIDEOS_KEY = "videos";
    private static final String REVIEWS_KEY = "reviews";
    private static final String CASTS_KEY = "credits";

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch/";
    private static final String YOUTUBE_QUERY ="v";

    private static final String YOUTUBE_THL_BASE_URL = "http://img.youtube.com/vi/";
    private static final String YOUTUBE_THL_PATH ="mqdefault.jpg";




    //Constructors
    public NetworkUtils() {
        //should be empty
    }

    /**
     * @description returns top rated movie URL
     *
     *
     */
    public static URL buildReviewsUrl(int id,int page) {
        Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(TYPE)
                .appendPath(String.valueOf(id))
                .appendPath(REVIEWS_KEY)
                .appendQueryParameter(API_QUERY,BuildConfig.TMDB_API_KEY)
                .appendQueryParameter(PAGE_NUMBER, String.valueOf(page))
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static URL buildYoutubeUrl( String key) {
        Uri uri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter(YOUTUBE_QUERY,key)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildYoutubeTHLUrl( String key) {
        Uri uri = Uri.parse(YOUTUBE_THL_BASE_URL).buildUpon()
               .appendPath(key)
                .appendPath(YOUTUBE_THL_PATH)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * @description returns most porpular movie URL
     *
     */
    public static URL buildVideosUrl(int id) {
        Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(TYPE)
                .appendPath(String.valueOf(id))
                .appendPath(VIDEOS_KEY)
                .appendQueryParameter(API_QUERY,BuildConfig.TMDB_API_KEY)

                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }




    public static URL buildCastUrl(int id) {
        Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(TYPE)
                .appendPath(String.valueOf(id))
                .appendPath(CASTS_KEY)
                .appendQueryParameter(API_QUERY,BuildConfig.TMDB_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * @description returns movie URL by preferred sort order
     * @param sort_order the movie sorting order
     * @param page the query page number
     */
    public static URL buildSortUrl(String sort_order, int page) {
        Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(TYPE)
                .appendPath(sort_order)
                .appendQueryParameter(API_QUERY,BuildConfig.TMDB_API_KEY)
                .appendQueryParameter(PAGE_NUMBER, String.valueOf(page))
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * @description returns an image URL giving it's path (poster_path)
     * @param img_path TMDB image patch
     */
    public static URL buildImageUrl( String img_path) {
        Uri uri = Uri.parse(POSTER_BASE_URL).buildUpon()
                .appendPath(POSTER_SIZE)
                .appendEncodedPath(img_path)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * @description returns raw json data
     * @param url JSON url
     */
    public static String getHttpResponse(URL url) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
        InputStream inputStream = new BufferedInputStream(connection.getInputStream());

        Scanner scanner = new Scanner(inputStream);
        scanner.useDelimiter("\\A");

        boolean hasInput = scanner.hasNext();
        if (hasInput) {
            return scanner.next();
        } else {
            return null;
        }
        }finally{
            connection.disconnect();
        }
    }

    public static String getHttpResponses(URL url) throws IOException{
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
