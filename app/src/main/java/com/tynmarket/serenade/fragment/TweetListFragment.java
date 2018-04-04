package com.tynmarket.serenade.fragment;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.twitter.sdk.android.core.models.Tweet;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.event.LoadFailureTweetListEvent;
import com.tynmarket.serenade.event.LoadTweetListEvent;
import com.tynmarket.serenade.event.LoadTwitterCardsEvent;
import com.tynmarket.serenade.event.StartLoadTweetListEvent;
import com.tynmarket.serenade.model.entity.TwitterCard;
import com.tynmarket.serenade.model.sqlite.TweetSQLiteHelper;
import com.tynmarket.serenade.model.util.DisposableHelper;
import com.tynmarket.serenade.model.util.DummyTweet;
import com.tynmarket.serenade.model.util.TweetUtil;
import com.tynmarket.serenade.view.adapter.TweetListAdapter;
import com.tynmarket.serenade.view.holder.TweetViewHolder;
import com.tynmarket.serenade.view.listner.InfiniteTimelineScrollListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by tyn-iMarket on 2018/01/29.
 */

public class TweetListFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String KEY_SCROLL_POSITION = "KEY_SCROLL_POSITION";

    private int sectionNumber;
    private int position = 0;

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

        //TweetList.loadTweets(sectionNumber, true, null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        this.rv = rootView.findViewById(R.id.tweet_list);
        this.progressBar = rootView.findViewById(R.id.refresh);

        // Layout
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);

        if (adapter == null) {
            boolean debug = false;
            ArrayList<Tweet> tweets;

            // Adapter
            if (debug) {
                tweets = DummyTweet.tweets();
            } else {
                tweets = new ArrayList<>();
                if (sectionNumber == 1) {
                    TweetSQLiteHelper helper = TweetSQLiteHelper.getHelper();
                    SQLiteDatabase db = helper.getReadableDatabase();

                    Cursor cursor = db.rawQuery(TweetSQLiteHelper.SELECT_STATEMENT, new String[]{String.valueOf(sectionNumber)});
                    while (cursor.moveToNext()) {
                        String json = cursor.getString(cursor.getColumnIndex("tweet"));
                        Gson gson = new Gson();
                        Tweet tweet = gson.fromJson(json, Tweet.class);
                        tweets.add(tweet);
                    }
                    cursor.close();
                    db.close();
                }
            }
            this.adapter = new TweetListAdapter(tweets);
        }

        rv.setAdapter(adapter);

        if (sectionNumber == 1) {
            if (savedInstanceState == null) {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.getContext());
                this.position = pref.getInt(KEY_SCROLL_POSITION, 0);

                String log = String.format("Restore position from pref: %d", position);
                Log.d("Serenade", log);
                Toast.makeText(this.getContext(), log, Toast.LENGTH_SHORT).show();
            } else {
                this.position = savedInstanceState.getInt(KEY_SCROLL_POSITION);

                String log = String.format("Restore position from savedInstanceState: %d", position);
                Log.d("Serenade", log);
                Toast.makeText(this.getContext(), log, Toast.LENGTH_SHORT).show();
            }

            manager.scrollToPosition(position);
        }


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
    public void onPause() {
        super.onPause();
        DisposableHelper.clear(sectionNumber);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (sectionNumber == 1) {
            LinearLayoutManager manager = (LinearLayoutManager) rv.getLayoutManager();
            int position = manager.findFirstVisibleItemPosition();

            String log = String.format("Save position: %d", position);
            Log.d("Serenade", log);
            Toast.makeText(this.getContext(), log, Toast.LENGTH_SHORT).show();

            outState.putInt(KEY_SCROLL_POSITION, position);

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.getContext());
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(KEY_SCROLL_POSITION, position);
            editor.apply();
        }
        super.onSaveInstanceState(outState);
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

    @Subscribe
    public void onLoadTwitterCardsEvent(LoadTwitterCardsEvent event) {
        if (event.sectionNumber == sectionNumber) {
            adapter.refreshCards(event.cards);

            LinearLayoutManager manager = (LinearLayoutManager) rv.getLayoutManager();
            int first = manager.findFirstVisibleItemPosition();
            int last = manager.findLastVisibleItemPosition();

            for (int i = first; i < last + 1; i++) {
                TweetViewHolder holder = (TweetViewHolder) rv.findViewHolderForAdapterPosition(i);
                TwitterCard card = event.cards.get(TweetUtil.expandedUrl(holder.getTweet()));
                holder.setCardToBindings(card);
            }
        }
    }

    private void showRefreshIndicator() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideRefreshIndicator() {
        progressBar.setVisibility(View.GONE);
    }
}
