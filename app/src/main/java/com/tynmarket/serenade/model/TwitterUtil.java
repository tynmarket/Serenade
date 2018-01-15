package com.tynmarket.serenade.model;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

/**
 * Created by tyn-iMarket on 2018/01/15.
 */

public class TwitterUtil {
    public static Tweet dummyTweet(int i) {
        Tweet tweet = new Tweet(null, "10時間", null, null,
                null, 0, false, "filterLevel", i + 1,
                String.valueOf(i + 1), "inReplyToScreenName", 0,
                "inReplyToStatusIdStr", 0, "inReplyToUserIdStr",
                "lang", null, false, null, 0,
                "quotedStatusIdStr", null, 0, false,
                null, "source", dummyText(i), null,
                false, dummyUser(i), false, null,
                "withheldScope", null);

        return tweet;
    }

    public static String dummyText(int i) {
        return String.format("ツイート内容ツイート内容ツイート内容ツイート内容ツイート内容ツイート内容 %d", i + 1);
    }

    public static User dummyUser(int i) {
        User user = new User(false, "createdAt", false,
                false, "description", "emailAddress",
                null, 0, false, 0,
                0, false, 0, "idStr", false,
                "lang", 0, "location", String.format("ティン＠iMarket管理人あああ %d", i + 1),
                "profileBackgroundColor", "profileBackgroundImageUrl",
                "profileBackgroundImageUrlHttps", false,
                "profileBannerUrl", "profileImageUrl",
                "https://pbs.twimg.com/profile_images/742013491/06c940e6-s_normal.png",
                "profileLinkColor",
                "profileSidebarBorderColor", "profileSidebarFillColor",
                "profileTextColor", false, false,
                String.format("tynmarket %d", i + 1), false, null, 0,
                "timeZone", "url", 0, false, null,
                "withheldScope");
        return user;
    }
}
