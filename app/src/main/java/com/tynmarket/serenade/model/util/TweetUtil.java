package com.tynmarket.serenade.model.util;

import android.support.annotation.Nullable;
import android.util.Log;

import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.UrlEntity;

import java.util.List;

/**
 * Created by tyn-iMarket on 2018/01/22.
 */

public class TweetUtil {
    @Nullable
    public static String photoUrl(Tweet tweet) {
        TweetEntities entities = tweet.entities;
        if (entities == null) {
            return null;
        }

        List<MediaEntity> mediaList = entities.media;
        if (mediaList.size() == 0) {
            return null;
        }

        // TODO: stream function (API 24)
        MediaEntity entity = null;
        for (MediaEntity m : mediaList) {
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

    public static void debugTimeline(List<Tweet> tweets) {
        for (int i = 0; i < tweets.size(); i++) {
            Tweet tweet = tweets.get(i);
            Tweet quotedStatus = tweet.quotedStatus;
            TweetEntities entities = tweet.entities;
            List<UrlEntity> urls;

            String photoUrl = photoUrl(tweet);
            String quotedPhotoUrl;

            Log.d("Serenade", String.format("timelime: %d", i));
            Log.d("Serenade", tweet.user.name);
            Log.d("Serenade", tweet.text);

            if (photoUrl != null) {
                Log.d("Serenade", photoUrl);
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
                Log.d("Serenade", quotedStatus.user.name);
                Log.d("Serenade", quotedStatus.text);

                quotedPhotoUrl = TweetUtil.photoUrl(quotedStatus);
                if (quotedPhotoUrl != null) {
                    Log.d("Serenade", quotedPhotoUrl);
                }
            }
        }
    }
}
