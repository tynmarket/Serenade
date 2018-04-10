package com.tynmarket.serenade.model.util;

import android.util.Log;

import com.tynmarket.serenade.model.entity.TwitterCard;

import java.util.Map;

/**
 * Created by tynmarket on 2018/04/10.
 */
public class TwitterCardUtil {
    public static void debugCards(Map<String, TwitterCard> cards) {
        for (String url : cards.keySet()) {
            TwitterCard card = cards.get(url);
            Log.d("Serenade", String.format("url: %s", url));
            Log.d("Serenade", String.format("card: %s", card.card));
            Log.d("Serenade", String.format("title: %s", card.title));
            Log.d("Serenade", String.format("image: %s", card.image));
        }
    }
}
