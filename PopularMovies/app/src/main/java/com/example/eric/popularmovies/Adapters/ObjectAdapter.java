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

import com.example.eric.popularmovies.Models.ReviewModel;
import com.example.eric.popularmovies.Models.VideoModel;
import com.example.eric.popularmovies.R;
import com.example.eric.popularmovies.Utils.NetworkUtils;

import java.util.List;

import static android.R.attr.id;

/**
 *
 * Created by eric on 18/10/2017.
 */

public class ObjectAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements VideoAdapter.ListItemClickListener {
    private Context context;
    private List<Object> mdata;
    private final static int VIDEOS = 0;
    private final static int REVIEWS = 1;

    public ObjectAdapter(Context context, List<Object> mdata) {
        this.context = context;
        this.mdata = mdata;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        View view;

        switch (viewType) {
            case VIDEOS:
                layoutId = R.layout.videos;
                view = LayoutInflater.from(context).inflate(layoutId,parent,false);
                return new MyVideosViewHolder(view);

            case REVIEWS:
                layoutId = R.layout.reviews;
                view = LayoutInflater.from(context).inflate(layoutId,parent,false);
                return new MyReviewViewHolder(view);

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return VIDEOS;
        }else if (position == 1){
            return REVIEWS;
        }else {
            return -1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case REVIEWS:
                bindReviews((MyReviewViewHolder) holder);
                break;
            case VIDEOS:
                bindVideo((MyVideosViewHolder) holder);

        }
    }

    //TODO: comment this method
    public void bindVideo(MyVideosViewHolder vidHolder){
        VideoAdapter adapter = new VideoAdapter(context, (List<VideoModel>) mdata.get(0), this);
        vidHolder.vRecyclerView.setAdapter(adapter);
        vidHolder.vRecyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        vidHolder.vRecyclerView.setHasFixedSize(true);
        vidHolder.vRecyclerView.setNestedScrollingEnabled(false);
    }

    //TODO: comment this method
    public void bindReviews(MyReviewViewHolder revHolder){
        ReviewAdapter adapter = new ReviewAdapter(context, (List<ReviewModel>) mdata.get(1));
        revHolder.rRecyclerView.setAdapter(adapter);
        revHolder.rRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        revHolder.rRecyclerView.setHasFixedSize(true);
        revHolder.rRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    @Override
    public void onListItemClick(int position, List<VideoModel> models) {
        VideoModel model = models.get(position);
        String youTubeUrl = String.valueOf(NetworkUtils.buildYoutubeUrl(model.getKey()));
        Intent appIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("vnd.youtube:" + model.getKey()));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(youTubeUrl + id));
            try {
                context.startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                context.startActivity(webIntent);
            }

    }

    class MyVideosViewHolder extends RecyclerView.ViewHolder {
        RecyclerView vRecyclerView;
        public MyVideosViewHolder(View itemView) {
            super(itemView);
            vRecyclerView = itemView.findViewById(R.id.video_recyclerView);

        }
    }

    class MyReviewViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rRecyclerView;
        public MyReviewViewHolder(View itemView) {
            super(itemView);
            rRecyclerView = itemView.findViewById(R.id.review_recyclerView);
        }
    }


}
