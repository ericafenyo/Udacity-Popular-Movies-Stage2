package com.example.eric.popularmovies.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eric.popularmovies.Models.Review;
import com.example.eric.popularmovies.Models.Video;
import com.example.eric.popularmovies.R;
import com.example.eric.popularmovies.Utils.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.id;

/**
 *
 * Created by eric on 18/10/2017.
 */

public class ObjectAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements VideoAdapter.ListItemClickListener {

    private final static int VIEW_TYPE_VIDEOS = 0;
    private final static int VIEW_TYPE_REVIEWS = 1;

    private Context mContext;
    private List<Object> mData;


    public ObjectAdapter(Context context, List<Object> objects) {
        this.mContext = context;
        this.mData = objects;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layoutId;
        View view;

        switch (viewType) {
            case VIEW_TYPE_VIDEOS:
                layoutId = R.layout.videos;
                view = LayoutInflater.from(mContext).inflate(layoutId,parent,false);
                return new MyVideosViewHolder(view);

            case VIEW_TYPE_REVIEWS:
                layoutId = R.layout.reviews;
                view = LayoutInflater.from(mContext).inflate(layoutId,parent,false);
                return new MyReviewViewHolder(view);

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return VIEW_TYPE_VIDEOS;
        }else if (position == 1){
            return VIEW_TYPE_REVIEWS;
        }else {
            return -1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case VIEW_TYPE_REVIEWS:
                bindReviews((MyReviewViewHolder) holder);
                break;
            case VIEW_TYPE_VIDEOS:
                bindVideo((MyVideosViewHolder) holder);

        }
    }


    private void bindVideo(MyVideosViewHolder vidHolder){
        VideoAdapter adapter = new VideoAdapter(mContext, (List<Video>) mData.get(0),this);
        vidHolder.vRecyclerView.setAdapter(adapter);
        vidHolder.vRecyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        vidHolder.vRecyclerView.setHasFixedSize(true);
        vidHolder.vRecyclerView.setNestedScrollingEnabled(false);
        vidHolder.vRecyclerView.setFocusable(false);
    }


    private void bindReviews(MyReviewViewHolder revHolder){
        ReviewAdapter adapter = new ReviewAdapter(mContext, (List<Review>) mData.get(1));
        revHolder.rRecyclerView.setAdapter(adapter);
        revHolder.rRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        revHolder.rRecyclerView.setHasFixedSize(true);
        revHolder.rRecyclerView.setNestedScrollingEnabled(false);
        revHolder.rRecyclerView.setFocusable(false);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }



    @Override
    public void onListItemClick(int position, List<Video> models) {
        Video model = models.get(position);
        String youTubeUrl = String.valueOf(NetworkUtils.buildYoutubeUrl(model.getKey()));
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + model.getKey()));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(youTubeUrl + id));
            try {
                mContext.startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                mContext.startActivity(webIntent);
            }

    }

    //Video ViewHolder
    public class MyVideosViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.video_recyclerView)
        RecyclerView vRecyclerView;
        public MyVideosViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    //Reviews ViewHolder
    public class MyReviewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.review_recyclerView)
        RecyclerView rRecyclerView;
        public MyReviewViewHolder(View itemView) {
            super(itemView);
          ButterKnife.bind(this,itemView);
        }
    }
}
