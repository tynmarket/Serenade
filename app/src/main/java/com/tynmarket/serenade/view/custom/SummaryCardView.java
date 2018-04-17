package com.tynmarket.serenade.view.custom;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.databinding.SummaryCardBinding;
import com.tynmarket.serenade.model.entity.TwitterCard;
import com.tynmarket.serenade.model.util.ActivityHelper;

/**
 * Created by tynmarket on 2018/03/19.
 */

public class SummaryCardView extends RelativeLayout implements View.OnClickListener {
    public SummaryCardBinding binding;
    private static RequestOptions summaryRequestOptions = RequestOptions.placeholderOf(
            R.drawable.border_summary_card_left);
    private static RequestOptions summaryLargeRequestOptions = RequestOptions.placeholderOf(
            R.drawable.border_summary_card_large_top);

    public SummaryCardView(Context context) {
        super(context);
    }

    public SummaryCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater, R.layout.summary_card, this, true);

        setOnClickListener(this);
    }

    public SummaryCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @BindingAdapter("summary_card_with_large_image_visibility")
    public static void setSummaryCardWithLargeImageVisibility(View view, TwitterCard card) {
        if (card != null && card.isSummaryLargeImage()) {
            view.setVisibility(VISIBLE);
        } else {
            view.setVisibility(GONE);
        }
    }

    @BindingAdapter("summary_card_visibility")
    public static void setSummaryCardVisibility(View view, TwitterCard card) {
        if (card != null && card.isSummary()) {
            view.setVisibility(VISIBLE);
        } else {
            view.setVisibility(GONE);
        }
    }

    @BindingAdapter("image")
    public static void setImage(ImageView view, TwitterCard card) {
        if (card != null && card.isSummary()) {
            Glide.with(view.getContext()).load(card.image).apply(summaryRequestOptions).into(view);
            view.setVisibility(VISIBLE);
        } else {
            view.setVisibility(GONE);
            view.setImageDrawable(null);
        }
    }

    @BindingAdapter("imageText")
    public static void setImageText(TextView view, TwitterCard card) {
        if (card != null && card.isSummaryLargeImage() && !card.showLargeImage) {
            view.setText(card.domain);
            view.setVisibility(VISIBLE);
        } else {
            view.setVisibility(GONE);
            view.setText(null);
        }
    }

    @BindingAdapter("large_image")
    public static void setLargeImage(ImageView view, TwitterCard card) {
        if (card != null && card.isSummaryLargeImage() && card.showLargeImage) {
            Glide.with(view.getContext()).load(card.image).apply(summaryLargeRequestOptions).into(view);
            view.setVisibility(VISIBLE);
        } else {
            view.setVisibility(GONE);
            view.setImageDrawable(null);
        }
    }

    @Override
    public void onClick(View v) {
        String url = binding.getCard().url;
        Uri uri = Uri.parse(url);
        ActivityHelper.startUriActivity(v.getContext(), uri);
    }
}
