package com.tynmarket.serenade.event;

/**
 * Created by tyn-iMarket on 2018/01/29.
 */

public class LoadHomeTimelineEvent {
    public boolean refresh;
    public Long maxId;

    public LoadHomeTimelineEvent(boolean refresh, Long maxId) {
        this.refresh = refresh;
        this.maxId = maxId;
    }
}
