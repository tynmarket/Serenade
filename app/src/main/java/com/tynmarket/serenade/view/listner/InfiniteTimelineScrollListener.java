package com.tynmarket.serenade.view.listner;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.twitter.sdk.android.core.models.Tweet;
import com.tynmarket.serenade.model.TweetList;
import com.tynmarket.serenade.model.TwitterCardList;
import com.tynmarket.serenade.model.util.FirebaseAnalyticsHelper;
import com.tynmarket.serenade.model.util.LogUtil;
import com.tynmarket.serenade.view.adapter.TweetListAdapter;
import com.tynmarket.serenade.view.holder.TweetViewHolder;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;

/**
 * Created by tynmarket on 2018/01/14.
 */

public class InfiniteTimelineScrollListener extends RecyclerView.OnScrollListener {
    private static final int REFRESH_INTERVAL = 3;

    public boolean mRefreshing = false;
    private LocalDateTime mLatestRefreshing;

    private final int sectionNumber;
    private FirebaseAnalyticsHelper analytics;
    private int lastPosition;

    public InfiniteTimelineScrollListener(int sectionNumber, FirebaseAnalyticsHelper analytics) {
        this.sectionNumber = sectionNumber;
        this.analytics = analytics;
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
            LocalDateTime now = LocalDateTime.now();

            // Suppress unintended continuous refreshing
            if (mLatestRefreshing != null) {
                long diff = Duration.between(mLatestRefreshing, now).getSeconds();

                if (diff < REFRESH_INTERVAL) {
                    LogUtil.d("Suppress unintended continuous refreshing");
                    return;
                }
            }

            analytics.logReadMoreTweetList(sectionNumber);
            mRefreshing = true;
            mLatestRefreshing = now;

            TweetViewHolder lastItem = (TweetViewHolder) recyclerView.findViewHolderForAdapterPosition(totalCount - 1);
            long maxId = lastItem.getTweet().id - 1;
            TweetList.loadTweets(sectionNumber, false, maxId);
        }

        // TODO: Move to other listener
        // TODO: Request before scrolling?
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
