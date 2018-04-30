package com.tynmarket.serenade.model.util;

import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.tynmarket.serenade.BuildConfig;
import com.tynmarket.serenade.R;

/**
 * Created by tynmarket on 2018/04/30.
 */
public class FirebaseRemoteConfigHelper {
    private static final long CACHE_EXPIRATION = 3600;

    public static void init() {
        FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        config.setConfigSettings(configSettings);
        config.setDefaults(R.xml.remote_config_defaults);

        fetch();
    }

    public static FirebaseRemoteConfig fetch() {
        FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();

        long cacheExpiration = CACHE_EXPIRATION;
        if (config.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        config.fetch(cacheExpiration).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                config.activateFetched();
            } else {
                Log.d("Serenade", "RemoteConfig init failed");
            }
        });

        return config;
    }
}
