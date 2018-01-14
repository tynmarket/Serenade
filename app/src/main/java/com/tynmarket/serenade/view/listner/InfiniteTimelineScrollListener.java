package com.tynmarket.serenade.view.listner;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.tynmarket.serenade.activity.MainActivity;
import com.tynmarket.serenade.view.fragment.RefreshFragment;

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
            Log.d("Serenade", "Refreshing");

            MainActivity activity = (MainActivity) recyclerView.getContext();
            RefreshFragment fragment = activity.showRefreshIndicator();
            activity.loadPreviousTimeline(fragment);
        }
    }
}
