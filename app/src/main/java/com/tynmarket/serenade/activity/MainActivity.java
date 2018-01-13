package com.tynmarket.serenade.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.FixedPreloadSizeProvider;
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
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.StatusesService;
import com.tynmarket.serenade.BuildConfig;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.view.adapter.TweetListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_LOGIN = 1001;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initTwitterConfig();

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

    private void loadHomeTimeline() {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();
        Call<List<Tweet>> call = statusesService.homeTimeline(20, null, null, false, false, false, true);

        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                Log.d("Serenade", "homeTimeline success");
                mHomeTimelineAdapter.refresh(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("Serenade", "homeTimeline failure");
            }
        });
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

            ArrayList<Tweet> tweets = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                Tweet tweet = dummyTweet(i);
                tweets.add(tweet);
            }

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

            // Glide
            int iconSize = TweetListAdapter.ICON_SIZE;
            ListPreloader.PreloadSizeProvider provider = new FixedPreloadSizeProvider(iconSize, iconSize);
            RecyclerViewPreloader<Tweet> loader =
                    new RecyclerViewPreloader<Tweet>(this, adapter, provider, MAX_PRELOAD);
            rv.addOnScrollListener(loader);

            return rootView;
        }

        private Tweet dummyTweet(int i) {
            Tweet tweet = new Tweet(null, "10時間", null, null,
                    null, 0, false, "filterLevel", i + 1,
                    String.valueOf(i + 1), "inReplyToScreenName", 0,
                    "inReplyToStatusIdStr", 0, "inReplyToUserIdStr",
                    "lang", null, false, null, 0,
                    "quotedStatusIdStr", null, 0, false,
                    null, "source", dummyText(i), null,
                    false, dummyUser(i), false, null,
                    "withheldScope", null);

            return tweet;
        }

        private String dummyText(int i) {
            return String.format("ツイート内容ツイート内容ツイート内容ツイート内容ツイート内容ツイート内容 %d", i + 1);
        }

        private User dummyUser(int i) {
            User user = new User(false, "createdAt", false,
                    false, "description", "emailAddress",
                    null, 0, false, 0,
                    0, false, 0, "idStr", false,
                    "lang", 0, "location", String.format("ティン＠iMarket管理人あああ %d", i + 1),
                    "profileBackgroundColor", "profileBackgroundImageUrl",
                    "profileBackgroundImageUrlHttps", false,
                    "profileBannerUrl", "profileImageUrl",
                    "https://pbs.twimg.com/profile_images/742013491/06c940e6-s_normal.png",
                    "profileLinkColor",
                    "profileSidebarBorderColor", "profileSidebarFillColor",
                    "profileTextColor", false, false,
                    String.format("tynmarket %d", i + 1), false, null, 0,
                    "timeZone", "url", 0, false, null,
                    "withheldScope");
            return user;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ツイート";
                case 1:
                    return "いいね";
                case 2:
                    return "未定";
            }
            return null;
        }
    }
}
