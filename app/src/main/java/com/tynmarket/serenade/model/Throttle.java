package com.tynmarket.serenade.model;

import android.annotation.SuppressLint;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.tynmarket.serenade.model.util.FirebaseRemoteConfigHelper;
import com.tynmarket.serenade.model.util.LogUtil;

import java.util.Random;

/**
 * Created by tynmarket on 2018/04/07.
 */
public class Throttle {
    private static final String KEY_REQUEST_TO_TOP_RATIO = "request_to_top_ratio";

    @SuppressLint("DefaultLocale")
    public static boolean requestToTop() {
        FirebaseRemoteConfig config = FirebaseRemoteConfigHelper.fetch();
        long requestToTopRatio = config.getLong(KEY_REQUEST_TO_TOP_RATIO);

        LogUtil.d(String.format("requestToTopRatio: %d", requestToTopRatio));

        Random rand = new Random();
        return rand.nextInt(100) < requestToTopRatio;
    }
}
