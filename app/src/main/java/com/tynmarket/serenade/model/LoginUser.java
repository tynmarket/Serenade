package com.tynmarket.serenade.model;

import com.twitter.sdk.android.core.models.User;
import com.tynmarket.serenade.event.LoadUserEvent;
import com.tynmarket.serenade.model.util.DummyUser;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by tynmarket on 2018/03/03.
 */

public class LoginUser {
    private static LoginUser loginUser;

    private User user;

    public static void loadUser() {
        if (loginUser == null) {
            User user = DummyUser.tynmarket();
            EventBus.getDefault().post(new LoadUserEvent(user));
            /*
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
            */
        }
    }
}
