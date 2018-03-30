package com.tynmarket.serenade.model.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by tynmarket on 2018/03/30.
 */

public class ActivityHelper {
    private static final int REQUEST_CODE_NO_OP = 1000;

    public static void startUriActivity(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivityForResult(intent, REQUEST_CODE_NO_OP);
            activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        } else {
            context.startActivity(intent);
        }
    }
}
