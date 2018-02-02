package com.tynmarket.serenade.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.tynmarket.serenade.BuildConfig;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.event.LoadFavoritesListEvent;
import com.tynmarket.serenade.event.LoadHomeTimelineEvent;
import com.tynmarket.serenade.view.adapter.SectionsPagerAdapter;

import org.greenrobot.eventbus.EventBus;

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
            loadTweets(true, null);
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
                // TODO: FIX unable to load
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

    public void loadTweets(boolean refresh, Long maxId) {
        int position = mViewPager.getCurrentItem();
        switch (position) {
            case 0:
                EventBus.getDefault().post(new LoadHomeTimelineEvent(refresh, maxId));
                break;
            case 1:
                String maxIdStr = maxId != null ? String.valueOf(maxId) : null;
                EventBus.getDefault().post(new LoadFavoritesListEvent(refresh, maxIdStr));
                break;
            case 2:
                // To be ...
                break;
        }
    }

    public void loadHomeTimeline() {
        EventBus.getDefault().post(new LoadHomeTimelineEvent(true, null));
    }

    @SuppressLint("StaticFieldLeak")
    private void clearAllGlideCache() {
        Glide glide = Glide.get(MainActivity.this);
        glide.clearMemory();
        // TODO: Lambda syntax
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                glide.clearDiskCache();
                Log.d("Serenade", "clearDiskCache() finish");
                return null;
            }
        }.execute();
    }
}
