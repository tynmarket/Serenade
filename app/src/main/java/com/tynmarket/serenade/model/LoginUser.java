package com.tynmarket.serenade.model;

import com.tynmarket.serenade.core.TwitterCore;
import com.tynmarket.serenade.core.TwitterSession;
import com.tynmarket.serenade.core.models.User;
import com.tynmarket.serenade.event.LoadUserEvent;
import com.tynmarket.serenade.event.SignOutEvent;
import com.tynmarket.serenade.model.util.DummyUser;
import com.tynmarket.serenade.model.util.LogUtil;
import com.tynmarket.serenade.model.util.RetrofitObserver;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;

/**
 * Created by tynmarket on 2018/03/03.
 */

public class LoginUser {
    private static final long TYNMARKET_USER_ID = 35427589;

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
                    EventBus.getDefault().post(new LoadUserEvent(user));
                }, throwable -> {
                    LogUtil.e("loadUser failure", throwable);
                    // TODO: Dummy or empty user
                    EventBus.getDefault().post(new LoadUserEvent(DummyUser.tynmarket()));
                });
    }

    public static void signOut() {
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
        EventBus.getDefault().post(new SignOutEvent());
    }

    public static boolean isTynmarket() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        return session != null && session.getUserId() == TYNMARKET_USER_ID;
    }
}
