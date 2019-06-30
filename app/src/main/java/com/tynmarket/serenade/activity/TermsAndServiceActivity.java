package com.tynmarket.serenade.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tynmarket.serenade.BuildConfig;
import com.tynmarket.serenade.R;

/**
 * Created by tynmarket on 2018/05/04.
 */
public class TermsAndServiceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_service);

        TextView versionCode = findViewById(R.id.version_code);
        versionCode.setText(BuildConfig.VERSION_NAME);
        // TODO: Set title
        //getSupportActionBar().setTitle(R.string.terms_and_service_title);
    }
}
