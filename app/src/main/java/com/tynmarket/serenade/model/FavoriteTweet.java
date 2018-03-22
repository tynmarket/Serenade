package com.tynmarket.serenade.model;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterApiException;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.FavoriteService;
import com.tynmarket.serenade.view.listner.TweetActionListener;

import retrofit2.Call;

/**
 * Created by tynmarket on 2018/03/21.
 */

public class FavoriteTweet {
    public static void favorite(Tweet tweet, TweetActionListener onSuccess, TweetActionListener onFailure) {
        TwitterApiClient client = TwitterCore.getInstance().getApiClient();
        FavoriteService service = client.getFavoriteService();
        Call<Tweet> call = service.create(tweet.id, true);

        RetrofitObserver
                .create(call)
                .subscribe(newTweet -> {
                    onSuccess.result(newTweet);
                }, throwable -> {
                    if (throwable instanceof TwitterApiException) {
                        if (((TwitterApiException) throwable).getErrorCode() == 139) {
                            // TODO: Map tweet
                            onSuccess.result(tweet);
                        } else {
                            onFailure.result(null);
                        }
                    } else {
                        onFailure.result(null);
                    }
                });
    }

    public static void unFavorite(Tweet tweet, TweetActionListener onSuccess, TweetActionListener onFailure) {
        TwitterApiClient client = TwitterCore.getInstance().getApiClient();
        FavoriteService service = client.getFavoriteService();
        Call<Tweet> call = service.destroy(tweet.id, true);

        RetrofitObserver
                .create(call)
                .subscribe(newTweet -> {
                    onSuccess.result(newTweet);
                }, throwable -> {
                    if (throwable instanceof TwitterApiException) {
                        if (((TwitterApiException) throwable).getStatusCode() == 404) {
                            // TODO: Map tweet
                            onSuccess.result(tweet);
                        } else {
                            onFailure.result(null);
                        }
                    } else {
                        onFailure.result(null);
                    }
                });
    }
}
