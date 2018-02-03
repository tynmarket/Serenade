package com.tynmarket.serenade.event;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

/**
 * Created by tynmarket on 2018/02/03.
 */

public class LoadTweetListEvent {
    public int sectionNumber;
    public List<Tweet> tweets;
    public boolean refresh;

    public LoadTweetListEvent(int sectionNumber, List<Tweet> tweets, boolean refresh) {
        this.sectionNumber = sectionNumber;
        this.tweets = tweets;
        this.refresh = refresh;
    }
}
