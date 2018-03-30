package com.tynmarket.serenade.model.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by tynmarket on 2018/03/30.
 */

public class ActivityHelper {
    public static void startUriActivity(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // TODO: Transition
        // https://developer.android.com/reference/android/app/Activity.html#overridePendingTransition(int, int)
        context.startActivity(intent);
    }
}
