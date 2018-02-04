package com.tynmarket.serenade.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tynmarket.serenade.R;
import com.tynmarket.serenade.view.adapter.SlideViewerPagerAdapter;

/**
 * Created by tynmarket on 2018/02/04.
 */

public class SlideActivity extends AppCompatActivity {
    public static final String DISPLAY_URL = "display_url";

    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        Bundle extras = getIntent().getExtras();
        String displayUrl = extras.getString(DISPLAY_URL);
        Toast.makeText(this, displayUrl, Toast.LENGTH_SHORT).show();

        mViewPager = findViewById(R.id.slide_viewer_view_pager);
        SlideViewerPagerAdapter adapter = new SlideViewerPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
    }
}