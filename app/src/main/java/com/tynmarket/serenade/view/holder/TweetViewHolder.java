package com.tynmarket.serenade.view.holder;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.FavoriteService;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.activity.SlideActivity;
import com.tynmarket.serenade.model.util.TweetUtil;
import com.tynmarket.serenade.model.util.TwitterUtil;
import com.tynmarket.serenade.view.adapter.TweetListAdapter;
import com.tynmarket.serenade.view.custom.TweetContentView;

import retrofit2.Call;

/**
 * Created by tyn-iMarket on 2017/12/18.
 */

public class TweetViewHolder extends RecyclerView.ViewHolder {
    private TweetListAdapter adapter;

    public Tweet tweet;
    private boolean favorited;

    public final LinearLayout retweetContainer;
    public final TextView retweetUserName;
    @SuppressWarnings("all")
    private final ImageView retweetByUser;

    // Tweet
    public final ImageView icon;
    public final TweetContentView tweetContent;

    // Quoted tweet
    public final TextView quotedName;
    public final TextView quotedScreenName;
    public final TextView quotedTweetText;
    public final ImageView quotedTweetPhoto;

    // Twitter Card Summary
    public final RelativeLayout cardSummary;
    public final ImageView cardSummaryImage;
    public final TextView cardSummaryLargeImageText;
    public final TextView cardSummaryTitle;
    public final TextView cardSummaryHost;

    // Slide
    public final Button slideButton;

    // Action
    public final TextView reply;
    public final TextView retweet;
    private final ImageView fav;

    public TweetViewHolder(View itemView) {
        super(itemView);
        this.retweetContainer = itemView.findViewById(R.id.retweet_container);
        this.retweetUserName = itemView.findViewById(R.id.retweet_user_name);
        this.retweetByUser = itemView.findViewById(R.id.retweet_by_user);
        this.icon = itemView.findViewById(R.id.icon);
        this.tweetContent = itemView.findViewById(R.id.tweet_content);
        this.quotedName = itemView.findViewById(R.id.quoted_name);
        this.quotedScreenName = itemView.findViewById(R.id.quoted_screen_name);
        this.quotedTweetText = itemView.findViewById(R.id.quoted_tweet_text);
        this.quotedTweetPhoto = itemView.findViewById(R.id.quoted_tweet_photo);
        this.cardSummary = itemView.findViewById(R.id.card_summary);
        this.cardSummaryImage = itemView.findViewById(R.id.card_summary_image);
        this.cardSummaryLargeImageText = itemView.findViewById(R.id.card_summary_large_image_text);
        this.cardSummaryTitle = itemView.findViewById(R.id.card_summary_title);
        this.cardSummaryHost = itemView.findViewById(R.id.card_summary_host);
        this.slideButton = itemView.findViewById(R.id.slide_button);
        this.reply = itemView.findViewById(R.id.reply);
        this.retweet = itemView.findViewById(R.id.retweet);
        this.fav = itemView.findViewById(R.id.fav);

        // Open profile
        // TODO: Set listener to quoted status
        setOnIconClickListener();
        // TODO: Set listener to quoted status tweetText
        // Open Twitter Card URL
        setOnCardSummaryClickListener();
        // Open slide
        setOnSlideButtonClickListener();
        // TODO: Show fullscreen image

        // TODO: already created/destroyed
        // TODO: Animation
        // TODO: Disable double click
        // TODO: Click RT/QT tweet
        fav.setOnClickListener((View v) -> {
            TwitterApiClient client = TwitterCore.getInstance().getApiClient();
            FavoriteService service = client.getFavoriteService();

            if (favorited) {
                favorited = false;
                Call<Tweet> call = service.destroy(tweet.id, true);
                call.enqueue(new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> result) {
                        Log.d("Serenade", "fav destroy: success");
                        Toast.makeText(v.getContext(), "いいねを取り消しました。", Toast.LENGTH_SHORT).show();
                        adapter.replaceTweet(getAdapterPosition(), result.data);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Log.d("Serenade", "fav destroy: failure");
                        Toast.makeText(v.getContext(), "いいねを取り消せませんでした。", Toast.LENGTH_SHORT).show();
                    }
                });
                fav.setImageResource(R.drawable.fav_off);
            } else {
                favorited = true;
                Call<Tweet> call = service.create(tweet.id, true);
                call.enqueue(new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> result) {
                        Log.d("Serenade", "fav create: success");
                        Toast.makeText(v.getContext(), "いいねに追加しました。", Toast.LENGTH_SHORT).show();
                        adapter.replaceTweet(getAdapterPosition(), result.data);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Toast.makeText(v.getContext(), "いいねに追加できませんでした。", Toast.LENGTH_SHORT).show();
                        Log.d("Serenade", "fav create: failure");
                    }
                });
                fav.setImageResource(R.drawable.fav_on);
            }
        });
    }

    public void setAdapter(TweetListAdapter adapter) {
        this.adapter = adapter;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
        fav.setImageResource(favorited ? R.drawable.fav_on : R.drawable.fav_off);
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

    private void setOnCardSummaryClickListener() {
        cardSummary.setOnClickListener(v -> {
            String url = TweetUtil.expandedUrl(tweet);
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
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
