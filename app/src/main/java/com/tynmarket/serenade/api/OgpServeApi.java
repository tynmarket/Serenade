package com.tynmarket.serenade.api;

import com.tynmarket.serenade.model.TwitterCard;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tynmarket on 2018/02/14.
 */

public interface OgpServeApi {
    // TODO: /twitter_cards
    @GET("/")
    Call<Map<String, TwitterCard>> twitterCards(@Query("urls[]") String... urls);
}
