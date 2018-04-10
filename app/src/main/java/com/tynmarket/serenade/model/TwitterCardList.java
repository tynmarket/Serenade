package com.tynmarket.serenade.model;

import android.annotation.SuppressLint;
import android.util.Log;

import com.twitter.sdk.android.core.models.Tweet;
import com.tynmarket.serenade.BuildConfig;
import com.tynmarket.serenade.event.LoadTwitterCardsEvent;
import com.tynmarket.serenade.model.api.OgpServeApi;
import com.tynmarket.serenade.model.entity.TwitterCard;
import com.tynmarket.serenade.model.util.DisposableHelper;
import com.tynmarket.serenade.model.util.DummyTweet;
import com.tynmarket.serenade.model.util.TweetUtil;
import com.tynmarket.serenade.model.util.TwitterCardUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tynmarket on 2018/03/31.
 */

public class TwitterCardList {
    private static final String OGPSERVE_URL = BuildConfig.OGPSERVE_URL;
    private static final Retrofit retrofit = new Retrofit
            .Builder()
            .baseUrl(OGPSERVE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @SuppressLint("StaticFieldLeak")
    public static void loadTwitterCards(int sectionNumber) {
        String[] urls = {DummyTweet.CARD_SUMMARY_URL, DummyTweet.CARD_SUMMARY_LARGE_IMAGE_URL};
        loadTwitterCards(sectionNumber, urls);
    }

    static void loadTwitterCards(int sectionNumber, List<Tweet> tweets) {
        List<String> urls = urlsFromTweets(tweets);
        loadTwitterCards(sectionNumber, urls.toArray(new String[urls.size()]));
    }

    private static void loadTwitterCards(int sectionNumber, String[] urls) {
        boolean domainEnabled = domainEnabled();

        Disposable disposable = ogpServeApi()
                .twitterCards(domainEnabled, urls)
                .subscribeOn(Schedulers.io())
                .subscribe(cards -> {
                    // Show Summary with large image
                    if (domainEnabled) {
                        setShowLargeImage(cards.values());
                    }

                    if (BuildConfig.DEBUG) {
                        TwitterCardUtil.debugCards(cards);
                    }

                    eventBus().post(new LoadTwitterCardsEvent(sectionNumber, cards));
                }, throwable -> {
                    // TODO: Notify error
                    Log.e("Serenade", "loadTwitterCards: error", throwable);
                });

        DisposableHelper.add(disposable, sectionNumber);
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

    private static boolean domainEnabled() {
        return Throttle.requestToTop();
    }

    private static void setShowLargeImage(Collection<TwitterCard> cards) {
        for (TwitterCard card : cards) {
            card.showLargeImage = true;
        }
    }

    private static OgpServeApi ogpServeApi() {
        return retrofit.create(OgpServeApi.class);
    }

    private static EventBus eventBus() {
        return EventBus.getDefault();
    }
}
