package com.tynmarket.serenade.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.tynmarket.serenade.core.Twitter;
import com.tynmarket.serenade.core.TwitterAuthConfig;
import com.tynmarket.serenade.core.TwitterConfig;
import com.tynmarket.serenade.BuildConfig;
import com.tynmarket.serenade.R;
import com.tynmarket.serenade.event.LoadUserEvent;
import com.tynmarket.serenade.model.LoginUser;
import com.tynmarket.serenade.model.TweetList;
import com.tynmarket.serenade.model.util.ActivityHelper;
import com.tynmarket.serenade.model.util.FirebaseAnalyticsHelper;
import com.tynmarket.serenade.model.util.FirebaseRemoteConfigHelper;
import com.tynmarket.serenade.model.util.LogUtil;
import com.tynmarket.serenade.model.util.TwitterUtil;
import com.tynmarket.serenade.view.adapter.TweetListPagerAdapter;
import com.tynmarket.serenade.view.custom.NavigationProfileView;
import com.tynmarket.serenade.view.listner.OpenDrawerOnSwipeListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity implements ViewPager.OnTouchListener {
    private static final int REQUEST_CODE_LOGIN = 1001;
    private static final int REQUEST_CODE_LOGIN_AFTER_SIGN_OUT = 1002;

    private FirebaseAnalyticsHelper analytics;
    private ViewPager mViewPager;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTwitterConfig();

        super.onCreate(savedInstanceState);

        initFirebaseConfig();

        if (LoginUser.signedIn()) {
            loadUser();
            continueMainActivity();
        } else {
            startLoginActivity();
        }
    }

    private void continueMainActivity() {
        setContentView(R.layout.activity_main);

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

        // Tab icon
        Tab tabHome = tabLayout.getTabAt(0);
        if (tabHome != null) {
            tabHome.setIcon(R.drawable.home_selector);
        }
        Tab tabFavorite = tabLayout.getTabAt(1);
        if (tabFavorite != null) {
            tabFavorite.setIcon(R.drawable.favorite_selector);
        }

        // TODO: Double click
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((View view) -> {
            int sectionNumber = mViewPager.getCurrentItem() + 1;
            analytics.logRefreshTweetList(sectionNumber);
            TweetList.loadTweets(sectionNumber, true, null);
        });
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, com.tynmarket.serenade.activity.LoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LOGIN);
    }


    private void startLoginActivityAfterSignOut() {
        Intent intent = new Intent(this, com.tynmarket.serenade.activity.LoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LOGIN_AFTER_SIGN_OUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOGIN) {
            // Login
            handleResultLogin(resultCode);
        } else if (requestCode == REQUEST_CODE_LOGIN_AFTER_SIGN_OUT) {
            handleResultLoginAfterSignOut(resultCode);
        } else {
            // Reenter
            overridePendingTransition(0, android.R.anim.slide_out_right);
        }
    }

    private void handleResultLoginAfterSignOut(int resultCode) {
        if (resultCode == RESULT_OK) {
            Toast.makeText(this, R.string.login_success, Toast.LENGTH_LONG).show();
            loadUser();
            TweetList.loadTweets(1, true, null);
            TweetList.loadTweets(2, true, null);
        } else {
            handleResultLogin(resultCode);
        }
    }

    private void handleResultLogin(int resultCode) {
        if (resultCode == RESULT_OK) {
            Toast.makeText(this, R.string.login_success, Toast.LENGTH_LONG).show();

            loadUser();
            continueMainActivity();
        } else if (resultCode == LoginActivity.RESULT_CODE_LOGIN_BACK) {
            finish();
        } else {
            LogUtil.d("LoginActivity resultCode != RESULT_OK");
            Toast.makeText(this, R.string.login_failure, Toast.LENGTH_LONG).show();

            startLoginActivity();
        }
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

    @Subscribe
    public void onLoadUserEvent(LoadUserEvent event) {
        // Navigation profile
        NavigationProfileView profile = findViewById(R.id.navigation_profile);
        profile.binding.setUser(event.user);
        profile.setOnNavigationItemClickListener(v -> {
            closeDrawer();
        });

        // Open profile
        findViewById(R.id.profile_link).setOnClickListener(v -> {
            analytics.logViewProfile();
            closeDrawer();

            Uri uri = TwitterUtil.profileUri(event.user.screenName);
            ActivityHelper.startUriActivity(this, uri);
        });

        // Open list
        findViewById(R.id.list_link).setOnClickListener(v -> {
            analytics.logViewList();
            closeDrawer();

            Uri uri = TwitterUtil.listUri(event.user.screenName);
            ActivityHelper.startUriActivity(this, uri);
        });

        // Open post tweet
        findViewById(R.id.tweet_link).setOnClickListener(v -> {
            analytics.logViewPostTweet();
            closeDrawer();

            Uri uri = TwitterUtil.newTweetUri();
            // TODO: startActivityForResult
            ActivityHelper.startUriActivity(this, uri);
        });

        // Open notifications
        findViewById(R.id.notification_link).setOnClickListener(v -> {
            analytics.logViewNotification();
            closeDrawer();

            Uri uri = TwitterUtil.notificationUri();
            ActivityHelper.startUriActivity(this, uri);
        });

        // Open search (dummy page of query 'a')
        findViewById(R.id.search_link).setOnClickListener(v -> {
            analytics.logViewSearch();
            closeDrawer();

            Uri uri = TwitterUtil.searchUri();
            ActivityHelper.startUriActivity(this, uri);
        });

        // Open messages
        findViewById(R.id.message_link).setOnClickListener(v -> {
            analytics.logViewMessage();
            closeDrawer();

            Uri uri = TwitterUtil.messageUri();
            ActivityHelper.startUriActivity(this, uri);
        });

        // Open support page
        findViewById(R.id.support_link).setOnClickListener(v -> {
            analytics.logViewSupport();
            closeDrawer();

            // TODO: English version
            Uri uri = Uri.parse("https://goo.gl/forms/A1WAEstc076I49dx1");
            ActivityHelper.startUriActivity(this, uri);
        });

        // Sign out from twitter
        findViewById(R.id.sign_out).setOnClickListener(v -> {
            analytics.logViewSignOut();
            new AlertDialog.Builder(this)
                    .setMessage(R.string.sign_out_prompt_message)
                    .setPositiveButton(R.string.text_ok, (dialog, which) -> {
                        Toast.makeText(this, R.string.sign_out_success, Toast.LENGTH_LONG).show();
                        closeDrawer();

                        LoginUser.signOut();
                        startLoginActivityAfterSignOut();
                    })
                    .setNegativeButton(R.string.text_cancel, null)
                    .show();
        });


        // Open terms and service
        findViewById(R.id.terms_and_service).setOnClickListener(v -> {
            analytics.logViewTermsAndService();
            closeDrawer();

            Intent intent = new Intent(this, com.tynmarket.serenade.activity.TermsAndServiceActivity.class);
            ActivityHelper.startActivity(this, intent);
        });
    }

    private void initTwitterConfig() {
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(LogUtil.twitterLogger())
                .twitterAuthConfig(new TwitterAuthConfig(BuildConfig.API_KEY, BuildConfig.API_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }

    private void initFirebaseConfig() {
        FirebaseRemoteConfigHelper.init();
        this.analytics = new FirebaseAnalyticsHelper(this);
    }

    private void loadUser() {
        LoginUser.loadUser();
    }

    private void openDrawer() {
        // TODO: Update profile
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

    private void closeDrawer() {
        Handler handler = new Handler();

        // Add delay before Closing the drawer to recognize ripple effect
        handler.postDelayed(() -> {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }, 250);
    }
}
