package com.example.eric.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eric.popularmovies.Models.Review;
import com.example.eric.popularmovies.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by eric on 17/10/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context mContext;
    private List<Review> mData;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.mContext = context;
        this.mData = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.review_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = mData.get(position);

        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.review_author) TextView author;
        @BindView(R.id.review_content) TextView content;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
