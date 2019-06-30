package com.tynmarket.serenade.model.util;

import androidx.annotation.Nullable;

import com.twitter.sdk.android.core.models.User;
import com.tynmarket.serenade.R;

import java.text.NumberFormat;

/**
 * Created by tynmarket on 2018/03/03.
 */

public class UserUtil {
    @Nullable
    public static String get200xProfileImageUrlHttps(@Nullable User user) {
        if (user == null) {
            return null;
        }
        return user.profileImageUrlHttps.replace("_normal", "_200x200");
    }

    @Nullable
    public static String screenName(@Nullable User user) {
        if (user == null) {
            return null;
        }
        return String.format("@%s", user.screenName);
    }

    @Nullable
    public static String follow(@Nullable User user) {
        if (user == null) {
            return null;
        }

        String followCount = NumberFormat.getInstance().format(user.friendsCount);
        String follow = Resource.getResources().getString(R.string.follow);

        return String.format("%s %s", followCount, follow);
    }

    @Nullable
    public static String follower(@Nullable User user) {
        if (user == null) {
            return null;
        }

        String followerCount = NumberFormat.getInstance().format(user.followersCount);
        String follower = Resource.getResources().getString(R.string.follower);

        return String.format("%s %s", followerCount, follower);
    }
}
