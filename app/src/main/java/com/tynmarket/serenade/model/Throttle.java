package com.tynmarket.serenade.model;

import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.Random;

/**
 * Created by tynmarket on 2018/04/07.
 */
public class Throttle {
    private static final String KEY_REQUEST_TO_TOP_RATIO = "request_to_top_ratio";

    public static boolean requestToTop() {
        FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
        long requestToTopRatio = config.getLong(KEY_REQUEST_TO_TOP_RATIO);
        Log.d("Serenade", String.format("requestToTopRatio: %d", requestToTopRatio));

        Random rand = new Random();
        return rand.nextInt(100) < requestToTopRatio;
    }
}
