package com.tynmarket.serenade.model.util;

import android.util.Log;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Logger;
import com.tynmarket.serenade.BuildConfig;

/**
 * Created by tynmarket on 2018/05/02.
 */
public class LogUtil {
    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d("Serenade", msg);
        }
    }

    public static void e(String msg, Throwable e) {
        if (BuildConfig.DEBUG) {
            Log.e("Serenade", msg, e);
        }
    }

    public static Logger twitterLogger() {
        if (BuildConfig.DEBUG) {
            return new DefaultLogger(Log.DEBUG);
        } else {
            return new DefaultLogger(Log.ERROR);
        }
    }
}
