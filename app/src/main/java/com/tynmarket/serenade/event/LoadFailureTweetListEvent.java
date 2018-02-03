package com.tynmarket.serenade.event;

/**
 * Created by tynmarket on 2018/02/03.
 */

public class LoadFailureTweetListEvent {
    public final int sectionNumber;

    public LoadFailureTweetListEvent(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }
}
