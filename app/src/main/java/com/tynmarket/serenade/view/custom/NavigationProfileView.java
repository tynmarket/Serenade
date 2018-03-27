package com.tynmarket.serenade.view.custom;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.twitter.sdk.android.core.models.User;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.model.util.UserUtil;

/**
 * Created by tynmarket on 2018/03/26.
 */

public class NavigationProfileView extends LinearLayout {
    public NavigationProfileView(Context context) {
        super(context);
    }

    public NavigationProfileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.navigation_profile, this);
    }

    public NavigationProfileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @BindingAdapter("profileImage")
    public static void setProfileImage(ImageView v, User user) {
        Glide.with(v.getContext()).load(UserUtil.get200xProfileImageUrlHttps(user)).into(v);
    }
}
