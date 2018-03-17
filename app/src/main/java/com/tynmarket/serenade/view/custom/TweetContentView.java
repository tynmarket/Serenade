package com.tynmarket.serenade.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.tynmarket.serenade.R;

/**
 * Created by tynmarket on 2018/03/17.
 */

public class TweetContentView extends RelativeLayout {
    public TweetContentView(Context context) {
        super(context);
    }

    public TweetContentView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.tweet_content, this);
    }

    public TweetContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
