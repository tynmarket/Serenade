package com.tynmarket.serenade.model;

import com.tynmarket.serenade.api.SpeakerDeckApi;

import retrofit2.Retrofit;

/**
 * Created by tynmarket on 2018/02/06.
 */

public class SpeakerDeck {
    private static final String SPEAKER_DECK_URL = "https://speakerdeck.com/";
    private static Retrofit retrofit;

    public static SpeakerDeckApi getApiClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(SPEAKER_DECK_URL)
                    .build();
        }
        return retrofit.create(SpeakerDeckApi.class);
    }
}
