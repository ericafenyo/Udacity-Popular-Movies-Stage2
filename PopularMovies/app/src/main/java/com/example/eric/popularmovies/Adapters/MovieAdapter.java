package com.example.eric.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.eric.popularmovies.Models.MovieModel;
import com.example.eric.popularmovies.R;
import com.example.eric.popularmovies.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * created by eric on 01/10/2017
 * My Movie Adapter Class
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    List<MovieModel> movies;
    Context context;
    final ListItemClickListener mClickItemListener;


    //Constructor
    public MovieAdapter(List<MovieModel> movies, Context context, ListItemClickListener mClickItemListener) {
        this.movies = movies;
        this.context = context;
        this.mClickItemListener = mClickItemListener;
    }


    public interface ListItemClickListener {
        void OnListItemClick(int position, List<MovieModel> movieModels, ImageView poster);

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_poster, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        MovieModel object = movies.get(position);
        String url = String.valueOf((NetworkUtils.buildImageUrl(object.getPosterPath())));
        Picasso.with(context).load(url).placeholder(R.drawable.poster_placeholder).error(R.drawable.error_placeholder).into(holder.movie_poster);
        //ViewCompat.setTransitionName(holder.movie_poster, "transitionName");
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView movie_poster;
        ImageView detail_poster;


        public MovieViewHolder(final View itemView) {
            super(itemView);
            movie_poster = itemView.findViewById(R.id.poster_main);
            movie_poster.setTransitionName("transitionName");
            detail_poster = itemView.findViewById(R.id.detail_poster);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            List<MovieModel> data = movies;
            mClickItemListener.OnListItemClick(position, data, movie_poster);

        }



    }

    public  void addto(List<MovieModel> mod) {
        movies = mod;
    }

}
