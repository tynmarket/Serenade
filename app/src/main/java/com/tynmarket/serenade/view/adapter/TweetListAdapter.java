package com.tynmarket.serenade.view.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
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

public class TweetListAdapter extends RecyclerView.Adapter<TweetViewHolder> {
    private ArrayList<Tweet> tweets;
    private RequestManager manager;

    public TweetListAdapter(ArrayList<Tweet> tweets) {
        this.tweets = tweets;
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
        holder.setAdapter(this);
        holder.tweet = tweet;
        holder.setFavorited(tweet.favorited);

        manager.load(getOriginalProfileImageUrlHttps(user)).into(holder.icon);
        holder.name.setText(user.name);
        holder.screenName.setText(String.format("@%s", user.screenName));
        holder.createdAt.setText(tweet.createdAt);
        holder.tweetText.setText(tweet.text);
        if (photoUrl != null) {
            manager.load(photoUrl).into(holder.tweetPhoto);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) holder.tweetPhoto.getLayoutParams();
            int topMargin = holder.itemView.getContext().getResources().getDimensionPixelSize(R.dimen.spacing_medium);
            lp.setMargins(lp.leftMargin, topMargin, lp.rightMargin, lp.bottomMargin);
            lp.height = holder.itemView.getContext().getResources().getDimensionPixelSize(R.dimen.image_height_tweet_photo);
            holder.tweetPhoto.setLayoutParams(lp);
        } else {
            holder.tweetPhoto.setImageDrawable(null);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) holder.tweetPhoto.getLayoutParams();
            lp.setMargins(lp.leftMargin, 0, lp.rightMargin, lp.bottomMargin);
            lp.height = 0;
            holder.tweetPhoto.setLayoutParams(lp);
        }
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

    private String getOriginalProfileImageUrlHttps(User user) {
        return user.profileImageUrlHttps.replace("_normal", "");
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
