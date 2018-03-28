package com.tynmarket.serenade.model.util;

import android.net.Uri;

/**
 * Created by tynmarket on 2018/02/03.
 */

public class TwitterUtil {
    private static final String TWITTER_URL = "https://twitter.com/";

    public static Uri profileUri(String screenName) {
        return Uri.parse(profileUrl(screenName));
    }

    public static Uri tweetUri(String screenName, String idStr) {
        return Uri.parse(profileUrl(screenName) + "/status/" + idStr);
    }

    public static Uri listUri(String screenName) {
        return Uri.parse(profileUrl(screenName) + "/lists");
    }

    public static Uri messageUri() {
        return Uri.parse("https://mobile.twitter.com/messages");
    }

    public static Uri followUri(String screenName) {
        return Uri.parse(profileUrl(screenName) + "/following");
    }

    public static Uri followerUri(String screenName) {
        return Uri.parse(profileUrl(screenName) + "/followers");
    }

    private static String profileUrl(String screenName) {
        return TWITTER_URL + screenName;
    }
}
