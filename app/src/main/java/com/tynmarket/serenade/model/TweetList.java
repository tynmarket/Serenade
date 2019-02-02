package com.tynmarket.serenade.model;

import android.annotation.SuppressLint;

import com.tynmarket.serenade.core.TwitterApiClient;
import com.tynmarket.serenade.core.TwitterCore;
import com.tynmarket.serenade.core.models.Tweet;
import com.tynmarket.serenade.core.services.FavoriteService;
import com.tynmarket.serenade.core.services.StatusesService;
import com.tynmarket.serenade.BuildConfig;
import com.tynmarket.serenade.event.LoadFailureTweetListEvent;
import com.tynmarket.serenade.event.LoadTweetListEvent;
import com.tynmarket.serenade.event.StartLoadTweetListEvent;
import com.tynmarket.serenade.model.util.LogUtil;
import com.tynmarket.serenade.model.util.RetrofitObserver;
import com.tynmarket.serenade.model.util.TweetUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;

/**
 * Created by tynmarket on 2018/02/01.
 */

public class TweetList {
    private static final int ITEM_COUNT = 50;

    // TODO: Caching
    @SuppressLint("DefaultLocale")
    public static void loadTweets(int sectionNumber, boolean refresh, Long maxId) {
        // TODO: Progress bar displayed infinitely?
        EventBus.getDefault().post(new StartLoadTweetListEvent(sectionNumber));
        Call<List<Tweet>> call = callApi(sectionNumber, maxId);

        RetrofitObserver
                .create(call)
                .subscribe(tweets -> {
                    LogUtil.d(String.format("loadTweets success: %d", sectionNumber));
                    if (BuildConfig.DEBUG) {
                        TweetUtil.debugTimeline(tweets);
                    }

                    // Request Twitter Cards to ogpserve
                    TwitterCardList.loadTwitterCards(sectionNumber, tweets);

                    eventBus().post(new LoadTweetListEvent(sectionNumber, tweets, refresh));
                }, throwable -> {
                    // TODO: Late limit(Status 429)
                    LogUtil.e(String.format("loadTweets failure: %d", sectionNumber), throwable);

                    eventBus().post(new LoadFailureTweetListEvent(sectionNumber, throwable));
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
