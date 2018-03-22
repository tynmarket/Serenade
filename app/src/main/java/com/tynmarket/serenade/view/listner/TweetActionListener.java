package com.tynmarket.serenade.view.listner;

import com.twitter.sdk.android.core.models.Tweet;

/**
 * Created by tynmarket on 2018/03/22.
 */

public interface TweetActionListener {
    void result(Tweet newTweet);
}
