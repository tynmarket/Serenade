package com.tynmarket.serenade.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.view.holder.TweetViewHolder;

import java.util.ArrayList;
import java.util.List;

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
        Tweet tweet = this.tweets.get(position);
        User user = tweet.user;
        holder.name.setText(user.name);
        holder.screenName.setText(user.screenName);
        holder.createdAt.setText(tweet.createdAt);
        holder.tweetText.setText(tweet.text);
        holder.talk.setText("talk");
        holder.retweet.setText("retweet");
        holder.fav.setText("fav");
    }

    @Override
    public int getItemCount() {
        return this.tweets.size();
    }

    public void refresh(List<Tweet> newTweets) {
        tweets.clear();
        tweets.addAll(newTweets);
        notifyDataSetChanged();
    }
}
