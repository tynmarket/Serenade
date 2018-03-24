package com.tynmarket.serenade.model.util;

import android.content.res.Resources;

/**
 * Created by tynmarket on 2018/03/24.
 */

public class Resource {
    private static Resources resources;

    public static Resources getResources() {
        return resources;
    }

    public static void setResources(Resources resources) {
        Resource.resources = resources;
    }
}
