package com.tynmarket.serenade.view.holder;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.twitter.sdk.android.core.models.Tweet;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.activity.SlideActivity;
import com.tynmarket.serenade.databinding.ListItemTweetBinding;
import com.tynmarket.serenade.model.FavoriteTweet;
import com.tynmarket.serenade.model.util.TweetUtil;
import com.tynmarket.serenade.model.util.TwitterUtil;
import com.tynmarket.serenade.view.adapter.TweetListAdapter;

/**
 * Created by tyn-iMarket on 2017/12/18.
 */

public class TweetViewHolder extends RecyclerView.ViewHolder {
    public ListItemTweetBinding binding;
    private TweetListAdapter adapter;

    public Tweet tweet;
    private boolean favorited;

    // Tweet
    public final ImageView icon;

    // Slide
    public final Button slideButton;

    // Action
    private final ImageView fav;

    public TweetViewHolder(View itemView) {
        super(itemView);

        binding = DataBindingUtil.bind(itemView);
        this.icon = itemView.findViewById(R.id.icon);
        this.slideButton = itemView.findViewById(R.id.slide_button);
        this.fav = binding.tweetAction.binding.fav;

        // Open profile
        // TODO: Set listener to quoted status
        setOnIconClickListener();
        // TODO: Set listener to quoted status tweetText
        // Open Twitter Card URL
        // Open slide
        setOnSlideButtonClickListener();
        // TODO: Show fullscreen image

        // TODO: already created/destroyed
        // TODO: Animation
        // TODO: Disable double click
        fav.setOnClickListener((View v) -> {
            if (!favorited) {
                favorited = true;

                FavoriteTweet.favorite(tweet, newTweet -> {
                    Toast.makeText(v.getContext(), "いいねに追加しました。", Toast.LENGTH_SHORT).show();
                    adapter.replaceTweet(getAdapterPosition(), tweet);
                }, newTweet -> {
                    Toast.makeText(v.getContext(), "いいねに追加できませんでした。", Toast.LENGTH_SHORT).show();
                    fav.setImageResource(R.drawable.fav_off);
                    Log.d("Serenade", "fav create: failure");
                });

                fav.setImageResource(R.drawable.fav_on);
            } else {
                favorited = false;

                FavoriteTweet.unFavorite(tweet, newTweet -> {
                    Toast.makeText(v.getContext(), "いいねを取り消しました。", Toast.LENGTH_SHORT).show();
                    adapter.replaceTweet(getAdapterPosition(), tweet);
                }, newTweet -> {
                    Log.d("Serenade", "fav destroy: failure");
                    Toast.makeText(v.getContext(), "いいねを取り消せませんでした。", Toast.LENGTH_SHORT).show();
                    fav.setImageResource(R.drawable.fav_on);
                });

                fav.setImageResource(R.drawable.fav_off);
            }
        });
    }

    public void setAdapter(TweetListAdapter adapter) {
        this.adapter = adapter;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void setOnIconClickListener() {
        // TODO: State pressed
        // http://snowrobin.tumblr.com/post/62229276876/androidimageview%E3%81%AB%E3%82%A8%E3%83%95%E3%82%A7%E3%82%AF%E3%83%88%E3%82%92%E4%BB%98%E4%B8%8E%E3%81%99%E3%82%8B
        icon.setOnClickListener(v -> {
            // TODO: Open correct profile when RT/QT
            Uri uri = TwitterUtil.profileUri(tweet.user.screenName);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            // TODO: Transition
            // https://developer.android.com/reference/android/app/Activity.html#overridePendingTransition(int, int)
            itemView.getContext().startActivity(intent);
        });
    }

    private void setOnSlideButtonClickListener() {
        // Open slide
        slideButton.setOnClickListener(v -> {
            String expandedUrl = TweetUtil.expandedUrl(tweet);
            Intent intent = new Intent(itemView.getContext(), com.tynmarket.serenade.activity.SlideActivity.class);
            intent.putExtra(SlideActivity.EXPANDED_URL, expandedUrl);
            itemView.getContext().startActivity(intent);
        });
    }
}
