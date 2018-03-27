package com.tynmarket.serenade.model.util;

import com.twitter.sdk.android.core.models.User;

/**
 * Created by tynmarket on 2018/03/04.
 */

public class DummyUser {
    public static User tynmarket() {
        return new User(false, "createdAt", false,
                false, "description", "emailAddress",
                null, 0, false, 1958,
                488, false, 0, "idStr", false,
                "lang", 0, "location", "ティン＠iMarket管理人",
                "profileBackgroundColor", "profileBackgroundImageUrl",
                "profileBackgroundImageUrlHttps", false,
                "profileBannerUrl", "profileImageUrl",
                "https://pbs.twimg.com/profile_images/742013491/06c940e6-s_200x200.png",
                "profileLinkColor",
                "profileSidebarBorderColor", "profileSidebarFillColor",
                "profileTextColor", false, false,
                "tynmarket", false, null, 0,
                "timeZone", "url", 0, false, null,
                "withheldScope");

    }
}
