package com.tynmarket.serenade.model;

import android.util.Log;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;
import com.tynmarket.serenade.event.LoadUserEvent;
import com.tynmarket.serenade.model.util.DummyUser;
import com.tynmarket.serenade.model.util.RetrofitObserver;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;

/**
 * Created by tynmarket on 2018/03/03.
 */

public class LoginUser {
    private User user;

    public static boolean signedIn() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        return session != null;
    }

    public static void loadUser() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        CustomTwitterApiClient client = new CustomTwitterApiClient(session);
        long userId = session.getUserId();

        Call<User> call = client.getUsersService().show(userId, null, false);
        RetrofitObserver
                .create(call)
                .subscribe(user -> {
                    Log.d("Serenade", String.format("loadUser success"));
                    EventBus.getDefault().post(new LoadUserEvent(user));
                }, throwable -> {
                    Log.d("Serenade", String.format("loadUser failure"));
                    // TODO: Dummy or empty user
                    EventBus.getDefault().post(new LoadUserEvent(DummyUser.tynmarket()));
                });
    }

    public static void signOut() {
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
    }
}
