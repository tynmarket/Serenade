package com.tynmarket.serenade.view.listner;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.twitter.sdk.android.core.models.Tweet;
import com.tynmarket.serenade.model.TweetList;
import com.tynmarket.serenade.model.TwitterCardList;
import com.tynmarket.serenade.view.adapter.TweetListAdapter;
import com.tynmarket.serenade.view.holder.TweetViewHolder;

/**
 * Created by tyn-iMarket on 2018/01/14.
 */

public class InfiniteTimelineScrollListener extends RecyclerView.OnScrollListener {
    public boolean mRefreshing = false;

    private final int sectionNumber;
    private int lastPosition;

    public InfiniteTimelineScrollListener(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = manager.findFirstVisibleItemPosition();
        int totalCount = recyclerView.getAdapter().getItemCount();
        int childCount = recyclerView.getChildCount();
        boolean cardsLoaded = ((TweetListAdapter) recyclerView.getAdapter()).cardsLoaded();
        int lastPosition = manager.findLastVisibleItemPosition();

        // TODO: Not load if last tweet is loaded
        if (!mRefreshing && position + childCount == totalCount) {
            mRefreshing = true;

            TweetViewHolder lastItem = (TweetViewHolder) recyclerView.findViewHolderForAdapterPosition(totalCount - 1);
            long maxId = lastItem.getTweet().id - 1;
            TweetList.loadTweets(sectionNumber, false, maxId);
        }

        if (cardsLoaded && !mRefreshing && lastPosition != this.lastPosition) {
            this.lastPosition = lastPosition;
            int loadPosition = lastPosition + 1;
            TweetListAdapter adapter = (TweetListAdapter) recyclerView.getAdapter();

            if (adapter.requestCardCache(loadPosition)) {
                Tweet tweet = adapter.getTweet(loadPosition);
                TwitterCardList.loadTwitterCard(sectionNumber, loadPosition, tweet);
            }
        }
    }
}
