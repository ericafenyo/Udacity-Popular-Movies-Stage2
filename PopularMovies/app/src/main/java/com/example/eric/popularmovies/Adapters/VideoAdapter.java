package com.example.eric.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eric.popularmovies.Models.VideoModel;
import com.example.eric.popularmovies.R;
import com.example.eric.popularmovies.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

/**
 *
 * Created by eric on 17/10/2017.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterViewHolder>{
    private Context context;
    private List<VideoModel> mData;
    private final ListItemClickListener itemClickListener;


    public VideoAdapter(Context context, List<VideoModel> mData, ListItemClickListener itemClickListener) {
        this.context = context;
        this.mData = mData;
        this.itemClickListener = itemClickListener;
    }

    public  interface ListItemClickListener {
        void onListItemClick(int position, List<VideoModel> models);
    }

    @Override
    public VideoAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_list_item,parent,false);
        return new VideoAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(VideoAdapterViewHolder holder, int position) {
        VideoModel model = mData.get(position);
        holder.name.setText(model.getName());
        holder.size.setText( "Size: " + String.valueOf(model.getSize())+"P");
        String key = model.getKey();
        URL thlUrl = NetworkUtils.buildYoutubeTHLUrl(key);
        Picasso.with(context).load(String.valueOf(thlUrl)).into(holder.videoContainer);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class VideoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private TextView size;
        private ImageView videoContainer;

        public VideoAdapterViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            size =  itemView.findViewById(R.id.size);
            videoContainer = itemView.findViewById(R.id.trailer_container);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            List<VideoModel> data = mData;
            itemClickListener.onListItemClick(position,data);

        }
    }
}
