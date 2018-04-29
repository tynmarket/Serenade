package com.tynmarket.serenade.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.core.models.Tweet;
import com.tynmarket.serenade.BuildConfig;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.model.LoginUser;
import com.tynmarket.serenade.model.entity.TwitterCard;
import com.tynmarket.serenade.model.util.TweetUtil;
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
    private boolean cardsLoaded = false;

    public TweetListAdapter(ArrayList<Tweet> tweets) {
        this.tweets = tweets;
        this.cards = new HashMap<>();
    }

    @Override
    // parent is RecyclerView
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tweet, parent, false);

        return new TweetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        holder.setAdapter(this);

        if (BuildConfig.DEBUG) {
            Log.d("Serenade", String.format("onBindViewHolder: position=%d", position));
        }

        Tweet tweet = this.tweets.get(position);
        TwitterCard card = cards.get(TweetUtil.expandedUrl(tweet));

        holder.setTweetAndCardToBindings(tweet, card);

        if (LoginUser.isTynmarket() && TweetUtil.containSlide(tweet)) {
            holder.binding.slideButton.setVisibility(View.VISIBLE);
        } else {
            holder.binding.slideButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return this.tweets.size();
    }

    public Tweet getTweet(int position) {
        return tweets.get(position);
    }

    public boolean requestCardCache(int position) {
        if (tweets.size() == 0) {
            return false;
        }

        Tweet tweet = tweets.get(position);
        String url = TweetUtil.expandedUrlWithoutTwitter(tweet);
        TwitterCard card = cards.get(url);

        return (url != null && card == null) || (card != null && card.retry);
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
        if (cards.size() == 0) {
            this.cards = cards;
        } else {
            this.cards.putAll(cards);
        }
        cardsLoaded = true;
    }

    public void replaceCard(TwitterCard card) {
        cards.put(card.url, card);
    }

    public boolean cardsLoaded() {
        return cardsLoaded;
    }

    public void clearAll() {
        tweets.clear();
        cards.clear();
        notifyDataSetChanged();
    }
}
