package com.tynmarket.serenade.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.twitter.sdk.android.core.models.Tweet;
import com.tynmarket.serenade.R;

/**
 * Created by tyn-iMarket on 2017/12/18.
 */

public class TweetViewHolder extends RecyclerView.ViewHolder {
    public Tweet tweet;
    public boolean favorited;

    public ImageView icon;
    public TextView name;
    public TextView screenName;
    public TextView createdAt;
    public TextView tweetText;
    public TextView talk;
    public TextView retweet;
    public ImageView fav;


    public TweetViewHolder(View itemView) {
        super(itemView);
        this.icon = (ImageView) itemView.findViewById(R.id.icon);
        this.name = (TextView) itemView.findViewById(R.id.name);
        this.screenName = (TextView) itemView.findViewById(R.id.screen_name);
        this.createdAt = (TextView) itemView.findViewById(R.id.created_at);
        this.tweetText = (TextView) itemView.findViewById(R.id.tweet_text);
        this.talk = (TextView) itemView.findViewById(R.id.talk);
        this.retweet = (TextView) itemView.findViewById(R.id.retweet);
        this.fav = (ImageView) itemView.findViewById(R.id.fav);
        fav.setOnClickListener((View v) -> {
            if (favorited) {
                favorited = false;
                fav.setImageResource(R.drawable.fav_off);
            } else {
                favorited = true;
                fav.setImageResource(R.drawable.fav_on);
            }
        });
    }
}
