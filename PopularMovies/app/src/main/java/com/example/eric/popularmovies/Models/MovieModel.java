package com.example.eric.popularmovies.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by eric on 02/10/2017.
 */

public class MovieModel implements Parcelable{
    private String originalTitle;
    private String backdropPath;
    private String voteAverage;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private int id;
    private int totalPages;
    private int genreIds;

    //Constructor
    public MovieModel() {
    }

    public MovieModel(String originalTitle, String backdropPath, String voteAverage, String posterPath, String overview, String releaseDate, int id, int totalPages, int genreIds) {
        this.originalTitle = originalTitle;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.totalPages = totalPages;
        this.genreIds = genreIds;
    }

    protected MovieModel(Parcel in) {
        originalTitle = in.readString();
        backdropPath = in.readString();
        voteAverage = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        id = in.readInt();
        totalPages = in.readInt();
        genreIds = in.readInt();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getId() {
        return id;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getGenreIds() {
        return genreIds;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(originalTitle);
        parcel.writeString(backdropPath);
        parcel.writeString(voteAverage);
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeInt(id);
        parcel.writeInt(totalPages);
        parcel.writeInt(genreIds);
    }
}
