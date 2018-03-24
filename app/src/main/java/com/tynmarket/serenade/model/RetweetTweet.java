package com.tynmarket.serenade.model;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterApiException;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.tynmarket.serenade.view.listner.TweetActionListener;

import retrofit2.Call;

/**
 * Created by tynmarket on 2018/03/21.
 */

public class RetweetTweet {
    public static void retweet(Tweet tweet, TweetActionListener onSuccess, TweetActionListener onFailure) {
        TwitterApiClient client = TwitterCore.getInstance().getApiClient();
        StatusesService service = client.getStatusesService();
        Call<Tweet> call = service.retweet(tweet.id, true);

        RetrofitObserver
                .create(call)
                .subscribe(newTweet -> {
                    onSuccess.result();
                }, throwable -> {
                    if (throwable instanceof TwitterApiException) {
                        // Already retweeted
                        if (((TwitterApiException) throwable).getErrorCode() == 327) {
                            onSuccess.result();
                        } else {
                            onFailure.result();
                        }
                    } else {
                        onFailure.result();
                    }
                });
    }

    public static void unRetweet(Tweet tweet, TweetActionListener onSuccess, TweetActionListener onFailure) {
        TwitterApiClient client = TwitterCore.getInstance().getApiClient();
        StatusesService service = client.getStatusesService();
        Call<Tweet> call = service.unretweet(tweet.id, true);

        RetrofitObserver
                .create(call)
                .subscribe(newTweet -> {
                    onSuccess.result();
                }, throwable -> {
                    if (throwable instanceof TwitterApiException) {
                        if (((TwitterApiException) throwable).getStatusCode() == 404) {
                            onSuccess.result();
                        } else {
                            onFailure.result();
                        }
                    } else {
                        onFailure.result();
                    }
                });
    }
}
