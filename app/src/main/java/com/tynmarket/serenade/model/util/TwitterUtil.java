package com.tynmarket.serenade.model.util;

import android.net.Uri;

/**
 * Created by tynmarket on 2018/02/03.
 */

public class TwitterUtil {
    private static final String TWITTER_URL = "https://twitter.com/";

    public static Uri profileUri(String screenNameWithPrefix) {
        return Uri.parse(TWITTER_URL + screenNameWithPrefix.substring(1));
    }
}
