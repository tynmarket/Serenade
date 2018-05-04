package com.tynmarket.serenade.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.tynmarket.serenade.R;

/**
 * Created by tynmarket on 2017/12/30.
 */

public class LoginActivity extends AppCompatActivity {
    public static final int RESULT_CODE_LOGIN_BACK = 1000;

    private TwitterLoginButton mLoginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTwitterLoginButtonCallback();
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            final PackageManager manager = getPackageManager();
            manager.getApplicationInfo("com.twitter.android", 0);
            View view = findViewById(R.id.install_twitter);
            view.setVisibility(View.GONE);
        } catch (PackageManager.NameNotFoundException e) {
            // Do nothing
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_CODE_LOGIN_BACK);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setTwitterLoginButtonCallback() {
        mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void failure(TwitterException exception) {
                finish();
            }
        });
    }
}
