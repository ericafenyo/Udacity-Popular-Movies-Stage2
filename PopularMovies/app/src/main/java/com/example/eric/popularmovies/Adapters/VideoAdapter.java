package com.example.eric.popularmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eric.popularmovies.Models.Video;
import com.example.eric.popularmovies.R;
import com.example.eric.popularmovies.Utils.NetworkUtils;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eric on 17/10/2017.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private Context mContext;
    private List<Video> mData;
    private final ListItemClickListener mItemClickListener;


    public VideoAdapter(Context context, List<Video> videos, ListItemClickListener itemClickListener) {
        this.mContext = context;
        this.mData = videos;
        this.mItemClickListener = itemClickListener;
    }

    public interface ListItemClickListener {
        void onListItemClick(int position, List<Video> videos);
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.video_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        Video video = mData.get(position);
        holder.tvMovieTitle.setText(video.getName());
        holder.tvVideoResolution.setText("Size: " + String.valueOf(video.getSize()) + "P");
        String key = video.getKey();
        String youtubeThumbnailUrl = String.valueOf(NetworkUtils.buildYoutubeTHLUrl(key));

        Glide.with(mContext)
                .load(youtubeThumbnailUrl)
                .into(holder.ivVideoThumbnail);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_movie_title) TextView tvMovieTitle;
        @BindView(R.id.text_video_resolution) TextView tvVideoResolution;
        @BindView(R.id.image_video_thumbnail) ImageView ivVideoThumbnail;
        @BindView(R.id.button_share_video) Button btnShare;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
            btnShare.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (view.getId() == btnShare.getId()) {
                Video model = mData.get(getAdapterPosition());
                String key = model.getKey();
                URL youtubeUrl = NetworkUtils.buildYoutubeUrl(key);
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, String.valueOf(youtubeUrl));
                mContext.startActivity(Intent.createChooser(sharingIntent, "Share using"));

            } else {
                int position = getAdapterPosition();
                List<Video> data = mData;
                mItemClickListener.onListItemClick(position, data);
            }
        }
    }
}
