package com.tynmarket.serenade.view.custom;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.databinding.TweetContentBinding;
import com.tynmarket.serenade.model.entity.TweetWithTwitterCard;
import com.tynmarket.serenade.model.util.ActivityHelper;
import com.tynmarket.serenade.model.util.TweetUtil;
import com.tynmarket.serenade.model.util.TwitterUtil;
import com.tynmarket.serenade.view.text.TweetTextClickableSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tynmarket on 2018/03/17.
 */

public class TweetContentView extends RelativeLayout {
    private static final Pattern patternScreenName = Pattern.compile("@([\\p{Alnum}|_]+)");
    private static final Pattern patternHashTag = Pattern.compile("#([\\w|_]+)");
    private static boolean spanClicked = false;

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
        // TODO: Make ripple effect on root item view when touch the tweet text (autoLink, LinkMovementMethod, OnClickListener)

        // Open tweet
        setOnTweetTextClickListener();

        // Open profile
        setOnNameClickListener();
        setOnScreenNameClickListener();
    }

    public TweetContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @BindingAdapter("tweetText")
    public static void setTweetText(TextView view, Tweet tweet) {
        if (tweet == null) {
            return;
        }

        String text;
        if (tweet instanceof TweetWithTwitterCard) {
            text = TweetUtil.tweetText((TweetWithTwitterCard) tweet);
        } else {
            text = TweetUtil.tweetText(tweet);
        }

        Spannable spannable = new SpannableString(text);

        Matcher matcher = patternScreenName.matcher(text);
        while (matcher.find()) {
            String screenName = matcher.group(1);
            // TODO: Disable ripple effect on other tweet text
            spannable.setSpan(new TweetTextClickableSpan() {
                @Override
                public Uri onClickSpannable() {
                    spanClicked = true;
                    return TwitterUtil.profileUri(screenName);
                }
            }, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        matcher = patternHashTag.matcher(text);
        while (matcher.find()) {
            String hashTag = matcher.group(1);
            // TODO: Disable ripple effect on other tweet text
            spannable.setSpan(new TweetTextClickableSpan() {
                @Override
                public Uri onClickSpannable() {
                    spanClicked = true;
                    return TwitterUtil.hashTagUri(hashTag);
                }
            }, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        view.setText(spannable, TextView.BufferType.SPANNABLE);
        view.setMovementMethod(LinkMovementMethod.getInstance());
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
            if (spanClicked) {
                spanClicked = false;
            } else {
                openTweet();
            }
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

    private void openTweet() {
        Tweet tweet = binding.getTweet();
        Uri uri = TwitterUtil.tweetUri(tweet.user.screenName, tweet.idStr);
        ActivityHelper.startUriActivity(getContext(), uri);
    }

    private void openProfile() {
        User user = TweetUtil.tweetOrRetweetedStatus(binding.getTweet()).user;
        Uri uri = TwitterUtil.profileUri(user.screenName);
        ActivityHelper.startUriActivity(binding.getRoot().getContext(), uri);
    }
}
