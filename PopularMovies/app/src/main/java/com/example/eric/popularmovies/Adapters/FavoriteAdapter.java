package com.example.eric.popularmovies.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.eric.popularmovies.Models.Movie;
import com.example.eric.popularmovies.R;
import com.example.eric.popularmovies.Utils.NetworkUtils;
import com.example.eric.popularmovies.Utils.data.FavoriteContract;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by eric on 25/10/2017.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private Context mContext;
    private List<Movie> mData;
    private ListItemClickListener mItemClickListener;
    private String DF = "favKey";

    public FavoriteAdapter(ListItemClickListener itemClicklistener, Context context, List<Movie> movies) {
        this.mItemClickListener = itemClicklistener;
        this.mContext = context;
        this.mData = movies;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavoriteViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.fav_poster, parent, false));
    }

    public interface ListItemClickListener {
        void onClick(int position, List<Movie> movies);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        Movie object = mData.get(position);
        String url = String.valueOf((NetworkUtils.buildImageUrl(object.getPosterPath())));
        Glide.with(mContext).load(url).placeholder(R.drawable.poster_placeholder).error(R.drawable.error_placeholder).into(holder.ivMoviePoster);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image_movie_poster) ImageView ivMoviePoster;
        @BindView(R.id.button_delete_favorite) ImageButton btnDeleteFavorite;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
            btnDeleteFavorite.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == btnDeleteFavorite.getId()) {
                deleteData();
            } else {
                int position = getAdapterPosition();
                mItemClickListener.onClick(position, mData);
            }
        }

        private void deleteData() {
            Movie model = mData.get(getAdapterPosition());
            int mId = model.getId();
            Uri uri = FavoriteContract.FavEntryList.FAVORITES_CONTENT_URI;
            Uri nUri = uri.buildUpon().appendPath(String.valueOf(mId)).build();
            int fb = mContext.getContentResolver().delete(nUri, null, null);
            if (fb > 0)
                mContext.getContentResolver().notifyChange(nUri, null);
            storeFavPreferences(String.valueOf(mId), 1);
            Intent intent = ((Activity) mContext).getIntent();
            ((Activity) mContext).finish();
            ((Activity) mContext).overridePendingTransition(0, 0);
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(0, 0);
        }

        public void storeFavPreferences(String key, int value) {
            SharedPreferences preferences = mContext.getSharedPreferences(DF, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }
}
