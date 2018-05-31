package com.tynmarket.serenade.event;

/**
 * Created by tynmarket on 2018/02/03.
 */

public class LoadFailureTweetListEvent {
    public final int sectionNumber;
    public final Throwable throwable;

    public LoadFailureTweetListEvent(int sectionNumber, Throwable throwable) {
        this.sectionNumber = sectionNumber;
        this.throwable = throwable;
    }
}
