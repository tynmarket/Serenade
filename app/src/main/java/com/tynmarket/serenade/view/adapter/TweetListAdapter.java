package com.tynmarket.serenade.view.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.view.holder.TweetViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyn-iMarket on 2017/12/18.
 */

public class TweetListAdapter extends RecyclerView.Adapter<TweetViewHolder> implements ListPreloader.PreloadModelProvider<Tweet> {
    public static final int ICON_SIZE = 48;

    private ArrayList<Tweet> tweets;
    private RequestOptions requestOptions;
    private RequestManager manager;

    public TweetListAdapter(ArrayList<Tweet> tweets) {
        this.tweets = tweets;
        this.requestOptions = new RequestOptions();
        requestOptions.override(ICON_SIZE);
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tweet, parent, false);
        // TODO: GlideApp
        manager = Glide.with(parent.getContext());

        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        Tweet tweet = this.tweets.get(position);
        User user = tweet.user;

        manager
                .load(user.profileImageUrlHttps)
                .apply(requestOptions)
                .into(holder.icon);
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

    @NonNull
    @Override
    public List<Tweet> getPreloadItems(int position) {
        return tweets.subList(position, position + 1);
    }

    @Nullable
    @Override
    public RequestBuilder getPreloadRequestBuilder(@NonNull Tweet item) {
        return manager
                .load(item.user.profileImageUrlHttps)
                .apply(requestOptions);
    }
}
