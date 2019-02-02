package com.tynmarket.serenade.model;

import com.tynmarket.serenade.core.TwitterApiClient;
import com.tynmarket.serenade.core.TwitterApiException;
import com.tynmarket.serenade.core.TwitterCore;
import com.tynmarket.serenade.core.models.Tweet;
import com.tynmarket.serenade.core.services.FavoriteService;
import com.tynmarket.serenade.model.util.RetrofitObserver;
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
                    onSuccess.result();
                }, throwable -> {
                    if (throwable instanceof TwitterApiException) {
                        // Already favorited
                        if (((TwitterApiException) throwable).getErrorCode() == 139) {
                            onSuccess.result();
                        } else {
                            onFailure.result();
                        }
                    } else {
                        onFailure.result();
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
