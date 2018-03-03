package com.tynmarket.serenade.model;

import android.util.Log;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;
import com.tynmarket.serenade.event.LoadUserEvent;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;

/**
 * Created by tynmarket on 2018/03/03.
 */

public class LoginUser {
    private static LoginUser loginUser;

    private User user;

    public static void loadUser() {
        if (loginUser == null) {
            TwitterApiClient client = TwitterCore.getInstance().getApiClient();
            AccountService service = client.getAccountService();
            Call<User> call = service.verifyCredentials(false, true, false);

            call.enqueue(new Callback<User>() {
                @Override
                public void success(Result<User> result) {
                    Log.d("Serenade", "verifyCredentials success");
                    EventBus.getDefault().post(new LoadUserEvent(result.data));
                }

                @Override
                public void failure(TwitterException exception) {
                    Log.d("Serenade", "verifyCredentials failure");
                }
            });
        }
    }
}
