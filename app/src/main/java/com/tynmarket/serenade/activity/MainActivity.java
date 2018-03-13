package com.tynmarket.serenade.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.tynmarket.serenade.BuildConfig;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.event.LoadUserEvent;
import com.tynmarket.serenade.model.LoginUser;
import com.tynmarket.serenade.model.TweetList;
import com.tynmarket.serenade.model.util.TwitterUtil;
import com.tynmarket.serenade.view.adapter.TweetListPagerAdapter;
import com.tynmarket.serenade.view.listner.OpenDrawerOnSwipeListener;
import com.tynmarket.serenade.view.util.ProfileLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity implements ViewPager.OnTouchListener {
    private static final int REQUEST_CODE_LOGIN = 1001;

    private ViewPager mViewPager;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initTwitterConfig();
        boolean signedIn = LoginUser.signedIn();

        if (signedIn) {
            continueMainActivity();
        } else {
            startLoginActivity();
        }
    }

    private void continueMainActivity() {
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TweetListPagerAdapter mSectionsPagerAdapter = new TweetListPagerAdapter(getSupportFragmentManager());

        // Open Drawer
        OpenDrawerOnSwipeListener listener = new OpenDrawerOnSwipeListener() {
            @Override
            public void onSwipe() {
                openDrawer();
            }
        };
        mGestureDetector = new GestureDetector(this, listener);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(listener);
        //noinspection AndroidLintClickableViewAccessibility
        mViewPager.setOnTouchListener(this);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // TODO: Double click
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((View view) -> {
            int sectionNumber = mViewPager.getCurrentItem() + 1;
            TweetList.loadTweets(sectionNumber, true, null);
        });

        TweetList.loadTweets(1, true, null);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, com.tynmarket.serenade.activity.LoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LOGIN);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == RESULT_OK) {
                //LoginUser.loadUser();
                continueMainActivity();
            } else {
                Log.d("Serenade", "LoginActivity resultCode != RESULT_OK");
                TweetList.loadTwitterCards(1);
                //LoginUser.loadUser();
            }
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                LoginUser.loadUser();
            }
        }.execute();
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        v.performClick();
        return false;
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

    @Subscribe
    public void onLoadUserEvent(LoadUserEvent event) {
        ProfileLoader.loadProfile(this, event.user);

        // Open profile
        findViewById(R.id.profile_link).setOnClickListener(v -> {
            closeDrawer();

            Uri uri = TwitterUtil.profileUri(event.user.screenName);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            // TODO: Transition
            startActivity(intent);
        });

        // Open post tweet
        findViewById(R.id.tweet_link).setOnClickListener(v -> {
            closeDrawer();

            Uri uri = Uri.parse("https://twitter.com/intent/tweet");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            // TODO: Transition
            // TODO: startActivityForResult
            startActivity(intent);
        });

        // Open notifications
        findViewById(R.id.notification_link).setOnClickListener(v -> {
            closeDrawer();

            // TODO: Transition
            Uri uri = Uri.parse("https://twitter.com/i/notifications");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            // TODO: Transition
            startActivity(intent);
        });

        // Open search (dummy page of query 'a')
        findViewById(R.id.search_link).setOnClickListener(v -> {
            closeDrawer();

            // TODO: Transition
            Uri uri = Uri.parse("https://twitter.com/search?q=a");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            // TODO: Transition
            startActivity(intent);
        });

        // Open support page
        findViewById(R.id.support_link).setOnClickListener(v -> {
            closeDrawer();

            // TODO: English version
            Uri uri = Uri.parse("https://goo.gl/forms/A1WAEstc076I49dx1");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            // TODO: Transition
            startActivity(intent);
        });

        // Sign out from twitter
        findViewById(R.id.sign_out).setOnClickListener(v -> {
            LoginUser.signOut();

            String text = getString(R.string.sign_out_success);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            // TODO: Move to sign in page

            closeDrawer();
        });
    }

    private void initTwitterConfig() {
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(BuildConfig.API_KEY, BuildConfig.API_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }

    private void openDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

    private void closeDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
