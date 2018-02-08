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

    private static final Retrofit retrofit = new Retrofit.Builder().baseUrl(SPEAKER_DECK_URL).build();

    private static final Pattern p = Pattern.compile("<meta property=\"og:image\" content=\"(.+)slide_0.jpg\" />");

    public static SpeakerDeckApi getApiClient() {
        return retrofit.create(SpeakerDeckApi.class);
    }

    public static String[] slideHtmlParams(String expandedUrl) {
        String[] strings = expandedUrl.split("/");
        int size = strings.length;
        String[] params = new String[2];
        params[0] = strings[size - 2];
        params[1] = strings[size - 1];

        return params;
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
