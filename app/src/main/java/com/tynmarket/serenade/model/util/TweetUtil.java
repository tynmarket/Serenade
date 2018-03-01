package com.tynmarket.serenade.model.util;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.UrlEntity;
import com.tynmarket.serenade.model.SpeakerDeck;

import java.util.List;

/**
 * Created by tyn-iMarket on 2018/01/22.
 */

public class TweetUtil {
    private static final String HOST_SPEAKERDECK = "https://speakerdeck.com";

    @Nullable
    public static String photoUrl(Tweet tweet) {
        List<MediaEntity> media = tweet.entities.media;
        if (media.size() == 0) {
            return null;
        }

        // TODO: stream function (API 24)
        MediaEntity entity = null;
        for (MediaEntity m : media) {
            if (m.type.equals("photo")) {
                entity = m;
                break;
            }
        }

        if (entity != null) {
            return entity.mediaUrlHttps;
        } else {
            return null;
        }
    }

    public static boolean containSlide(Tweet tweet) {
        UrlEntity url = urlEntity(tweet);
        if (url != null) {
            return url.expandedUrl.startsWith(SpeakerDeck.SPEAKER_DECK_URL);
        } else {
            return false;
        }
    }

    @Nullable
    public static String expandedUrl(Tweet tweet) {
        UrlEntity url = urlEntity(tweet);
        if (url != null) {
            return url.expandedUrl;
        } else {
            return null;
        }
    }

    @Nullable
    public static String expandedUrlHost(Tweet tweet) {
        String expandedUrl = expandedUrl(tweet);
        if (expandedUrl != null) {
            Uri uri = Uri.parse(expandedUrl);
            return uri.getHost();
        } else {
            return null;
        }
    }

    public static UrlEntity urlEntity(Tweet tweet) {
        List<UrlEntity> urls = tweet.entities.urls;
        if (urls.size() > 0) {
            return urls.get(0);
        } else {
            return null;
        }
    }

    public static void debugTimeline(List<Tweet> tweets) {
        for (int i = 0; i < tweets.size(); i++) {
            Tweet tweet = tweets.get(i);
            Tweet quotedStatus = tweet.quotedStatus;
            TweetEntities entities = tweet.entities;
            List<UrlEntity> urls;

            String photoUrl = photoUrl(tweet);
            String quotedPhotoUrl;

            Log.d("Serenade", String.format("timeline: %d", i));
            Log.d("Serenade", String.format("name: %s", tweet.user.name));
            Log.d("Serenade", String.format("text: %s", tweet.text));

            if (photoUrl != null) {
                Log.d("Serenade", String.format("photoUrl: %s", photoUrl));
            }

            if (entities != null) {
                urls = entities.urls;

                if (urls != null) {
                    for (UrlEntity url : urls) {
                        Log.d("Serenade", String.format("url: %s", url.url));
                        Log.d("Serenade", String.format("expandedUrl: %s", url.expandedUrl));
                        Log.d("Serenade", String.format("displayUrl: %s", url.displayUrl));
                    }
                }
            }

            if (quotedStatus != null) {
                Log.d("Serenade", String.format("quoted status: %d", i));
                Log.d("Serenade", String.format("name: %s", quotedStatus.user.name));
                Log.d("Serenade", String.format("text: %s", quotedStatus.text));

                quotedPhotoUrl = TweetUtil.photoUrl(quotedStatus);
                if (quotedPhotoUrl != null) {
                    Log.d("Serenade", String.format("photoUrl: %s", quotedPhotoUrl));
                }
            }
            Log.d("Serenade", "");
        }
    }
}
