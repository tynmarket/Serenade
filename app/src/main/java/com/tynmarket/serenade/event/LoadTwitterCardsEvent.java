package com.tynmarket.serenade.event;

import android.support.v4.util.LongSparseArray;

import com.tynmarket.serenade.model.TwitterCard;

/**
 * Created by tynmarket on 2018/02/10.
 */

public class LoadTwitterCardsEvent {
    public final int sectionNumber;
    public final LongSparseArray<TwitterCard> cards;

    public LoadTwitterCardsEvent(int sectionNumber, LongSparseArray<TwitterCard> cards) {
        this.sectionNumber = sectionNumber;
        this.cards = cards;
    }
}
