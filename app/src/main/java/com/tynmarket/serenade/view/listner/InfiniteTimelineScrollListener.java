package com.tynmarket.serenade.view.listner;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tynmarket.serenade.activity.MainActivity;
import com.tynmarket.serenade.view.adapter.TweetListAdapter;
import com.tynmarket.serenade.view.holder.TweetViewHolder;

/**
 * Created by tyn-iMarket on 2018/01/14.
 */

public class InfiniteTimelineScrollListener extends RecyclerView.OnScrollListener {
    // TODO: https://github.com/JakeWharton/RxBinding
    public static boolean mRefreshing = false;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = manager.findFirstVisibleItemPosition();
        int totalCount = recyclerView.getAdapter().getItemCount();
        int childCount = recyclerView.getChildCount();

        if (!mRefreshing && position + childCount == totalCount) {
            mRefreshing = true;
            ((TweetListAdapter) recyclerView.getAdapter()).showRefreshIndicator();
            MainActivity activity = (MainActivity) recyclerView.getContext();

            TweetViewHolder lastItem = (TweetViewHolder) recyclerView.findViewHolderForAdapterPosition(totalCount - 1);
            activity.loadPreviousTweets(lastItem.tweet.id - 1);
        }
    }
}
