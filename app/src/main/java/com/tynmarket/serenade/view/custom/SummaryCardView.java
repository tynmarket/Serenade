package com.tynmarket.serenade.view.custom;

import android.content.Context;
import android.content.Intent;
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
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.databinding.SummaryCardBinding;
import com.tynmarket.serenade.model.TwitterCard;

/**
 * Created by tynmarket on 2018/03/19.
 */

public class SummaryCardView extends RelativeLayout implements View.OnClickListener {
    public SummaryCardBinding binding;

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

    @BindingAdapter("image")
    public static void setImage(ImageView view, TwitterCard card) {
        if (card != null && card.isSummary()) {
            Glide.with(view.getContext()).load(card.image).into(view);
            view.setVisibility(VISIBLE);
        } else {
            view.setVisibility(GONE);
            view.setImageDrawable(null);
        }
    }

    @BindingAdapter("imageText")
    public static void setImageText(TextView view, TwitterCard card) {
        if (card != null && card.isSummaryLargeImage()) {
            view.setText(card.domain);
            view.setVisibility(VISIBLE);
        } else {
            view.setVisibility(GONE);
            view.setText(null);
        }
    }

    @Override
    public void onClick(View v) {
        String url = binding.getCard().url;
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        v.getContext().startActivity(intent);
    }
}
