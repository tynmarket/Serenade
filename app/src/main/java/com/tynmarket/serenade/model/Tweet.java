package com.tynmarket.serenade.model;

/**
 * Created by tyn-iMarket on 2017/12/18.
 */

public class Tweet {
    private String name;
    private String screenName;
    private String tweetText;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getTweetText() {
        return tweetText;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }
}
