package com.tynmarket.serenade.model.api;

import com.tynmarket.serenade.model.entity.TwitterCard;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tynmarket on 2018/02/14.
 */

public interface OgpServeApi {
    // TODO: /twitter_cards
    @GET("/twitter")
    Observable<HashMap<String, TwitterCard>> twitterCards(
            @Query("domain") boolean domain,
            @Query("tag") String tag,
            @Query("urls[]") String... urls);
}
