package com.tynmarket.serenade.view.holder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.tynmarket.serenade.view.adapter.TweetListAdapter;

import retrofit2.Call;

/**
 * Created by tyn-iMarket on 2017/12/18.
 */

public class TweetViewHolder extends RecyclerView.ViewHolder {
    private TweetListAdapter adapter;

    public Tweet tweet;
    private boolean favorited;

    public TextView retweetUserName;
    public ImageView retweetByUser;
    public ImageView icon;
    public TextView name;
    public TextView screenName;
    public TextView createdAt;
    public TextView tweetText;
    public ImageView tweetPhoto;
    public TextView quotedName;
    public TextView quotedScreenName;
    public TextView quotedTweetText;
    public ImageView quotedTweetPhoto;
    public TextView reply;
    public TextView retweet;
    private ImageView fav;

    public TweetViewHolder(View itemView) {
        super(itemView);
        this.retweetUserName = (TextView) itemView.findViewById(R.id.retweet_user_name);
        this.retweetByUser = (ImageView) itemView.findViewById(R.id.retweet_by_user);
        this.icon = (ImageView) itemView.findViewById(R.id.icon);
        this.name = (TextView) itemView.findViewById(R.id.name);
        this.screenName = (TextView) itemView.findViewById(R.id.screen_name);
        this.createdAt = (TextView) itemView.findViewById(R.id.created_at);
        this.tweetText = (TextView) itemView.findViewById(R.id.tweet_text);
        this.tweetPhoto = (ImageView) itemView.findViewById(R.id.tweet_photo);
        this.quotedName = (TextView) itemView.findViewById(R.id.quoted_name);
        this.quotedScreenName = (TextView) itemView.findViewById(R.id.quoted_screen_name);
        this.quotedTweetText = (TextView) itemView.findViewById(R.id.quoted_tweet_text);
        this.quotedTweetPhoto = (ImageView) itemView.findViewById(R.id.quoted_tweet_photo);
        this.reply = (TextView) itemView.findViewById(R.id.reply);
        this.retweet = (TextView) itemView.findViewById(R.id.retweet);
        this.fav = (ImageView) itemView.findViewById(R.id.fav);

        // TODO: already created/destroyed
        // TODO: Animation
        // TODO: Disable double click
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
}
