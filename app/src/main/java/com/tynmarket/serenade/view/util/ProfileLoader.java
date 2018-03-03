package com.tynmarket.serenade.view.util;

import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.twitter.sdk.android.core.models.User;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.model.util.UserUtil;

/**
 * Created by tynmarket on 2018/03/03.
 */

public class ProfileLoader {
    public static void loadProfile(AppCompatActivity activity, User user) {
        ImageView icon = activity.findViewById(R.id.profile_icon);
        TextView name = activity.findViewById(R.id.profile_name);
        TextView screenName = activity.findViewById(R.id.profile_screen_name);

        // icon
        RequestManager manager = Glide.with(activity);
        manager.load(UserUtil.get200xProfileImageUrlHttps(user)).into(icon);

        // name
        name.setText(user.name);

        // screen name
        screenName.setText(String.format("@%s", user.screenName));
    }
}
