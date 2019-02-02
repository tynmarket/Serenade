package com.tynmarket.serenade.view.custom;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.tynmarket.serenade.core.models.User;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.databinding.NavigationProfileBinding;
import com.tynmarket.serenade.model.util.ActivityHelper;
import com.tynmarket.serenade.model.util.FirebaseAnalyticsHelper;
import com.tynmarket.serenade.model.util.TwitterUtil;
import com.tynmarket.serenade.model.util.UserUtil;
import com.tynmarket.serenade.view.listner.OnNavigationItemClickListener;

/**
 * Created by tynmarket on 2018/03/26.
 */

public class NavigationProfileView extends RelativeLayout {
    public NavigationProfileBinding binding;
    private FirebaseAnalyticsHelper analytics;
    private OnNavigationItemClickListener listener;

    public NavigationProfileView(Context context) {
        super(context);
    }

    public NavigationProfileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater, R.layout.navigation_profile, this, true);
        analytics = new FirebaseAnalyticsHelper(context);

        // Open following
        setOnFollowClickListener();
        // Open followers
        setOnFollowerClickListener();
    }

    public NavigationProfileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @BindingAdapter("profileImage")
    public static void setProfileImage(ImageView v, User user) {
        Glide.with(v.getContext()).load(UserUtil.get200xProfileImageUrlHttps(user)).into(v);
    }

    public void setOnNavigationItemClickListener(OnNavigationItemClickListener listener) {
        this.listener = listener;
    }

    private void setOnFollowClickListener() {
        binding.follow.setOnClickListener(v -> {
            analytics.logViewFollow();
            listener.onClick(v);

            // Open follow
            Uri uri = TwitterUtil.followUri(binding.getUser().screenName);
            ActivityHelper.startUriActivity(v.getContext(), uri);
        });
    }

    private void setOnFollowerClickListener() {
        binding.follower.setOnClickListener(v -> {
            analytics.logViewFollower();
            listener.onClick(v);

            // Open follower
            Uri uri = TwitterUtil.followerUri(binding.getUser().screenName);
            ActivityHelper.startUriActivity(v.getContext(), uri);
        });
    }
}
