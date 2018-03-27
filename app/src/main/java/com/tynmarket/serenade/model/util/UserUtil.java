package com.tynmarket.serenade.model.util;

import com.twitter.sdk.android.core.models.User;
import com.tynmarket.serenade.R;

import java.text.NumberFormat;

/**
 * Created by tynmarket on 2018/03/03.
 */

public class UserUtil {
    public static String get200xProfileImageUrlHttps(User user) {
        return user.profileImageUrlHttps.replace("_normal", "_200x200");
    }

    public static String screenName(User user) {
        return String.format("@%s", user.screenName);
    }

    public static String follow(User user) {
        String followCount = NumberFormat.getInstance().format(user.friendsCount);
        String follow = Resource.getResources().getString(R.string.follow);

        return String.format("%s %s", followCount, follow);
    }

    public static String follower(User user) {
        String followerCount = NumberFormat.getInstance().format(user.followersCount);
        String follower = Resource.getResources().getString(R.string.follower);

        return String.format("%s %s", followerCount, follower);
    }
}
