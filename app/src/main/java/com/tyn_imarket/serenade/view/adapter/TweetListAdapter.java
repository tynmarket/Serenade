package com.tyn_imarket.serenade.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tyn_imarket.serenade.R;
import com.tyn_imarket.serenade.model.Tweet;
import com.tyn_imarket.serenade.view.holder.TweetViewHolder;

import java.util.ArrayList;

/**
 * Created by tyn-iMarket on 2017/12/18.
 */

public class TweetListAdapter extends RecyclerView.Adapter<TweetViewHolder> {
    private ArrayList<Tweet> tweets;

    public TweetListAdapter(ArrayList<Tweet> tweets) {
        this.tweets = tweets;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tweet, parent, false);
        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        holder.text.setText(this.tweets.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return this.tweets.size();
    }
}
