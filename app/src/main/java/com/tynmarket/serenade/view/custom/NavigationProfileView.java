package com.tynmarket.serenade.view.custom;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tynmarket.serenade.R;
import com.tynmarket.serenade.databinding.NavigationProfileBinding;

/**
 * Created by tynmarket on 2018/03/26.
 */

public class NavigationProfileView extends LinearLayout {
    public NavigationProfileBinding binding;

    public NavigationProfileView(Context context) {
        super(context);
    }

    public NavigationProfileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater, R.layout.navigation_profile, this, true);
    }

    public NavigationProfileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
