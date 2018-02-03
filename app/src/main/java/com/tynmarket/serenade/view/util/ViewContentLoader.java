package com.tynmarket.serenade.view.util;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

/**
 * Created by tyn-iMarket on 2018/01/20.
 */

public class ViewContentLoader {
    private RequestManager manager;

    public ViewContentLoader() {
    }

    public ViewContentLoader(RequestManager manager) {
        this.manager = manager;
    }

    public void loadImage(ImageView view, String url, int height,
                          @Nullable Integer leftMargin, @Nullable Integer topMargin,
                          @Nullable Integer rightMargin, @Nullable Integer bottomMargin) {
        manager.load(url).into(view);
        ViewGroup.MarginLayoutParams lp = setMargin(view, leftMargin, topMargin, rightMargin, bottomMargin);
        lp.height = height;
        view.setLayoutParams(lp);
    }

    public void unloadImage(ImageView view,
                            @Nullable Integer leftMargin, @Nullable Integer topMargin,
                            @Nullable Integer rightMargin, @Nullable Integer bottomMargin) {
        view.setImageDrawable(null);
        ViewGroup.MarginLayoutParams lp = setMargin(view, leftMargin, topMargin, rightMargin, bottomMargin);
        lp.height = 0;
        view.setLayoutParams(lp);
    }

    public void setText(TextView view, String text,
                        @Nullable Integer leftMargin, @Nullable Integer topMargin,
                        @Nullable Integer rightMargin, @Nullable Integer bottomMargin) {
        view.setText(text);
        ViewGroup.MarginLayoutParams lp = setMargin(view, leftMargin, topMargin, rightMargin, bottomMargin);
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        view.setLayoutParams(lp);
    }

    public void unsetText(TextView view,
                          @Nullable Integer leftMargin, @Nullable Integer topMargin,
                          @Nullable Integer rightMargin, @Nullable Integer bottomMargin) {
        view.setText(null);
        ViewGroup.MarginLayoutParams lp = setMargin(view, leftMargin, topMargin, rightMargin, bottomMargin);
        lp.height = 0;
        view.setLayoutParams(lp);
    }

    private ViewGroup.MarginLayoutParams setMargin(View view,
                                                   @Nullable Integer leftMargin, @Nullable Integer topMargin,
                                                   @Nullable Integer rightMargin, @Nullable Integer bottomMargin) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        lp.setMargins(
                leftMargin != null ? leftMargin : lp.leftMargin,
                topMargin != null ? topMargin : lp.topMargin,
                rightMargin != null ? rightMargin : lp.rightMargin,
                bottomMargin != null ? bottomMargin : lp.bottomMargin);

        return lp;
    }
}
