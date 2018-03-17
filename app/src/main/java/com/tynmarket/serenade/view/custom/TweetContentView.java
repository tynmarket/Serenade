package com.tynmarket.serenade.view.custom;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twitter.sdk.android.core.models.Tweet;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.model.util.TwitterUtil;

/**
 * Created by tynmarket on 2018/03/17.
 */

public class TweetContentView extends RelativeLayout {
    public Tweet tweet;
    private TextView tweetText;

    public TweetContentView(Context context) {
        super(context);
    }

    public TweetContentView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.tweet_content, this);
        this.tweetText = findViewById(R.id.tweet_text);

        // Open tweet
        setOnTweetTextClickListener();
    }

    public TweetContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setOnTweetTextClickListener() {
        tweetText.setOnClickListener(v -> {
            Uri uri = TwitterUtil.tweetUri(tweet.user.screenName, tweet.idStr);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            // TODO: FLAG_ACTIVITY
            // TODO: Transition
            // https://developer.android.com/reference/android/app/Activity.html#overridePendingTransition(int, int)
            getContext().startActivity(intent);
        });
    }
}
