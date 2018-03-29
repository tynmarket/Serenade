package com.tynmarket.serenade.model;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tynmarket on 2018/03/29.
 */

class CustomTwitterApiClient extends TwitterApiClient {
    CustomTwitterApiClient(TwitterSession session) {
        super(session);
    }

    UsersService getUsersService() {
        return getService(UsersService.class);
    }

    @SuppressWarnings("all")
    public interface UsersService {
        @GET("/1.1/users/show.json")
        Call<User> show(@Query("user_id") Long id,
                        @Query("screen_name") String screenName,
                        @Query("include_entities") Boolean includeEntities);
    }
}
