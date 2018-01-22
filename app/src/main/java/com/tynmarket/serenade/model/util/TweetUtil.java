package com.tynmarket.serenade.model.util;

import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetEntities;

import java.util.List;

/**
 * Created by tyn-iMarket on 2018/01/22.
 */

public class TweetUtil {
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
}
