package com.tynmarket.serenade.event;

/**
 * Created by tyn-iMarket on 2018/01/29.
 */

public class LoadFavoritesListEvent {
    public boolean refresh;
    public String maxIdStr;

    public LoadFavoritesListEvent(boolean refresh, String maxIdStr) {
        this.refresh = refresh;
        this.maxIdStr = maxIdStr;
    }
}
