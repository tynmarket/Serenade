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
import com.tynmarket.serenade.view.holder.TweetViewHolder;
import com.tynmarket.serenade.view.util.ViewContentLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tynmarket.serenade.model.util.TweetUtil.photoUrl;

/**
 * Created by tyn-iMarket on 2017/12/18.
 */

public class TweetListAdapter extends RecyclerView.Adapter<TweetViewHolder> {
    private final ArrayList<Tweet> tweets;
    private Map<String, TwitterCard> cards;

    private RequestManager manager;
    private ViewContentLoader textLoader;
    private ViewContentLoader imageLoader;
    // TODO: Utility class
    private Integer tweetPhotoHeight;
    private Integer tweetPhotoTopMargin;
    private Integer spacingLarge;
    private Integer spacingMedium;
    private Integer spacingSmall;

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
        textLoader = new ViewContentLoader();
        imageLoader = new ViewContentLoader(manager);
        if (tweetPhotoHeight == null) {
            tweetPhotoHeight = parent.getContext().getResources().getDimensionPixelSize(R.dimen.image_height_tweet_photo);
        }
        if (tweetPhotoTopMargin == null) {
            tweetPhotoTopMargin = parent.getContext().getResources().getDimensionPixelSize(R.dimen.spacing_medium);
        }
        if (spacingLarge == null) {
            spacingLarge = parent.getContext().getResources().getDimensionPixelSize(R.dimen.spacing_large);
        }
        if (spacingMedium == null) {
            spacingMedium = parent.getContext().getResources().getDimensionPixelSize(R.dimen.spacing_medium);
        }
        if (spacingSmall == null) {
            spacingSmall = parent.getContext().getResources().getDimensionPixelSize(R.dimen.spacing_small);
        }

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
        Tweet retweetedStatus = tweet.retweetedStatus;
        Tweet quotedStatus = tweet.quotedStatus;

        holder.tweet = tweet;
        holder.setFavorited(tweet.favorited);

        String profileImageUrlHttps;
        String name;
        String screenName;
        // TODO: Show displayUrl, move to url
        String tweetText;
        String photoUrl = photoUrl(tweet);
        TwitterCard card = cards.get(TweetUtil.expandedUrl(tweet));

        // TODO: split by view type?
        // TODO: Retweet quoted tweet
        // Retweet
        if (retweetedStatus != null) {
            textLoader.setText(holder.retweetUserName, String.format("%sがリツイート", user.name),
                    null, spacingSmall, null, spacingSmall);
            profileImageUrlHttps = get200xProfileImageUrlHttps(retweetedStatus.user);
            name = retweetedStatus.user.name;
            screenName = retweetedStatus.user.screenName;
            tweetText = retweetedStatus.text;
        } else {
            textLoader.unsetText(holder.retweetUserName,
                    null, spacingLarge, null, 0);
            profileImageUrlHttps = get200xProfileImageUrlHttps(user);
            name = user.name;
            screenName = user.screenName;
            tweetText = tweet.text;
        }

        manager.load(profileImageUrlHttps).into(holder.icon);
        setNameAndText(holder, name, screenName, tweetText);
        holder.createdAt.setText(tweet.createdAt);

        // Image
        if (photoUrl != null) {
            imageLoader.loadImage(holder.tweetPhoto, photoUrl, tweetPhotoHeight,
                    null, tweetPhotoTopMargin, null, null);
        } else {
            imageLoader.unloadImage(holder.tweetPhoto,
                    null, 0, null, null);
        }

        // TODO: split by view type?
        // Quoted retweet
        if (quotedStatus != null) {
            textLoader.setText(holder.quotedName, quotedStatus.user.name,
                    null, spacingMedium, null, null);
            textLoader.setText(holder.quotedScreenName, quotedStatus.user.screenName,
                    spacingSmall, spacingMedium, null, null);
            textLoader.setText(holder.quotedTweetText, quotedStatus.text,
                    null, spacingMedium, null, null);

            String quotedPhotoUrl = TweetUtil.photoUrl(quotedStatus);
            if (quotedPhotoUrl != null) {
                imageLoader.loadImage(holder.quotedTweetPhoto, photoUrl(quotedStatus), tweetPhotoHeight,
                        null, tweetPhotoTopMargin, null, null);
            }
        } else {
            textLoader.unsetText(holder.quotedName,
                    null, 0, null, null);
            textLoader.unsetText(holder.quotedScreenName,
                    0, 0, null, null);
            textLoader.unsetText(holder.quotedTweetText,
                    null, 0, null, null);
            imageLoader.unloadImage(holder.quotedTweetPhoto,
                    null, 0, null, null);
        }

        // Twitter Card Summary
        if (card != null && (card.isSummary() || card.isSummaryLargeImage())) {
            holder.cardSummaryTitle.setText(card.title);
            holder.cardSummaryHost.setText(TweetUtil.expandedUrlHost(tweet));

            if (card.isSummary()) {
                holder.cardSummaryLargeImageText.setVisibility(View.GONE);
                manager.load(card.image).into(holder.cardSummaryImage);
            } else {
                String domain = TweetUtil.expandedUrlDomain(tweet);
                holder.cardSummaryLargeImageText.setText(domain.toUpperCase());
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

    private String get200xProfileImageUrlHttps(User user) {
        return user.profileImageUrlHttps.replace("_normal", "_200x200");
    }

    private void setNameAndText(TweetViewHolder holder, String name, String screenName, String tweetText) {
        holder.name.setText(name);
        holder.screenName.setText(String.format("@%s", screenName));
        // TODO: Move to profile on clicking screenName
        // TODO: Replace escaped character
        holder.tweetText.setText(tweetText);
    }
}
