package com.tynmarket.serenade.model.util;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;

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

            // TODO: Not responding after return
            Fade fadeExit = new Fade(Fade.OUT);
            fadeExit.setDuration(android.R.integer.config_shortAnimTime);
            Slide slide = new Slide(Gravity.RIGHT);
            slide.setDuration(android.R.integer.config_shortAnimTime);
            Fade fadeReturn = new Fade(Fade.OUT);

            activity.getWindow().setEnterTransition(slide);
            activity.getWindow().setExitTransition(fadeExit);
            activity.getWindow().setReturnTransition(fadeReturn);
            activity.getWindow().setReenterTransition(null);

            ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(activity);
            
            activity.startActivityForResult(intent, REQUEST_CODE_NO_OP, option.toBundle());

            //activity.startActivityForResult(intent, REQUEST_CODE_NO_OP);
            //activity.overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out);
        } else {
            context.startActivity(intent);
        }
    }
}
