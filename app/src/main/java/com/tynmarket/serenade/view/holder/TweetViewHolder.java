package com.tynmarket.serenade.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tynmarket.serenade.R;

/**
 * Created by tyn-iMarket on 2017/12/18.
 */

public class TweetViewHolder extends RecyclerView.ViewHolder {
    private View view;
    public TextView name;
    public TextView screenName;
    public TextView createdAt;
    public TextView tweetText;
    public TextView talk;
    public TextView retweet;
    public TextView fav;


    public TweetViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        this.name = (TextView) itemView.findViewById(R.id.name);
        this.screenName = (TextView) itemView.findViewById(R.id.screen_name);
        this.createdAt = (TextView) itemView.findViewById(R.id.created_at);
        this.tweetText = (TextView) itemView.findViewById(R.id.tweet_text);
        this.talk = (TextView) itemView.findViewById(R.id.talk);
        this.retweet = (TextView) itemView.findViewById(R.id.retweet);
        this.fav = (TextView) itemView.findViewById(R.id.fav);
    }
}
