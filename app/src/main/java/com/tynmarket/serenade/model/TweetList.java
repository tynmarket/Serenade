package com.tynmarket.serenade.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.FavoriteService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.tynmarket.serenade.event.LoadFailureTweetListEvent;
import com.tynmarket.serenade.event.LoadTweetListEvent;
import com.tynmarket.serenade.event.LoadTwitterCardsEvent;
import com.tynmarket.serenade.event.StartLoadTweetListEvent;
import com.tynmarket.serenade.model.util.DummyTweet;
import com.tynmarket.serenade.model.util.TweetUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;

/**
 * Created by tyn-iMarket on 2018/02/01.
 */

public class TweetList {
    private static final int ITEM_COUNT = 50;

    // TODO: Caching
    public static void loadTweets(int sectionNumber, boolean refresh, Long maxId) {
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
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                eventBus().post(new LoadTwitterCardsEvent(sectionNumber, DummyTweet.twitterCards()));
            }
        }.execute();
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
}
