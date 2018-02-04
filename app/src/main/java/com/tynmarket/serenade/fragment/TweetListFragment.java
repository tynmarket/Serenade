package com.tynmarket.serenade.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.twitter.sdk.android.core.models.Tweet;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.event.LoadFailureTweetListEvent;
import com.tynmarket.serenade.event.LoadTweetListEvent;
import com.tynmarket.serenade.event.StartLoadTweetListEvent;
import com.tynmarket.serenade.model.util.DummyTweet;
import com.tynmarket.serenade.view.adapter.TweetListAdapter;
import com.tynmarket.serenade.view.listner.InfiniteTimelineScrollListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by tyn-iMarket on 2018/01/29.
 */

public class TweetListFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private int sectionNumber;
    private RecyclerView rv;
    private ProgressBar progressBar;
    private TweetListAdapter adapter;
    private InfiniteTimelineScrollListener scrollListener;

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
        this.progressBar = rootView.findViewById(R.id.refresh);

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.sectionNumber = bundle.getInt(ARG_SECTION_NUMBER);
        }

        // Layout
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);

        // Adapter
        ArrayList<Tweet> tweets = DummyTweet.tweets();
        this.adapter = new TweetListAdapter(tweets);
        rv.setAdapter(adapter);

        // Infinite scroll
        this.scrollListener = new InfiniteTimelineScrollListener(sectionNumber);
        rv.addOnScrollListener(scrollListener);

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

    @Subscribe
    public void onStartLoadTweetListEvent(StartLoadTweetListEvent event) {
        if (event.sectionNumber == sectionNumber) {
            showRefreshIndicator();
        }
    }

    @Subscribe
    public void onLoadTweetListEvent(LoadTweetListEvent event) {
        if (event.sectionNumber == sectionNumber) {
            if (event.refresh) {
                adapter.refresh(event.tweets);
                // TODO: Not scroll if no new tweets
                rv.getLayoutManager().scrollToPosition(0);
            } else {
                adapter.addTweets(event.tweets);
                scrollListener.mRefreshing = false;
            }
            hideRefreshIndicator();
        }
    }

    @Subscribe
    public void onLoadFailureTweetListEvent(LoadFailureTweetListEvent event) {
        if (event.sectionNumber == sectionNumber) {
            // TODO: I18n
            Toast.makeText(rv.getContext(), "タイムラインを読み込めませんでした。", Toast.LENGTH_SHORT).show();
            scrollListener.mRefreshing = false;

            hideRefreshIndicator();
        }
    }

    private void showRefreshIndicator() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideRefreshIndicator() {
        progressBar.setVisibility(View.GONE);
    }
}
