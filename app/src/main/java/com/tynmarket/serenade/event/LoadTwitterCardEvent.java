package com.tynmarket.serenade.event;

import com.tynmarket.serenade.model.entity.TwitterCard;

/**
 * Created by tynmarket on 2018/02/10.
 */

public class LoadTwitterCardEvent {
    public final int sectionNumber;
    public final int position;
    public final long tweetId;
    public final TwitterCard card;

    public LoadTwitterCardEvent(int sectionNumber, int position, long tweetId, TwitterCard card) {
        this.sectionNumber = sectionNumber;
        this.position = position;
        this.tweetId = tweetId;
        this.card = card;
    }
}
