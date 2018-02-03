package com.tynmarket.serenade.event;

/**
 * Created by tynmarket on 2018/02/03.
 */

public class StartLoadTweetListEvent {
    public final int sectionNumber;

    public StartLoadTweetListEvent(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }
}
