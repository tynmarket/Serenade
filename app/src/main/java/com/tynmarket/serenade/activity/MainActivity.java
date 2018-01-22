package com.tynmarket.serenade.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.tynmarket.serenade.BuildConfig;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.model.DummyTweet;
import com.tynmarket.serenade.model.util.TweetUtil;
import com.tynmarket.serenade.view.adapter.SectionsPagerAdapter;
import com.tynmarket.serenade.view.adapter.TweetListAdapter;
import com.tynmarket.serenade.view.fragment.RefreshFragment;
import com.tynmarket.serenade.view.listner.InfiniteTimelineScrollListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_LOGIN = 1001;
    private int ITEM_COUNT = 50;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public static TweetListAdapter mHomeTimelineAdapter;
    public static TweetListAdapter mFavoritesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // TODO: Double click
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((View view) -> {
            RefreshFragment fragment = showRefreshIndicator();
            loadHomeTimeline(fragment);
        });

        initTwitterConfig();
        //clearAllGlideCache();
        Intent i = new Intent(this, com.tynmarket.serenade.activity.LoginActivity.class);
        startActivityForResult(i, REQUEST_CODE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == RESULT_OK) {
                loadHomeTimeline();
            } else {
                Log.d("Serenade", "LoginActivity resultCode != RESULT_OK");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initTwitterConfig() {
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(BuildConfig.API_KEY, BuildConfig.API_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }

    public void loadHomeTimeline() {
        loadHomeTimeline(null, true, null);
    }

    public void loadPreviousTimeline(RefreshFragment fragment, Long maxId) {
        loadHomeTimeline(fragment, false, maxId);
    }

    public void loadHomeTimeline(RefreshFragment fragment) {
        loadHomeTimeline(fragment, true, null);
    }

    // TODO: Transaction
    // http://blog.techium.jp/entry/2016/05/27/023716
    private void loadHomeTimeline(RefreshFragment fragment, boolean refresh, Long maxId) {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();
        Call<List<Tweet>> call = statusesService.homeTimeline(ITEM_COUNT, null, maxId, false, false, false, true);

        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                Log.d("Serenade", "homeTimeline success");
                debugTimeline(result.data);

                if (refresh) {
                    mHomeTimelineAdapter.refresh(result.data);
                    // TODO: Not scroll if no new tweets
                    ((RecyclerView) findViewById(R.id.tweet_list)).getLayoutManager().scrollToPosition(0);
                } else {
                    mHomeTimelineAdapter.addTweets(result.data);
                    InfiniteTimelineScrollListener.mRefreshing = false;
                }

                if (fragment != null) {
                    hideRefreshFragment(fragment);
                }
            }

            @Override
            public void failure(TwitterException exception) {
                // TODO: Late limit(Status 429)
                Log.d("Serenade", "homeTimeline failure");
                Toast.makeText(fragment.getContext(), "タイムラインを読み込めませんでした。", Toast.LENGTH_SHORT).show();
                InfiniteTimelineScrollListener.mRefreshing = false;

                if (fragment != null) {
                    hideRefreshFragment(fragment);
                }
            }
        });
    }

    public RefreshFragment showRefreshIndicator() {
        RefreshFragment fragment = new RefreshFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.recyclerViewContainer, fragment).commit();
        return fragment;
    }

    public void hideRefreshFragment(RefreshFragment fragment) {
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @SuppressLint("StaticFieldLeak")
    private void clearAllGlideCache() {
        Glide glide = Glide.get(MainActivity.this);
        glide.clearMemory();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                glide.clearDiskCache();
                Log.d("Serenade", "clearDiskCache() finish");
                return null;
            }
        }.execute();
    }

    private void debugTimeline(List<Tweet> tweets) {
        for (int i = 0; i < tweets.size(); i++) {
            Tweet tweet = tweets.get(i);
            String photoUrl = null;
            String quotedPhotoUrl = null;
            Tweet quotedStatus = tweet.quotedStatus;
            Log.d("Serenade", String.format("timelime: %d", i));
            Log.d("Serenade", tweet.user.name);
            Log.d("Serenade", tweet.text);
            photoUrl = TweetUtil.photoUrl(tweet);
            if (photoUrl != null) {
                Log.d("Serenade", photoUrl);
            }
            if (quotedStatus != null) {
                Log.d("Serenade", String.format("quoted status: %d", i));
                Log.d("Serenade", quotedStatus.user.name);
                Log.d("Serenade", quotedStatus.text);
                quotedPhotoUrl = TweetUtil.photoUrl(quotedStatus);
                if (quotedPhotoUrl != null) {
                    Log.d("Serenade", quotedPhotoUrl);
                }
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final int MAX_PRELOAD = 10;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.tweet_list);

            // Layout
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(manager);

            ArrayList<Tweet> tweets = DummyTweet.dummyTweets();

            // Adapter
            int section = getArguments().getInt(ARG_SECTION_NUMBER);
            TweetListAdapter adapter;

            if (section == 1) {
                adapter = mHomeTimelineAdapter = new TweetListAdapter(tweets);
            } else if (section == 2) {
                adapter = mFavoritesListAdapter = new TweetListAdapter(tweets);
            } else {
                adapter = new TweetListAdapter(tweets);
            }
            rv.setAdapter(adapter);

            // Infinite scroll
            rv.addOnScrollListener(new InfiniteTimelineScrollListener());

            return rootView;
        }
    }
}
