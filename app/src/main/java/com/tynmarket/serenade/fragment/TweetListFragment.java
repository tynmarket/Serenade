package com.tynmarket.serenade.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.twitter.sdk.android.core.TwitterApiErrorConstants;
import com.twitter.sdk.android.core.TwitterApiException;
import com.twitter.sdk.android.core.models.Tweet;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.event.LoadFailureTweetListEvent;
import com.tynmarket.serenade.event.LoadTweetListEvent;
import com.tynmarket.serenade.event.LoadTwitterCardEvent;
import com.tynmarket.serenade.event.LoadTwitterCardsEvent;
import com.tynmarket.serenade.event.SignOutEvent;
import com.tynmarket.serenade.event.StartLoadTweetListEvent;
import com.tynmarket.serenade.model.TweetList;
import com.tynmarket.serenade.model.TwitterCardList;
import com.tynmarket.serenade.model.entity.TwitterCard;
import com.tynmarket.serenade.model.util.DisposableHelper;
import com.tynmarket.serenade.model.util.DummyTweet;
import com.tynmarket.serenade.model.util.FirebaseAnalyticsHelper;
import com.tynmarket.serenade.model.util.LogUtil;
import com.tynmarket.serenade.model.util.Resource;
import com.tynmarket.serenade.view.adapter.TweetListAdapter;
import com.tynmarket.serenade.view.listner.InfiniteTimelineScrollListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by tynmarket on 2018/01/29.
 */

public class TweetListFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final long LOAD_TWITTER_CARD_CACHE_DELAY = 500;
    private static boolean debug = false;

    private int sectionNumber;
    private FirebaseAnalyticsHelper analytics;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Save data on screen rotation
        setRetainInstance(true);

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.sectionNumber = bundle.getInt(ARG_SECTION_NUMBER);
        }
        this.analytics = new FirebaseAnalyticsHelper(this.getContext());

        if (!debug) {
            TweetList.loadTweets(sectionNumber, true, null);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        this.rv = rootView.findViewById(R.id.tweet_list);
        this.progressBar = rootView.findViewById(R.id.refresh);

        // Layout
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(manager);

        // Animation
        muteAnimation();

        // Adapter
        if (adapter == null) {
            ArrayList<Tweet> tweets;

            if (debug) {
                tweets = DummyTweet.tweets();
            } else {
                tweets = new ArrayList<>();
            }
            this.adapter = new TweetListAdapter(tweets);
        }
        rv.setAdapter(adapter);

        // Infinite scroll
        this.scrollListener = new InfiniteTimelineScrollListener(sectionNumber, analytics);
        rv.addOnScrollListener(scrollListener);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        DisposableHelper.clear(sectionNumber);
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
            TwitterApiException twitterApiException = null;

            if (event.throwable instanceof TwitterApiException) {
                twitterApiException = (TwitterApiException) event.throwable;
            }

            if (twitterApiException != null &&
                    twitterApiException.getErrorCode() == TwitterApiErrorConstants.RATE_LIMIT_EXCEEDED) {
                String message = String.format(
                        Resource.getString(R.string.rate_limit_exceeded), twitterApiException.getTwitterRateLimit().getLimit());
                Toast.makeText(rv.getContext(), message, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(rv.getContext(), R.string.load_tweet_failure, Toast.LENGTH_LONG).show();
            }

            scrollListener.mRefreshing = false;
            hideRefreshIndicator();
        }
    }

    @Subscribe
    public void onLoadTwitterCardsEvent(LoadTwitterCardsEvent event) {
        if (event.sectionNumber == sectionNumber) {
            adapter.refreshCards(event.cards);

            LinearLayoutManager manager = (LinearLayoutManager) rv.getLayoutManager();
            int first = manager.findFirstVisibleItemPosition();
            int last = manager.findLastVisibleItemPosition() + 1;
            ArrayList<Integer> postDelays = new ArrayList<>();

            for (int position = first; position <= last; position++) {
                if (adapter.requestCardCache(position)) {
                    postDelays.add(position);
                } else {
                    adapter.notifyItemChanged(position);
                }
            }

            new Handler().postDelayed(() -> {
                for (Integer position : postDelays) {
                    Tweet tweet = adapter.getTweet(position);
                    TwitterCardList.loadTwitterCard(sectionNumber, position, tweet);
                }
            }, LOAD_TWITTER_CARD_CACHE_DELAY);
        }
    }

    @Subscribe
    public void onLoadTwitterCardEvent(LoadTwitterCardEvent event) {
        if (event.sectionNumber == sectionNumber) {
            LogUtil.d("onLoadTwitterCardEvent");

            int position = event.position;
            TwitterCard card = event.card;
            if (card == null) {
                return;
            }

            adapter.replaceCard(card);
            adapter.notifyItemChanged(position);
        }
    }

    @Subscribe
    public void onSignOutEvent(SignOutEvent event) {
        adapter.clearAll();
    }

    private void showRefreshIndicator() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideRefreshIndicator() {
        progressBar.setVisibility(View.GONE);
    }

    private void muteAnimation() {
        rv.getItemAnimator().setChangeDuration(0);
        ((DefaultItemAnimator) rv.getItemAnimator()).setSupportsChangeAnimations(false);
    }
}
