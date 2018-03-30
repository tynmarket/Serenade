package com.tynmarket.serenade.view.text;

import android.net.Uri;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.tynmarket.serenade.model.util.ActivityHelper;

/**
 * Created by tynmarket on 2018/03/29.
 */

public abstract class TweetTextClickableSpan extends ClickableSpan {
    @Override
    public void onClick(View widget) {
        Uri uri = onClickSpannable();
        ActivityHelper.startUriActivity(widget.getContext(), uri);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

    abstract public Uri onClickSpannable();
}
