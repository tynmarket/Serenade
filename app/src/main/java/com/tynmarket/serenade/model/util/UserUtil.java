package com.tynmarket.serenade.model.util;

import com.twitter.sdk.android.core.models.User;

/**
 * Created by tynmarket on 2018/03/03.
 */

public class UserUtil {
    public static String get200xProfileImageUrlHttps(User user) {
        return user.profileImageUrlHttps.replace("_normal", "_200x200");
    }
}
