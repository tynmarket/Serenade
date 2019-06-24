package com.tynmarket.serenade.view.holder;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.databinding.ListItemTweetBinding;
import com.tynmarket.serenade.model.FavoriteTweet;
import com.tynmarket.serenade.model.RetweetTweet;
import com.tynmarket.serenade.model.entity.TweetWithTwitterCard;
import com.tynmarket.serenade.model.entity.TwitterCard;
import com.tynmarket.serenade.model.util.ActivityHelper;
import com.tynmarket.serenade.model.util.FirebaseAnalyticsHelper;
import com.tynmarket.serenade.model.util.LogUtil;
import com.tynmarket.serenade.model.util.TweetMapper;
import com.tynmarket.serenade.model.util.TweetUtil;
import com.tynmarket.serenade.model.util.TwitterUtil;
import com.tynmarket.serenade.model.util.UserUtil;
import com.tynmarket.serenade.view.adapter.TweetListAdapter;

/**
 * Created by tynmarket on 2017/12/18.
 */

public class TweetViewHolder extends RecyclerView.ViewHolder {
    public ListItemTweetBinding binding;
    private FirebaseAnalyticsHelper analytics;

    private TweetListAdapter adapter;

    public TweetViewHolder(View itemView) {
        super(itemView);

        binding = DataBindingUtil.bind(itemView);
        analytics = new FirebaseAnalyticsHelper(itemView.getContext());

        // Open Tweet
        setOnLayoutClickListener();

        // Open profile
        setOnRetweetUserNameClickListener();
        setOnProfileClickListener();

        // TODO: Animation
        // TODO: Move to TweetActionView
        setOnReplyClickListener();
        setOnFavoriteClickListener();
        setOnRetweetClickListener();
    }

    @BindingAdapter("profileImage")
    public static void setProfileImage(ImageView view, Tweet tweet) {
        User user = TweetUtil.tweetOrRetweetedStatus(tweet).user;
        String profileImageUrl = UserUtil.get200xProfileImageUrlHttps(user);
        // TODO: GlideApp
        // TODO: Loading image or border
        Glide.with(view.getContext()).load(profileImageUrl).into(view);
    }

    public void setAdapter(TweetListAdapter adapter) {
        this.adapter = adapter;
    }

    public void setTweetAndCardToBindings(Tweet tweet, TwitterCard card) {
        if (card != null) {
            tweet = new TweetWithTwitterCard(tweet, card);
        }

        setTweetToBindings(tweet);
        binding.setShowQuotedStatus(tweet.quotedStatus != null);
        setCardToBindings(card);
    }

    private void setCardToBindings(TwitterCard card) {
        Tweet tweet = getTweet();
        String expandedUrl = TweetUtil.expandedUrl(tweet);

        if (card != null) {
            card.url = expandedUrl;
            card.domain = TweetUtil.expandedUrlDomain(tweet);
            card.host = TweetUtil.expandedUrlHost(tweet);
        }

        binding.setShowSummaryCard(card != null && card.showSummaryCard());
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

    private void setOnLayoutClickListener() {
        binding.getRoot().setOnClickListener(v -> {
            Tweet tweet = binding.getTweet();
            analytics.logViewTweet();

            Uri uri = TwitterUtil.tweetUri(tweet.user.screenName, tweet.idStr);
            ActivityHelper.startUriActivity(v.getContext(), uri);
        });
    }

    private void setOnRetweetUserNameClickListener() {
        binding.retweetUserName.setOnClickListener(v -> {
            User user = getTweet().user;
            Uri uri = TwitterUtil.profileUri(user.screenName);
            ActivityHelper.startUriActivity(v.getContext(), uri);
        });
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void setOnProfileClickListener() {
        // TODO: State pressed
        // http://snowrobin.tumblr.com/post/62229276876/androidimageview%E3%81%AB%E3%82%A8%E3%83%95%E3%82%A7%E3%82%AF%E3%83%88%E3%82%92%E4%BB%98%E4%B8%8E%E3%81%99%E3%82%8B
        binding.profile.setOnClickListener(v -> {
            User user = TweetUtil.tweetOrRetweetedStatus(getTweet()).user;
            Uri uri = TwitterUtil.profileUri(user.screenName);
            ActivityHelper.startUriActivity(v.getContext(), uri);
        });
    }

    private void setOnReplyClickListener() {
        binding.tweetAction.binding.reply.setOnClickListener(v -> {
            Tweet tweet = TweetUtil.tweetOrRetweetedStatus(getTweet());
            analytics.logReply(tweet);

            Uri uri = TwitterUtil.replyUri(tweet.idStr);
            // TODO: startActivityForResult
            ActivityHelper.startUriActivity(v.getContext(), uri);
        });
    }

    private void setOnFavoriteClickListener() {
        binding.tweetAction.binding.fav.setOnClickListener((View v) -> {
            Tweet tweet = getTweet();
            Tweet newTweet = TweetMapper.withFavorited(tweet, !tweet.favorited);
            replaceTweet(newTweet);

            if (!tweet.favorited) {
                analytics.logFavorite(tweet);

                FavoriteTweet.favorite(tweet, () -> {
                    Toast.makeText(v.getContext(), R.string.favorite_success, Toast.LENGTH_SHORT).show();
                }, () -> {
                    LogUtil.d("favorite: failure");
                    replaceTweet(tweet);
                    Toast.makeText(v.getContext(), R.string.favorite_failure, Toast.LENGTH_SHORT).show();
                });
            } else {
                FavoriteTweet.unFavorite(tweet, () -> {
                    Toast.makeText(v.getContext(), R.string.cancel_favorite_success, Toast.LENGTH_SHORT).show();
                }, () -> {
                    LogUtil.d("unFavorite: failure");
                    replaceTweet(tweet);
                    Toast.makeText(v.getContext(), R.string.cancel_favorite_failure, Toast.LENGTH_SHORT).show();
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
                analytics.logRetweet(tweet);

                RetweetTweet.retweet(tweet, () -> {
                    Toast.makeText(v.getContext(), R.string.retweet_success, Toast.LENGTH_SHORT).show();
                }, () -> {
                    LogUtil.d("retweet: failure");
                    replaceTweet(tweet);
                    Toast.makeText(v.getContext(), R.string.retweet_failure, Toast.LENGTH_SHORT).show();
                });
            } else {
                RetweetTweet.unRetweet(tweet, () -> {
                    Toast.makeText(v.getContext(), R.string.cancel_retweet_success, Toast.LENGTH_SHORT).show();
                }, () -> {
                    LogUtil.d("unRetweet: failure");
                    replaceTweet(tweet);
                    Toast.makeText(v.getContext(), R.string.cancel_retweet_failure, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
