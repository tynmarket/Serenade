package com.tynmarket.serenade.model;

import android.annotation.SuppressLint;
import android.util.Log;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.FavoriteService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.tynmarket.serenade.BuildConfig;
import com.tynmarket.serenade.api.OgpServeApi;
import com.tynmarket.serenade.event.LoadFailureTweetListEvent;
import com.tynmarket.serenade.event.LoadTweetListEvent;
import com.tynmarket.serenade.event.LoadTwitterCardsEvent;
import com.tynmarket.serenade.event.StartLoadTweetListEvent;
import com.tynmarket.serenade.model.util.DummyTweet;
import com.tynmarket.serenade.model.util.TweetUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tyn-iMarket on 2018/02/01.
 */

public class TweetList {
    private static final int ITEM_COUNT = 50;
    private static final String OGPSERVE_URL = BuildConfig.OGPSERVE_URL;

    private static final Retrofit retrofit = new Retrofit
            .Builder()
            .baseUrl(OGPSERVE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // TODO: Caching
    public static void loadTweets(int sectionNumber, boolean refresh, Long maxId) {
        // TODO: Progress bar displayed infinitely?
        EventBus.getDefault().post(new StartLoadTweetListEvent(sectionNumber));
        Call<List<Tweet>> call = callApi(sectionNumber, maxId);

        RetrofitObserver
                .create(call)
                .subscribe(tweets -> {
                    Log.d("Serenade", String.format("loadTweets success: %d", sectionNumber));
                    TweetUtil.debugTimeline(tweets);

                    // Request Twitter Cards to ogpserve
                    loadTwitterCards(sectionNumber, tweets);

                    eventBus().post(new LoadTweetListEvent(sectionNumber, tweets, refresh));
                }, throwable -> {
                    // TODO: Late limit(Status 429)
                    Log.d("Serenade", String.format("loadTweets failure: %d", sectionNumber));

                    eventBus().post(new LoadFailureTweetListEvent(sectionNumber));
                });
    }

    @SuppressLint("StaticFieldLeak")
    public static void loadTwitterCards(int sectionNumber) {
        String[] urls = {DummyTweet.CARD_SUMMARY_URL, DummyTweet.CARD_SUMMARY_LARGE_IMAGE_URL};
        loadTwitterCards(sectionNumber, urls);
    }

    private static void loadTwitterCards(int sectionNumber, List<Tweet> tweets) {
        List<String> urls = urlsFromTweets(tweets);
        loadTwitterCards(sectionNumber, urls.toArray(new String[urls.size()]));
    }

    private static void loadTwitterCards(int sectionNumber, String[] urls) {
        ogpServeApi()
                .twitterCards(urls)
                .subscribeOn(Schedulers.io())
                .subscribe(cards -> {
                    eventBus().post(new LoadTwitterCardsEvent(sectionNumber, cards));
                }, throwable -> {
                    // TODO: Notify error
                    Log.e("Serenade", "loadTwitterCards: error", throwable);
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

    private static ArrayList<String> urlsFromTweets(List<Tweet> tweets) {
        ArrayList<String> urls = new ArrayList<>();

        for (Tweet tweet : tweets) {
            String expandedUrl = TweetUtil.expandedUrl(tweet);
            if (expandedUrl != null) {
                urls.add(expandedUrl);
            }
        }
        return urls;
    }
}
