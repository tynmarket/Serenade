package com.tynmarket.serenade.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.FavoriteService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.event.LoadFavoritesListEvent;
import com.tynmarket.serenade.event.LoadHomeTimelineEvent;
import com.tynmarket.serenade.model.DummyTweet;
import com.tynmarket.serenade.model.util.TweetUtil;
import com.tynmarket.serenade.view.adapter.TweetListAdapter;
import com.tynmarket.serenade.view.listner.InfiniteTimelineScrollListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by tyn-iMarket on 2018/01/29.
 */

public class TweetListFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private final int ITEM_COUNT = 50;
    private final int SECTION_NUMBER_HOME_TIMELINE = 1;
    private final int SECTION_NUMBER_FAVORITE_LIST = 2;

    private int sectionNumber;
    private RecyclerView rv;
    private TweetListAdapter adapter;

    public TweetListFragment() {
    }

    public static TweetListFragment newInstance(int sectionNumber) {
        TweetListFragment fragment = new TweetListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        this.rv = rootView.findViewById(R.id.tweet_list);

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.sectionNumber = bundle.getInt(ARG_SECTION_NUMBER);
        }

        // Layout
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);

        // Adapter
        ArrayList<Tweet> tweets = DummyTweet.dummyTweets();
        this.adapter = new TweetListAdapter(tweets);
        rv.setAdapter(adapter);

        // Infinite scroll
        rv.addOnScrollListener(new InfiniteTimelineScrollListener());

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    // TODO: Transaction
    // http://blog.techium.jp/entry/2016/05/27/023716
    // TODO: I18n
    @Subscribe
    public void onLoadHomeTimelineEvent(LoadHomeTimelineEvent event) {
        if (sectionNumber != SECTION_NUMBER_HOME_TIMELINE) {
            return;
        }

        adapter.showRefreshIndicator();

        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();
        Call<List<Tweet>> call = statusesService.homeTimeline(ITEM_COUNT, null, event.maxId, false, false, false, true);

        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                Log.d("Serenade", "homeTimeline success");
                TweetUtil.debugTimeline(result.data);

                if (event.refresh) {
                    adapter.refresh(result.data);
                    // TODO: Not scroll if no new tweets
                    rv.getLayoutManager().scrollToPosition(0);
                } else {
                    adapter.addTweets(result.data);
                    InfiniteTimelineScrollListener.mRefreshing = false;
                }
                adapter.hideRefreshIndicator();
            }

            @Override
            public void failure(TwitterException exception) {
                // TODO: Late limit(Status 429)
                Log.d("Serenade", "homeTimeline failure");
                Toast.makeText(rv.getContext(), "タイムラインを読み込めませんでした。", Toast.LENGTH_SHORT).show();
                InfiniteTimelineScrollListener.mRefreshing = false;

                adapter.hideRefreshIndicator();
            }
        });
    }

    @Subscribe
    public void onLoadFavoritesListEvent(LoadFavoritesListEvent event) {
        if (sectionNumber != SECTION_NUMBER_FAVORITE_LIST) {
            return;
        }

        adapter.showRefreshIndicator();

        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        FavoriteService service = twitterApiClient.getFavoriteService();
        Call<List<Tweet>> call = service.list(null, null, ITEM_COUNT, null, event.maxIdStr, true);

        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                Log.d("Serenade", "favoriteList success");
                TweetUtil.debugTimeline(result.data);

                if (event.refresh) {
                    adapter.refresh(result.data);
                    // TODO: Not scroll if no new tweets
                    rv.getLayoutManager().scrollToPosition(0);
                } else {
                    adapter.addTweets(result.data);
                    InfiniteTimelineScrollListener.mRefreshing = false;
                }
                adapter.hideRefreshIndicator();
            }

            @Override
            public void failure(TwitterException exception) {
                // TODO: Late limit(Status 429)
                Log.d("Serenade", "favoriteList failure");
                Toast.makeText(rv.getContext(), "いいねを読み込めませんでした。", Toast.LENGTH_SHORT).show();
                InfiniteTimelineScrollListener.mRefreshing = false;
                adapter.hideRefreshIndicator();
            }
        });
    }
}
