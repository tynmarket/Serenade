package com.tynmarket.serenade.model.util;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.twitter.sdk.android.core.models.Tweet;
import com.tynmarket.serenade.model.LoginUser;
import com.tynmarket.serenade.model.entity.TwitterCard;

/**
 * Created by tynmarket on 2018/05/02.
 */
public class FirebaseAnalyticsHelper {
    private static final String CATEGORY_TWEET = "tweet";
    private static final String CATEGORY_SUMMARY_CARD = "summary_card";
    private static final String CATEGORY_SUMMARY_CARD_LARGE = "summary_card_large";
    private static final String CATEGORY_REFRESH = "refresh";
    private static final String CATEGORY_READ_MORE = "read_more";
    private static final String CATEGORY_TWEET_ACTION = "tweet_action";
    private static final String CATEGORY_NAVIGATION = "navigation";

    private static final String NAME_VIEW_TWEET = "view_tweet";
    private static final String NAME_HOME_TIMELINE = "home_timeline";
    private static final String NAME_FAVORITE_LIST = "favorite_list";
    private static final String NAME_REPLY = "reply";
    private static final String NAME_RETWEET = "retweet";
    private static final String NAME_FAVORITE = "favorite";
    private static final String NAME_FOLLOW = "follow";
    private static final String NAME_FOLLOWER = "follower";
    private static final String NAME_PROFILE = "profile";
    private static final String NAME_LIST = "list";
    private static final String NAME_POST_TWEET = "post_tweet";
    private static final String NAME_SEARCH = "search";
    private static final String NAME_NOTIFICATION = "notification";
    private static final String NAME_MESSAGE = "message";
    private static final String NAME_SUPPORT = "support";
    private static final String NAME_SIGN_OUT = "sign_out";
    private static final String NAME_TERMS_AND_SERVICE = "terms_and_service";

    private static final String ID_VIEW_TWEET = "0";
    private static final String ID_REFRESH_HOME_TIMELINE = "1";
    private static final String ID_REFRESH_FAVORITE_LIST = "2";
    private static final String ID_READ_MORE_HOME_TIMELINE = "3";
    private static final String ID_READ_MORE_FAVORITE_LIST = "4";
    private static final String ID_NAVIGATION_FOLLOW = "5";
    private static final String ID_NAVIGATION_FOLLOWER = "6";
    private static final String ID_NAVIGATION_PROFILE = "7";
    private static final String ID_NAVIGATION_LIST = "8";
    private static final String ID_NAVIGATION_POST_TWEET = "9";
    private static final String ID_NAVIGATION_SEARCH = "10";
    private static final String ID_NAVIGATION_NOTIFICATION = "11";
    private static final String ID_NAVIGATION_MESSAGE = "12";
    private static final String ID_NAVIGATION_SUPPORT = "13";
    private static final String ID_NAVIGATION_SIGN_OUT = "14";
    private static final String ID_NAVIGATION_TERMS_AND_SERVICE = "15";

    private FirebaseAnalytics analytics;

    public FirebaseAnalyticsHelper(Context context) {
        // Do not track tynmarket
        if (LoginUser.isTynmarket()) {
            return;
        }

        this.analytics = FirebaseAnalytics.getInstance(context);
    }

    public void logViewTweet() {
        logViewItem(ID_VIEW_TWEET, NAME_VIEW_TWEET, CATEGORY_TWEET);
    }

    public void logViewTwitterCard(TwitterCard twitterCard) {
        if (twitterCard.isSummary()) {
            logViewSummaryCard(twitterCard);
        } else {
            logViewSummaryCardLarge(twitterCard);
        }
    }

    private void logViewSummaryCard(TwitterCard twitterCard) {
        logViewItem(twitterCard.tweet.idStr, twitterCard.host, CATEGORY_SUMMARY_CARD);
    }

    private void logViewSummaryCardLarge(TwitterCard twitterCard) {
        logViewItem(twitterCard.tweet.idStr, twitterCard.host, CATEGORY_SUMMARY_CARD_LARGE);
    }

    public void logRefreshTweetList(int sectionNumber) {
        if (sectionNumber == 1) {
            logRefreshHomeTimeline();
        } else {
            logRefreshFavoriteList();
        }
    }

    private void logRefreshHomeTimeline() {
        logViewItem(ID_REFRESH_HOME_TIMELINE, NAME_HOME_TIMELINE, CATEGORY_REFRESH);
    }

    private void logRefreshFavoriteList() {
        logViewItem(ID_REFRESH_FAVORITE_LIST, NAME_FAVORITE_LIST, CATEGORY_REFRESH);
    }

    public void logReadMoreTweetList(int sectionNumber) {
        if (sectionNumber == 1) {
            logReadMoreHomeTimeline();
        } else {
            logReadMoreFavoriteList();
        }
    }

    private void logReadMoreHomeTimeline() {
        logViewItem(ID_READ_MORE_HOME_TIMELINE, NAME_HOME_TIMELINE, CATEGORY_READ_MORE);
    }

    private void logReadMoreFavoriteList() {
        logViewItem(ID_READ_MORE_FAVORITE_LIST, NAME_FAVORITE_LIST, CATEGORY_READ_MORE);
    }

    public void logReply(Tweet tweet) {
        logViewItem(tweet.idStr, NAME_REPLY, CATEGORY_TWEET_ACTION);
    }

    public void logRetweet(Tweet tweet) {
        logViewItem(tweet.idStr, NAME_RETWEET, CATEGORY_TWEET_ACTION);
    }

    public void logFavorite(Tweet tweet) {
        logViewItem(tweet.idStr, NAME_FAVORITE, CATEGORY_TWEET_ACTION);
    }

    public void logViewFollow() {
        logViewItem(ID_NAVIGATION_FOLLOW, NAME_FOLLOW, CATEGORY_NAVIGATION);
    }

    public void logViewFollower() {
        logViewItem(ID_NAVIGATION_FOLLOWER, NAME_FOLLOWER, CATEGORY_NAVIGATION);
    }

    public void logViewProfile() {
        logViewItem(ID_NAVIGATION_PROFILE, NAME_PROFILE, CATEGORY_NAVIGATION);
    }

    public void logViewList() {
        logViewItem(ID_NAVIGATION_LIST, NAME_LIST, CATEGORY_NAVIGATION);
    }

    public void logViewPostTweet() {
        logViewItem(ID_NAVIGATION_POST_TWEET, NAME_POST_TWEET, CATEGORY_NAVIGATION);
    }

    public void logViewSearch() {
        logViewItem(ID_NAVIGATION_SEARCH, NAME_SEARCH, CATEGORY_NAVIGATION);
    }

    public void logViewNotification() {
        logViewItem(ID_NAVIGATION_NOTIFICATION, NAME_NOTIFICATION, CATEGORY_NAVIGATION);
    }

    public void logViewMessage() {
        logViewItem(ID_NAVIGATION_MESSAGE, NAME_MESSAGE, CATEGORY_NAVIGATION);
    }

    public void logViewSupport() {
        logViewItem(ID_NAVIGATION_SUPPORT, NAME_SUPPORT, CATEGORY_NAVIGATION);
    }

    public void logViewSignOut() {
        logViewItem(ID_NAVIGATION_SIGN_OUT, NAME_SIGN_OUT, CATEGORY_NAVIGATION);
    }

    public void logViewTermsAndService() {
        logViewItem(ID_NAVIGATION_TERMS_AND_SERVICE, NAME_TERMS_AND_SERVICE, CATEGORY_NAVIGATION);
    }

    private void logViewItem(String idStr, String name, String category) {
        // Do not track tynmarket
        if (LoginUser.isTynmarket()) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, idStr);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category);
        analytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }
}
