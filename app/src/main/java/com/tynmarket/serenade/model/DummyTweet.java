package com.tynmarket.serenade.model;

import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.User;

import java.util.Collections;
import java.util.List;

/**
 * Created by tyn-iMarket on 2018/01/15.
 */

public class DummyTweet {
    public static Tweet tweet(int i) {
        return tweet(i, null, dummyUser(i));
    }

    private static Tweet tweet(int i, Tweet retweetedStatus, User user) {
        return new Tweet(null, "10時間", null, dummyEntities(),
                null, 0, false, "filterLevel", i + 1,
                String.valueOf(i + 1), "inReplyToScreenName", 0,
                "inReplyToStatusIdStr", 0, "inReplyToUserIdStr",
                "lang", null, false, null, 0,
                "quotedStatusIdStr", null, 0, false,
                retweetedStatus, "source", dummyText(i), null,
                false, user, false, null,
                "withheldScope", null);
    }

    public static Tweet tweetWithRetweetedStatus(int i) {
        Tweet retweetedStatus = tweet(i, null, dummyUser(i, "リツイートされた人"));
        return tweet(i, retweetedStatus, dummyUser(i));
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
        return new TweetEntities(null, null, dummyMediaEntity(), null, null);
    }

    private static List<MediaEntity> dummyMediaEntity() {
        MediaEntity entity = new MediaEntity("url", "expandedUrl", "displayUrl",
                0, 1, 10, "idStr", "mediaUrl",
                "https://pbs.twimg.com/media/DT00eThV4AAgQSl.jpg",
                null, 0, "sourceStatusIdStr",
                "photo", null, null);
        return Collections.singletonList(entity);
    }
}
