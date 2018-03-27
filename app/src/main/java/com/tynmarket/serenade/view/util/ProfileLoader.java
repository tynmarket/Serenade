package com.tynmarket.serenade.view.util;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;

import com.twitter.sdk.android.core.models.User;

/**
 * Created by tynmarket on 2018/03/03.
 */

public class ProfileLoader {
    @SuppressLint("SetTextI18n")
    public static void loadProfile(AppCompatActivity activity, User user) {

        /*
        ImageView icon = activity.findViewById(R.id.profile_icon);
        TextView name = activity.findViewById(R.id.profile_name);
        TextView screenName = activity.findViewById(R.id.profile_screen_name);
        TextView followAndFollower = activity.findViewById(R.id.profile_follow_and_follower);
        //TextView profileLink = activity.findViewById(R.id.profile_profile_link);

        // icon
        RequestManager manager = Glide.with(activity);
        manager.load(UserUtil.get200xProfileImageUrlHttps(user)).into(icon);

        // name
        name.setText(user.name);

        // screen name
        screenName.setText(String.format("@%s", user.screenName));

        // follow and follower
        String followCount = NumberFormat.getInstance().format(488);
        String follow = Resource.getResources().getString(R.string.follow);

        String followerCount = NumberFormat.getInstance().format(1958);
        String follower = Resource.getResources().getString(R.string.follower);

        String text = String.format("%s %s  %s %s", followCount, follow, followerCount, follower);
        followAndFollower.setText(text);
        */
    }
}
