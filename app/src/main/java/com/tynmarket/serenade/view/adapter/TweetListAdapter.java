package com.tynmarket.serenade.view.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetEntities;
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
        //requestOptions.override(ICON_SIZE);
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
        String photoUrl = getPhotoUrl(tweet);
        Log.d("Serenade", String.format("photoUrl: %s", photoUrl));
        holder.setAdapter(this);
        holder.tweet = tweet;
        holder.setFavorited(tweet.favorited);

        manager
                .load(getLargeProfileImageUrlHttps(user))
                //.apply(requestOptions)
                .into(holder.icon);
        holder.name.setText(user.name);
        holder.screenName.setText(String.format("@%s", user.screenName));
        holder.createdAt.setText(tweet.createdAt);
        holder.tweetText.setText(tweet.text);
        holder.talk.setText("talk");
        holder.retweet.setText("retweet");
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

    public void addTweets(List<Tweet> prevTweets) {
        int itemCount = tweets.size();
        tweets.addAll(prevTweets);
        notifyItemRangeInserted(itemCount, prevTweets.size());
    }

    public void replaceTweet(int position, Tweet tweet) {
        tweets.set(position, tweet);
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
                .load(item.user.profileImageUrlHttps);
        //.apply(requestOptions);
    }

    private String getLargeProfileImageUrlHttps(User user) {
        return user.profileImageUrlHttps.replace("_normal", "_bigger");
    }

    @Nullable
    private String getPhotoUrl(Tweet tweet) {
        TweetEntities entities = tweet.entities;
        if (entities == null) {
            return null;
        }

        List<MediaEntity> mediaList = entities.media;
        if (mediaList.size() == 0) {
            return null;
        }

        // TODO: stream function (API 24)
        MediaEntity entity = null;
        for (MediaEntity m : mediaList) {
            if (m.type.equals("photo")) {
                entity = m;
                break;
            }
        }

        if (entity != null) {
            return entity.mediaUrlHttps;
        } else {
            return null;
        }
    }
}
