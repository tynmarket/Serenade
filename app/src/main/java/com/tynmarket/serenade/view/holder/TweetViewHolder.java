package com.tynmarket.serenade.view.holder;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.twitter.sdk.android.core.models.Tweet;
import com.tynmarket.serenade.activity.SlideActivity;
import com.tynmarket.serenade.databinding.ListItemTweetBinding;
import com.tynmarket.serenade.model.FavoriteTweet;
import com.tynmarket.serenade.model.RetweetTweet;
import com.tynmarket.serenade.model.TweetMapper;
import com.tynmarket.serenade.model.TwitterCard;
import com.tynmarket.serenade.model.util.TweetUtil;
import com.tynmarket.serenade.model.util.TwitterUtil;
import com.tynmarket.serenade.view.adapter.TweetListAdapter;

/**
 * Created by tyn-iMarket on 2017/12/18.
 */

public class TweetViewHolder extends RecyclerView.ViewHolder {
    public ListItemTweetBinding binding;

    private TweetListAdapter adapter;

    public TweetViewHolder(View itemView) {
        super(itemView);

        binding = DataBindingUtil.bind(itemView);

        // Open profile
        // TODO: Set listener to quoted status
        setOnIconClickListener();
        // TODO: Set listener to quoted status tweetText
        // Open slide
        setOnSlideButtonClickListener();
        // TODO: Show fullscreen image

        // TODO: Animation
        // TODO: Move to TweetActionView
        setOnReplyClickListener();
        setOnFavoriteClickListener();
        setOnRetweetClickListener();
    }

    public void setAdapter(TweetListAdapter adapter) {
        this.adapter = adapter;
    }

    public void setTweetAndCardToBindings(Tweet tweet, TwitterCard card) {
        String expandedUrl = TweetUtil.expandedUrl(tweet);

        if (card != null) {
            card.url = expandedUrl;
            card.domain = TweetUtil.expandedUrlDomain(tweet);
            card.host = TweetUtil.expandedUrlHost(tweet);
        }

        setTweetToBindings(tweet);
        binding.setShowQuotedStatus(tweet.quotedStatus != null);
        binding.setShowSummaryCard(card != null && (card.isSummary() || card.isSummaryLargeImage()));
        binding.summaryCard.binding.setCard(card);
    }

    public Tweet getTweet() {
        return binding.getTweet();
    }

    private void setTweetToBindings(Tweet tweet) {
        binding.setTweet(tweet);

        // Tweet
        binding.tweetContent.setTweet(tweet);
        // Quote tweet
        binding.quoteTweetContent.setTweet(tweet.quotedStatus);
        // Tweet action
        binding.tweetAction.binding.setTweet(tweet);
    }

    private void replaceTweet(Tweet tweet) {
        adapter.replaceTweet(getAdapterPosition(), tweet);
        setTweetToBindings(tweet);
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void setOnIconClickListener() {
        // TODO: State pressed
        // http://snowrobin.tumblr.com/post/62229276876/androidimageview%E3%81%AB%E3%82%A8%E3%83%95%E3%82%A7%E3%82%AF%E3%83%88%E3%82%92%E4%BB%98%E4%B8%8E%E3%81%99%E3%82%8B
        binding.icon.setOnClickListener(v -> {
            // TODO: Open correct profile when RT/QT
            Uri uri = TwitterUtil.profileUri(getTweet().user.screenName);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            // TODO: Transition
            // https://developer.android.com/reference/android/app/Activity.html#overridePendingTransition(int, int)
            itemView.getContext().startActivity(intent);
        });
    }

    private void setOnSlideButtonClickListener() {
        // Open slide
        binding.slideButton.setOnClickListener(v -> {
            String expandedUrl = TweetUtil.expandedUrl(getTweet());
            Intent intent = new Intent(itemView.getContext(), com.tynmarket.serenade.activity.SlideActivity.class);
            intent.putExtra(SlideActivity.EXPANDED_URL, expandedUrl);
            itemView.getContext().startActivity(intent);
        });
    }

    private void setOnReplyClickListener() {
        binding.tweetAction.binding.reply.setOnClickListener(v -> {
            Tweet tweet = TweetUtil.tweetOrRetweetedStatus(getTweet());
            String url = "https://twitter.com/intent/tweet?in_reply_to=" + tweet.idStr;
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            // TODO: Transition
            // TODO: startActivityForResult
            v.getContext().startActivity(intent);
        });
    }

    private void setOnFavoriteClickListener() {
        binding.tweetAction.binding.fav.setOnClickListener((View v) -> {
            Tweet tweet = getTweet();
            Tweet newTweet = TweetMapper.withFavorited(tweet, !tweet.favorited);
            replaceTweet(newTweet);

            if (!tweet.favorited) {
                FavoriteTweet.favorite(tweet, () -> {
                    Toast.makeText(v.getContext(), "いいねに追加しました。", Toast.LENGTH_SHORT).show();
                }, () -> {
                    Log.d("Serenade", "favorite: failure");
                    replaceTweet(tweet);
                    Toast.makeText(v.getContext(), "いいねに追加できませんでした。", Toast.LENGTH_SHORT).show();
                });
            } else {
                FavoriteTweet.unFavorite(tweet, () -> {
                    Toast.makeText(v.getContext(), "いいねを取り消しました。", Toast.LENGTH_SHORT).show();
                }, () -> {
                    Log.d("Serenade", "unFavorite: failure");
                    replaceTweet(tweet);
                    Toast.makeText(v.getContext(), "いいねを取り消せませんでした。", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void setOnRetweetClickListener() {
        binding.tweetAction.binding.retweet.setOnClickListener((View v) -> {
            Tweet tweet = getTweet();
            Tweet newTweet = TweetMapper.withRetweeted(tweet, !tweet.retweeted);
            replaceTweet(newTweet);

            if (!tweet.retweeted) {
                RetweetTweet.retweet(tweet, () -> {
                    Toast.makeText(v.getContext(), "リツイートしました。", Toast.LENGTH_SHORT).show();
                }, () -> {
                    Log.d("Serenade", "retweet: failure");
                    replaceTweet(tweet);
                    Toast.makeText(v.getContext(), "リツイートできませんでした。", Toast.LENGTH_SHORT).show();
                });
            } else {
                RetweetTweet.unRetweet(tweet, () -> {
                    Toast.makeText(v.getContext(), "リツイートを取り消しました。", Toast.LENGTH_SHORT).show();
                }, () -> {
                    Log.d("Serenade", "unRetweet: failure");
                    replaceTweet(tweet);
                    Toast.makeText(v.getContext(), "リツイートを取り消せませんでした。", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
