package com.tynmarket.serenade.event;

import com.tynmarket.serenade.model.TwitterCard;

import java.util.HashMap;

/**
 * Created by tynmarket on 2018/02/10.
 */

public class LoadTwitterCardsEvent {
    public final int sectionNumber;
    public final HashMap<String, TwitterCard> cards;

    public LoadTwitterCardsEvent(int sectionNumber, HashMap<String, TwitterCard> cards) {
        this.sectionNumber = sectionNumber;
        this.cards = cards;
    }
}
