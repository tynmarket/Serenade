package com.tynmarket.serenade.view.custom;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tynmarket.serenade.R;
import com.tynmarket.serenade.databinding.TweetActionBinding;

/**
 * Created by tynmarket on 2018/03/21.
 */

public class TweetActionView extends LinearLayout {
    public TweetActionBinding binding;

    public TweetActionView(Context context) {
        super(context);
    }

    public TweetActionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater, R.layout.tweet_action, this, true);
    }

    public TweetActionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
