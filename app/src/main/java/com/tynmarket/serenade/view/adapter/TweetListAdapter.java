package com.tynmarket.serenade.view.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.model.TwitterCard;
import com.tynmarket.serenade.model.util.TweetUtil;
import com.tynmarket.serenade.model.util.UserUtil;
import com.tynmarket.serenade.view.holder.TweetViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tyn-iMarket on 2017/12/18.
 */

public class TweetListAdapter extends RecyclerView.Adapter<TweetViewHolder> {
    private final ArrayList<Tweet> tweets;
    private Map<String, TwitterCard> cards;

    private RequestManager manager;

    public TweetListAdapter(ArrayList<Tweet> tweets) {
        this.tweets = tweets;
        this.cards = new HashMap<>();
    }

    @Override
    // parent is RecyclerView
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tweet, parent, false);
        // TODO: GlideApp
        manager = Glide.with(parent.getContext());

        return new TweetViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        // TODO: Method too long
        // TODO: Control visibility by using View.GONE
        holder.setAdapter(this);

        // TODO: Make Tweet wrapper class to use utility method
        Tweet tweet = this.tweets.get(position);
        User user = tweet.user;
        Tweet quotedStatus = tweet.quotedStatus;

        holder.tweet = tweet;
        holder.tweetContent.setTweet(tweet);
        holder.setFavorited(tweet.favorited);

        String profileImageUrlHttps;
        TwitterCard card = cards.get(TweetUtil.expandedUrl(tweet));

        // TODO: split by view type?
        // TODO: Retweet quoted tweet
        // Retweet
        if (tweet.retweetedStatus != null) {
            holder.retweetUserName.setText(String.format("%sがリツイート", user.name));
            holder.retweetContainer.setVisibility(View.VISIBLE);
            profileImageUrlHttps = UserUtil.get200xProfileImageUrlHttps(tweet.retweetedStatus.user);
        } else {
            holder.retweetContainer.setVisibility(View.GONE);
            profileImageUrlHttps = UserUtil.get200xProfileImageUrlHttps(user);
        }

        manager.load(profileImageUrlHttps).into(holder.icon);

        // Quote tweet
        holder.quoteTweetContent.setTweet(quotedStatus);

        // Twitter Card Summary
        if (card != null && (card.isSummary() || card.isSummaryLargeImage())) {
            holder.cardSummaryTitle.setText(card.title);
            holder.cardSummaryHost.setText(TweetUtil.expandedUrlHost(tweet));

            if (card.isSummary()) {
                holder.cardSummaryLargeImageText.setVisibility(View.GONE);
                manager.load(card.image).into(holder.cardSummaryImage);
            } else {
                String domain = TweetUtil.expandedUrlDomain(tweet);
                if (domain != null) {
                    holder.cardSummaryLargeImageText.setText(domain.toUpperCase());
                }
                holder.cardSummaryLargeImageText.setVisibility(View.VISIBLE);
            }

            holder.cardSummary.setVisibility(View.VISIBLE);
        } else {
            holder.cardSummary.setVisibility(View.GONE);
            holder.cardSummaryImage.setImageDrawable(null);
            holder.cardSummaryLargeImageText.setText(null);
            holder.cardSummaryTitle.setText(null);
            holder.cardSummaryHost.setText(null);
        }

        // Slide button
        // TODO: SlideShare
        if (TweetUtil.containSlide(tweet)) {
            holder.slideButton.setVisibility(View.VISIBLE);
        } else {
            holder.slideButton.setVisibility(View.GONE);
        }

        // Actions
        holder.reply.setText("reply");
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

    public void refreshCards(Map<String, TwitterCard> cards) {
        this.cards = cards;
    }
}
