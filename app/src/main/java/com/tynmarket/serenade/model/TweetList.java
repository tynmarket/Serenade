package com.tynmarket.serenade.model;

import android.annotation.SuppressLint;
import android.util.Log;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.FavoriteService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.tynmarket.serenade.api.OgpServeApi;
import com.tynmarket.serenade.event.LoadFailureTweetListEvent;
import com.tynmarket.serenade.event.LoadTweetListEvent;
import com.tynmarket.serenade.event.LoadTwitterCardsEvent;
import com.tynmarket.serenade.event.StartLoadTweetListEvent;
import com.tynmarket.serenade.model.util.DummyTweet;
import com.tynmarket.serenade.model.util.TweetUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tyn-iMarket on 2018/02/01.
 */

public class TweetList {
    private static final int ITEM_COUNT = 50;
    private static final String OGP_SERVE_URL = "http://35.230.36.246:8080/";

    private static final Retrofit retrofit = new Retrofit
            .Builder()
            .baseUrl(OGP_SERVE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // TODO: Caching
    public static void loadTweets(int sectionNumber, boolean refresh, Long maxId) {
        // TODO: Progress bar displayed infinitely?
        EventBus.getDefault().post(new StartLoadTweetListEvent(sectionNumber));
        Call<List<Tweet>> call = callApi(sectionNumber, maxId);

        // TODO: Lambda function?
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                Log.d("Serenade", String.format("loadTweets success: %d", sectionNumber));
                TweetUtil.debugTimeline(result.data);

                eventBus().post(new LoadTweetListEvent(sectionNumber, result.data, refresh));
            }

            @Override
            public void failure(TwitterException exception) {
                // TODO: Late limit(Status 429)
                Log.d("Serenade", String.format("loadTweets failure: %d", sectionNumber));

                eventBus().post(new LoadFailureTweetListEvent(sectionNumber));
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public static void loadTwitterCards(int sectionNumber) {
        Call<HashMap<String, TwitterCard>> call = ogpServeApi().twitterCards(DummyTweet.CARD_SUMMARY_URL, DummyTweet.CARD_SUMMARY_LARGE_URL);

        call.enqueue(new retrofit2.Callback<HashMap<String, TwitterCard>>() {
            @Override
            public void onResponse(Call<HashMap<String, TwitterCard>> call, Response<HashMap<String, TwitterCard>> response) {
                if (response.isSuccessful()) {
                    eventBus().post(new LoadTwitterCardsEvent(sectionNumber, response.body()));
                } else {
                    // TODO: Notify error
                    Log.d("Serenade", String.format("twitterCards response error: %d", sectionNumber));
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, TwitterCard>> call, Throwable t) {
                Log.d("Serenade", "twitterCards: failure");
            }
        });
    }


    private static EventBus eventBus() {
        return EventBus.getDefault();
    }

    private static Call<List<Tweet>> callApi(int sectionNumber, Long maxId) {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        Call<List<Tweet>> call = null;

        switch (sectionNumber) {
            // TODO: Constant
            case 1:
                StatusesService statusesService = twitterApiClient.getStatusesService();
                call = statusesService.homeTimeline(ITEM_COUNT, null, maxId, false, false, false, true);
                break;
            case 2:
                FavoriteService service = twitterApiClient.getFavoriteService();
                String maxIdStr = maxId != null ? String.valueOf(maxId) : null;
                call = service.list(null, null, ITEM_COUNT, null, maxIdStr, true);
                break;
            case 3:
                // To be ...
                break;
        }
        return call;
    }

    private static OgpServeApi ogpServeApi() {
        return retrofit.create(OgpServeApi.class);
    }
}
