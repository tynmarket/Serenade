package com.tynmarket.serenade.model.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.tynmarket.serenade.R;

/**
 * Created by tynmarket on 2018/03/30.
 */

public class ActivityHelper {
    private static final int REQUEST_CODE_NO_OP = 1000;

    public static void startActivity(Context context, Intent intent) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            startActivityForResult(activity, intent, REQUEST_CODE_NO_OP);
        } else {
            context.startActivity(intent);
        }
    }

    public static void startUriActivity(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        // TODO: Set only if url is twitter
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(context, intent);
    }

    public static void startUriActivityForResult(Context context, Uri uri, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            // TODO: Set only if url is twitter
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(activity, intent, requestCode);
        } else {
            context.startActivity(intent);
        }
    }

    private static void startActivityForResult(Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out);
    }
}
