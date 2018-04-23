package com.tynmarket.serenade.model;

import android.annotation.SuppressLint;
import android.util.Log;

import com.twitter.sdk.android.core.models.Tweet;
import com.tynmarket.serenade.BuildConfig;
import com.tynmarket.serenade.event.LoadTwitterCardEvent;
import com.tynmarket.serenade.event.LoadTwitterCardsEvent;
import com.tynmarket.serenade.model.api.OgpServeApi;
import com.tynmarket.serenade.model.entity.TwitterCard;
import com.tynmarket.serenade.model.util.DisposableHelper;
import com.tynmarket.serenade.model.util.DummyTweet;
import com.tynmarket.serenade.model.util.TweetUtil;
import com.tynmarket.serenade.model.util.TwitterCardUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
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
    private static final String TAG_TIMELINE = "timeline";
    private static final String TAG_FAVORITE = "favorite";
    private static final String TAG_CACHE = "cache";
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

    public static void loadTwitterCards(int sectionNumber, List<Tweet> tweets) {
        List<String> urls = urlsFromTweets(tweets);
        loadTwitterCards(sectionNumber, urls.toArray(new String[urls.size()]));
    }

    public static void loadTwitterCard(int sectionNumber, int position, Tweet tweet) {
        boolean requestToTop = requestToTop();
        String url = TweetUtil.expandedUrlWithoutTwitter(tweet);

        Disposable disposable = ogpServeApi()
                .twitterCards(requestToTop, true, TAG_CACHE, url)
                .subscribeOn(Schedulers.io())
                .subscribe(cards -> {
                    TwitterCard card = cards.get(url);

                    if (BuildConfig.DEBUG) {
                        TwitterCardUtil.debugCard(card);
                    }

                    if (card != null) {
                        card.url = url;
                    }

                    eventBus().post(new LoadTwitterCardEvent(sectionNumber, position, tweet.id, card));
                }, throwable -> {
                    // TODO: Notify error
                    Log.e("Serenade", "loadTwitterCard: error", throwable);
                });

        DisposableHelper.add(disposable, sectionNumber);
    }

    private static void loadTwitterCards(int sectionNumber, String[] urls) {
        boolean requestToTop = requestToTop();
        String tag = getTag(sectionNumber);

        Disposable disposable = ogpServeApi()
                .twitterCards(requestToTop, false, tag, urls)
                .subscribeOn(Schedulers.io())
                .subscribe(cards -> {
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
            String expandedUrl = TweetUtil.expandedUrlWithoutTwitter(tweet);
            if (expandedUrl != null) {
                urls.add(expandedUrl);
            }
        }
        return urls;
    }

    private static boolean requestToTop() {
        return Throttle.requestToTop();
    }

    private static String getTag(int sectionNumber) {
        switch (sectionNumber) {
            case 1:
                return TAG_TIMELINE;
            case 2:
                return TAG_FAVORITE;
            default:
                return "";
        }
    }

    private static OgpServeApi ogpServeApi() {
        return retrofit.create(OgpServeApi.class);
    }

    private static EventBus eventBus() {
        return EventBus.getDefault();
    }
}
