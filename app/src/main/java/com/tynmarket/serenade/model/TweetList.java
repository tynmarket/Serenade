package com.tynmarket.serenade.model;

import com.tynmarket.serenade.event.LoadFavoriteListEvent;
import com.tynmarket.serenade.event.LoadHomeTimelineEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by tyn-iMarket on 2018/02/01.
 */

public class TweetList {
    public static void loadTweets(int sectionNumber, boolean refresh, Long maxId) {
        switch (sectionNumber) {
            case 1:
                EventBus.getDefault().post(new LoadHomeTimelineEvent(refresh, maxId));
                break;
            case 2:
                String maxIdStr = maxId != null ? String.valueOf(maxId) : null;
                EventBus.getDefault().post(new LoadFavoriteListEvent(refresh, maxIdStr));
                break;
            case 3:
                // To be ...
                break;
        }
    }
}
