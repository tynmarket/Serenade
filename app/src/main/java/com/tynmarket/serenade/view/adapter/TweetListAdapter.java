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
import com.twitter.sdk.android.core.models.UrlEntity;
import com.twitter.sdk.android.core.models.User;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.view.holder.TweetViewHolder;
import com.tynmarket.serenade.view.util.ViewContentLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyn-iMarket on 2017/12/18.
 */

public class TweetListAdapter extends RecyclerView.Adapter<TweetViewHolder> {
    private ArrayList<Tweet> tweets;
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
    }

    @Override
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

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        holder.setAdapter(this);

        Tweet tweet = this.tweets.get(position);
        User user = tweet.user;
        String profileImageUrlHttps;
        String name;
        String screenName;
        String tweetText;
        String photoUrl = getPhotoUrl(tweet);
        holder.tweet = tweet;
        holder.setFavorited(tweet.favorited);

        // TODO: split by view type?
        // TODO: Retweet quoted tweet
        // Retweet
        if (tweet.retweetedStatus != null) {
            textLoader.setText(holder.retweetUserName, String.format("%sがリツイート", user.name),
                    null, spacingSmall, null, spacingSmall);
            profileImageUrlHttps = get200xProfileImageUrlHttps(tweet.retweetedStatus.user);
            name = tweet.retweetedStatus.user.name;
            screenName = tweet.retweetedStatus.user.screenName;
            tweetText = replaceUrlWithDisplayUrl(tweet.retweetedStatus);
        } else {
            textLoader.unsetText(holder.retweetUserName,
                    null, spacingLarge, null, 0);
            profileImageUrlHttps = get200xProfileImageUrlHttps(user);
            name = user.name;
            screenName = user.screenName;
            tweetText = replaceUrlWithDisplayUrl(tweet);
        }

        // TODO: split by view type?
        //Quoted retweet
        if (tweet.quotedStatus != null) {
            textLoader.setText(holder.quotedName, tweet.quotedStatus.user.name,
                    null, spacingMedium, null, null);
            textLoader.setText(holder.quotedScreenName, tweet.quotedStatus.user.screenName,
                    spacingSmall, spacingMedium, null, null);
            textLoader.setText(holder.quotedTweetText, tweet.quotedStatus.text,
                    null, spacingMedium, null, null);
            // TODO: Not appear
            imageLoader.loadImage(holder.quotedTweetPhoto, getPhotoUrl(tweet.quotedStatus), tweetPhotoHeight,
                    null, tweetPhotoTopMargin, null, null);
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

    private String get200xProfileImageUrlHttps(User user) {
        return user.profileImageUrlHttps.replace("_normal", "_200x200");
    }

    // TODO: Invalid url of tweet and others?
    private String replaceUrlWithDisplayUrl(Tweet tweet) {
        String text = tweet.text;

        List<UrlEntity> entities = tweet.entities.urls;
        if (entities.size() == 0) {
            return text;
        }

        for (UrlEntity entity : entities) {
            text = text.replace(entity.url, entity.displayUrl);
        }

        return text;
    }

    private void setNameAndText(TweetViewHolder holder, String name, String screenName, String tweetText) {
        holder.name.setText(name);
        holder.screenName.setText(String.format("@%s", screenName));
        holder.tweetText.setText(tweetText);
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
