package com.tynmarket.serenade.model.util;

import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tyn-iMarket on 2018/01/15.
 */

public class DummyTweet {
    public static ArrayList<Tweet> dummyTweets() {
        ArrayList<Tweet> tweets = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Tweet tweet;

            if (i == 1) {
                tweet = tweetWithRetweetedStatus(i);
            } else if (i == 2) {
                tweet = tweetWithQuotedStatus(i);
            } else {
                tweet = tweet(i);
            }
            tweets.add(tweet);
        }
        return tweets;
    }

    private static Tweet tweet(int i) {
        return tweet(i, dummyEntities(), null, null, dummyUser(i));
    }

    private static Tweet tweet(int i, TweetEntities entities, Tweet retweetedStatus, Tweet quotedStatus, User user) {
        return new Tweet(null, "10時間", null, entities,
                null, 0, false, "filterLevel", i + 1,
                String.valueOf(i + 1), "inReplyToScreenName", 0,
                "inReplyToStatusIdStr", 0, "inReplyToUserIdStr",
                "lang", null, false, null, 0,
                "quotedStatusIdStr", quotedStatus, 0, false,
                retweetedStatus, "source", dummyText(i), null,
                false, user, false, null,
                "withheldScope", null);
    }

    private static Tweet tweetWithRetweetedStatus(int i) {
        Tweet retweetedStatus = tweet(i, null, null, null, dummyUser(i, "リツイートされた人"));
        return tweet(i, dummyEntities(), retweetedStatus, null, dummyUser(i));
    }

    private static Tweet tweetWithQuotedStatus(int i) {
        Tweet quotedStatus = tweet(i, dummyEntities(), null, null, dummyUser(i, "引用リツイートされた人"));
        return tweet(i, dummyEntities(null), null, quotedStatus, dummyUser(i));
    }

    private static String dummyText(int i) {
        return String.format("ツイート内容ツイート内容ツイート内容ツイート内容ツイート内容ツイート内容 %d", i + 1);
    }

    private static User dummyUser(int i) {
        return dummyUser(i, String.format("ティン＠iMarket管理人あああ %d", i + 1));
    }

    private static User dummyUser(int i, String name) {
        return new User(false, "createdAt", false,
                false, "description", "emailAddress",
                null, 0, false, 0,
                0, false, 0, "idStr", false,
                "lang", 0, "location", name,
                "profileBackgroundColor", "profileBackgroundImageUrl",
                "profileBackgroundImageUrlHttps", false,
                "profileBannerUrl", "profileImageUrl",
                "https://pbs.twimg.com/profile_images/742013491/06c940e6-s_200x200.png",
                "profileLinkColor",
                "profileSidebarBorderColor", "profileSidebarFillColor",
                "profileTextColor", false, false,
                String.format("tynmarket %d", i + 1), false, null, 0,
                "timeZone", "url", 0, false, null,
                "withheldScope");

    }

    private static TweetEntities dummyEntities() {
        return new TweetEntities(null, null,
                dummyMediaEntity("https://pbs.twimg.com/media/DT00eThV4AAgQSl.jpg"),
                null, null);
    }

    private static TweetEntities dummyEntities(String mediaUrlHttps) {
        return new TweetEntities(null, null, dummyMediaEntity(mediaUrlHttps), null, null);
    }

    private static List<MediaEntity> dummyMediaEntity(String mediaUrlHttps) {
        MediaEntity entity = new MediaEntity("url", "expandedUrl", "displayUrl",
                0, 1, 10, "idStr", "mediaUrl",
                mediaUrlHttps,
                null, 0, "sourceStatusIdStr",
                "photo", null, null);
        return Collections.singletonList(entity);
    }
}
