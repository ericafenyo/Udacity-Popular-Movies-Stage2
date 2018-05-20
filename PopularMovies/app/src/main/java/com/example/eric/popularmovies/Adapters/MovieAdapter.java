package com.example.eric.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.eric.popularmovies.Models.Movie;
import com.example.eric.popularmovies.R;
import com.example.eric.popularmovies.Utils.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by eric on 01/10/2017
 * My Movie Adapter Class
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context mContext;
    private List<Movie> mData;
    private ListItemClickListener mItemClickListener;

    //Constructor
    public MovieAdapter(Context context, List<Movie> movies, ListItemClickListener itemClickListener) {
        this.mContext = context;
        this.mData = movies;
        this.mItemClickListener = itemClickListener;
    }

    public interface ListItemClickListener {
        void onClick(int position, List<Movie> movieModels, ImageView poster);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_poster, parent, false));
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        Movie movie = mData.get(position);
        String moviePosterUrl = String.valueOf((NetworkUtils.buildImageUrl(movie.getPosterPath())));

        Glide.with(mContext)
                .load(moviePosterUrl)
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.error_placeholder)
                .into(holder.ivMoviePoster);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image_movie_poster) ImageView ivMoviePoster;

        public MovieViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mItemClickListener.onClick(getAdapterPosition(), mData, ivMoviePoster);
        }
    }
}
