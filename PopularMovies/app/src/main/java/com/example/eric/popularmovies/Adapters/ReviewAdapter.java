package com.example.eric.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eric.popularmovies.Models.ReviewModel;
import com.example.eric.popularmovies.R;

import java.util.List;

/**
 * Created by eric on 17/10/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder >{
    private Context context;
    private List<ReviewModel> mData;

    public ReviewAdapter(Context context, List<ReviewModel> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_list_item,parent,false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        ReviewModel model = mData.get(position);
        holder.author.setText(model.getAuthor());
        holder.content.setText(model.getContent());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        private TextView author;
        private TextView content;
        public ReviewViewHolder(View itemView) {
            super(itemView);

            author = itemView.findViewById(R.id.review_author);
            content = itemView.findViewById(R.id.review_content);
        }
    }

}
