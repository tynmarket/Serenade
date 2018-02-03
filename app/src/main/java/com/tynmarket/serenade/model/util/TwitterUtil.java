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

    private static String profileUrl(String screenName) {
        return TWITTER_URL + screenName;
    }
}
