package com.tynmarket.serenade.view.custom;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.databinding.TweetContentBinding;
import com.tynmarket.serenade.model.util.TweetUtil;
import com.tynmarket.serenade.model.util.TwitterUtil;

/**
 * Created by tynmarket on 2018/03/17.
 */

public class TweetContentView extends RelativeLayout {
    private TweetContentBinding binding;

    public TweetContentView(Context context) {
        super(context);
    }

    public TweetContentView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater, R.layout.tweet_content, this, true);

        // TODO: Show displayUrl, move to url
        // TODO: Replace escaped character in tweet

        // Open tweet
        setOnTweetTextClickListener();
        // Open profile
        setOnNameClickListener();
        setOnScreenNameClickListener();
    }

    public TweetContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @BindingAdapter("tweetPhoto")
    public static void setTweetPhoto(ImageView view, Tweet tweet) {
        TweetUtil.loadImage(view, tweet);
    }

    public void setTweet(@Nullable Tweet tweet) {
        if (tweet != null && tweet.retweetedStatus != null) {
            binding.setTweet(tweet.retweetedStatus);
        } else {
            binding.setTweet(tweet);
        }
    }

    private void setOnTweetTextClickListener() {
        binding.tweetText.setOnClickListener(v -> {
            Tweet tweet = binding.getTweet();
            Uri uri = TwitterUtil.tweetUri(tweet.user.screenName, tweet.idStr);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            // TODO: FLAG_ACTIVITY
            // TODO: Transition
            // https://developer.android.com/reference/android/app/Activity.html#overridePendingTransition(int, int)
            getContext().startActivity(intent);
        });
    }

    private void setOnNameClickListener() {
        binding.name.setOnClickListener(v -> {
            openProfile();
        });
    }

    private void setOnScreenNameClickListener() {
        binding.screenName.setOnClickListener(v -> {
            openProfile();
        });
    }

    private void openProfile() {
        User user = TweetUtil.tweetOrRetweetedStatus(binding.getTweet()).user;
        Uri uri = TwitterUtil.profileUri(user.screenName);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        // TODO: Transition
        binding.getRoot().getContext().startActivity(intent);
    }
}
