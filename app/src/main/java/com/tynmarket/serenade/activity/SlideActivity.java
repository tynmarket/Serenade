package com.tynmarket.serenade.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tynmarket.serenade.R;
import com.tynmarket.serenade.model.SpeakerDeck;
import com.tynmarket.serenade.view.adapter.SlideViewerPagerAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tynmarket on 2018/02/04.
 */

public class SlideActivity extends AppCompatActivity {
    public static final String EXPANDED_URL = "expanded_url";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Show loading
        setContentView(R.layout.activity_slide);

        Bundle extras = getIntent().getExtras();
        String expandedUrl = extras.getString(EXPANDED_URL);

        String[] params = SpeakerDeck.slideHtmlParams(expandedUrl);
        Call<ResponseBody> call = SpeakerDeck.getApiClient().slideHtml(params[0], params[1]);

        // TODO: RxJava
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody resp = response.body();
                    try {
                        String html = resp.string();
                        String slideUrl = SpeakerDeck.slideUrlFromHtml(html);
                        Log.d("Serenade", String.format("slideUrl: %s", slideUrl));

                        // TODO: setContentView again?
                        initViewPager(slideUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // TODO: 404
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // TODO: Network error
            }
        });
    }

    private void initViewPager(String slideUrl) {
        ViewPager mViewPager = findViewById(R.id.slide_viewer_view_pager);
        SlideViewerPagerAdapter adapter = new SlideViewerPagerAdapter(getSupportFragmentManager(), slideUrl);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);
    }
}
