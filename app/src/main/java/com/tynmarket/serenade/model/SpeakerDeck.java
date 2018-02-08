package com.tynmarket.serenade.model;

import android.support.annotation.Nullable;

import com.tynmarket.serenade.api.SpeakerDeckApi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Retrofit;

/**
 * Created by tynmarket on 2018/02/06.
 */

public class SpeakerDeck {
    public static final String SPEAKER_DECK_URL = "https://speakerdeck.com/";
    private static final Pattern p = Pattern.compile("<meta property=\"og:image\" content=\"(.+)slide_0.jpg\" />");

    private static Retrofit retrofit;

    public static SpeakerDeckApi getApiClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(SPEAKER_DECK_URL)
                    .build();
        }
        return retrofit.create(SpeakerDeckApi.class);
    }

    @Nullable
    public static String slideUrlFromHtml(String html) {
        Matcher m = p.matcher(html);
        if (m.find()) {
            return m.group(1);
        } else {
            return null;
        }
    }
}
