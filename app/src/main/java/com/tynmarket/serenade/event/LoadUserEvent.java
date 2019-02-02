package com.tynmarket.serenade.event;

import com.tynmarket.serenade.core.models.User;

/**
 * Created by tynmarket on 2018/03/03.
 */

public class LoadUserEvent {
    public User user;

    public LoadUserEvent(User user) {
        this.user = user;
    }
}
