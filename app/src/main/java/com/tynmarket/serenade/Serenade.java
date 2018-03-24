package com.tynmarket.serenade;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.tynmarket.serenade.model.util.Resource;

/**
 * Created by tynmarket on 2018/03/17.
 */

public class Serenade extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Resource.setResources(getResources());
        AndroidThreeTen.init(this);
    }
}
