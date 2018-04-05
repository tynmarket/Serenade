package com.tynmarket.serenade.model;

import android.util.Log;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.FavoriteService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.tynmarket.serenade.event.LoadFailureTweetListEvent;
import com.tynmarket.serenade.event.LoadTweetListEvent;
import com.tynmarket.serenade.event.StartLoadTweetListEvent;
import com.tynmarket.serenade.model.dao.TweetDao;
import com.tynmarket.serenade.model.util.RetrofitObserver;
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
        // TODO: Progress bar displayed infinitely?
        EventBus.getDefault().post(new StartLoadTweetListEvent(sectionNumber));
        Call<List<Tweet>> call = callApi(sectionNumber, maxId);

        RetrofitObserver
                .create(call)
                .subscribe(tweets -> {
                    Log.d("Serenade", String.format("loadTweets success: %d", sectionNumber));
                    // TODO: Only when debug
                    TweetUtil.debugTimeline(tweets);

                    // Replace tweets in DB
                    TweetDao.replaceTweets(tweets, sectionNumber);

                    // Request Twitter Cards to ogpserve
                    TwitterCardList.loadTwitterCards(sectionNumber, tweets);

                    eventBus().post(new LoadTweetListEvent(sectionNumber, tweets, refresh));
                }, throwable -> {
                    // TODO: Late limit(Status 429)
                    Log.d("Serenade", String.format("loadTweets failure: %d", sectionNumber));

                    eventBus().post(new LoadFailureTweetListEvent(sectionNumber));
                });
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

    private static EventBus eventBus() {
        return EventBus.getDefault();
    }
}
