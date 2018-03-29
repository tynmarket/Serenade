package com.tynmarket.serenade.view.text;

import android.content.Intent;
import android.net.Uri;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by tynmarket on 2018/03/29.
 */

public abstract class TweetTextClickableSpan extends ClickableSpan {
    @Override
    public void onClick(View widget) {
        Uri uri = onClickSpannable();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        // TODO: Transition
        widget.getContext().startActivity(intent);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

    abstract public Uri onClickSpannable();
}
