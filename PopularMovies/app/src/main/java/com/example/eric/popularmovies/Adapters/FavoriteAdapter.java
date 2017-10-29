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
import android.widget.ImageView;

import com.example.eric.popularmovies.Models.MovieModel;
import com.example.eric.popularmovies.R;
import com.example.eric.popularmovies.Utils.NetworkUtils;
import com.example.eric.popularmovies.Utils.data.FavoriteContract;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

/**
 *
 * Created by eric on 25/10/2017.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>{
    private ItemClickListener itemClicklistener;
    private Context context;
    private List<MovieModel> mdata;
    private String DF = "favKey";

    public FavoriteAdapter(ItemClickListener itemClicklistener, Context context, List<MovieModel> mdata) {
        this.itemClicklistener = itemClicklistener;
        this.context = context;
        this.mdata = mdata;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fav_poster,parent,false);
        return new FavoriteViewHolder(view);
    }

    public interface ItemClickListener{
        void onItemClickListener( int position, List<MovieModel> models);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        MovieModel object = mdata.get(position);
        String url = String.valueOf((NetworkUtils.buildImageUrl(object.getPosterPath())));
        Picasso.with(context).load(url).placeholder(R.drawable.poster_placeholder).error(R.drawable.error_placeholder).into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView poster;
        ImageView delFav;

        RecyclerView rv;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(context,itemView);
            poster = itemView.findViewById(R.id.poster_main);
            delFav = itemView.findViewById(R.id.remove_fav);
            rv = itemView.findViewById(R.id.fav_rv);
            itemView.setOnClickListener(this);
            delFav.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (view.getId() == delFav.getId()){
                deleteData();
            }
            else {
                int position = getAdapterPosition();
                itemClicklistener.onItemClickListener(position,mdata);
            }

        }

        private void deleteData(){
            MovieModel model = mdata.get(getAdapterPosition());
            int mId = model.getId();
            Uri uri = FavoriteContract.FavEntryList.FAVORITES_CONTENT_URI;
            Uri nUri = uri.buildUpon().appendPath(String.valueOf(mId)).build();
            int fb = context.getContentResolver().delete(nUri,null,null);
            if (fb > 0)
            context.getContentResolver().notifyChange(nUri,null);
            storeFavPreferences(String.valueOf(mId),1);
            Intent intent = ((Activity) context).getIntent();
            ((Activity) context).finish();
            ((Activity) context).overridePendingTransition( 0, 0);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition( 0, 0);


        }
        public void storeFavPreferences(String key, int value) {
            SharedPreferences preferences = context.getSharedPreferences(DF, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }
}
