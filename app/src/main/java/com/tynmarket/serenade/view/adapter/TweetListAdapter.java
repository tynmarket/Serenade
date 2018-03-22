package com.tynmarket.serenade.view.adapter;

import android.annotation.SuppressLint;
import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.model.TwitterCard;
import com.tynmarket.serenade.model.util.TweetUtil;
import com.tynmarket.serenade.model.util.UserUtil;
import com.tynmarket.serenade.view.custom.SummaryCardView;
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
        holder.setAdapter(this);

        // TODO: Make Tweet wrapper class to use utility method
        Tweet tweet = this.tweets.get(position);
        User user = tweet.user;
        Tweet quotedStatus = tweet.quotedStatus;
        String expandedUrl = TweetUtil.expandedUrl(tweet);

        TwitterCard card = cards.get(expandedUrl);
        if (card != null) {
            card.url = expandedUrl;
            card.domain = TweetUtil.expandedUrlDomain(tweet);
            card.host = TweetUtil.expandedUrlHost(tweet);
        }

        holder.tweet = tweet;
        holder.binding.setTweet(tweet);
        holder.binding.setCard(card);
        // Tweet
        holder.binding.tweetContent.setTweet(tweet);
        // Quote tweet
        holder.binding.quoteTweetContent.setTweet(quotedStatus);
        // Twitter Card
        holder.binding.summaryCard.binding.setCard(card);
        // Tweet action
        holder.binding.tweetAction.binding.setTweet(tweet);

        String profileImageUrlHttps;

        // Retweet
        if (tweet.retweetedStatus != null) {
            profileImageUrlHttps = UserUtil.get200xProfileImageUrlHttps(tweet.retweetedStatus.user);
        } else {
            profileImageUrlHttps = UserUtil.get200xProfileImageUrlHttps(user);
        }

        manager.load(profileImageUrlHttps).into(holder.icon);

        // Slide button
        // TODO: SlideShare
        if (TweetUtil.containSlide(tweet)) {
            holder.slideButton.setVisibility(View.VISIBLE);
        } else {
            holder.slideButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return this.tweets.size();
    }

    @BindingAdapter("retweetUserName")
    public static void setRetweetUserName(TextView view, Tweet tweet) {
        view.setText(TweetUtil.retweetUserName(tweet));
    }

    @BindingAdapter("summaryCardVisibility")
    public static void setSummaryCardVisibility(SummaryCardView view, TwitterCard card) {
        if (card != null && (card.isSummary() || card.isSummaryLargeImage())) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
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
