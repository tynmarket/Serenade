package com.tynmarket.serenade.view.custom;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.databinding.TweetContentBinding;
import com.tynmarket.serenade.model.util.TweetUtil;
import com.tynmarket.serenade.model.util.TwitterUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tynmarket on 2018/03/17.
 */

public class TweetContentView extends RelativeLayout {
    private static final Pattern pattern = Pattern.compile("@([\\p{Alnum}|_]+)");
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

        Matcher matcher = pattern.matcher(tweet.text);
        Spannable spannable = new SpannableString(tweet.text);
        if (matcher.find()) {
            String screenName = matcher.group(1);
            // TODO: Ripple effect
            spannable.setSpan(new ClickableSpan() {
                                  @Override
                                  public void onClick(View widget) {
                                      spanClicked = true;

                                      Uri uri = TwitterUtil.profileUri(screenName);
                                      Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                      // TODO: Transition
                                      widget.getContext().startActivity(intent);
                                  }

                                  @Override
                                  public void updateDrawState(TextPaint ds) {
                                      super.updateDrawState(ds);
                                      ds.setUnderlineText(false);
                                  }
                              }, matcher.start(), matcher.end(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
                Tweet tweet = binding.getTweet();
                Uri uri = TwitterUtil.tweetUri(tweet.user.screenName, tweet.idStr);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                // TODO: FLAG_ACTIVITY
                // TODO: Transition
                // https://developer.android.com/reference/android/app/Activity.html#overridePendingTransition(int, int)
                getContext().startActivity(intent);
            }
        });
        /*
        binding.tweetText.setMovementMethod(new LinkMovementMethod() {
            @Override
            public boolean onTouchEvent(TextView widget, Spannable buffer,
                                        MotionEvent event) {
                return true;
            }
        });
        */
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
