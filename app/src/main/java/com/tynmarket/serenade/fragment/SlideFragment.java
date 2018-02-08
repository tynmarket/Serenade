package com.tynmarket.serenade.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tynmarket.serenade.R;

/**
 * Created by tynmarket on 2018/02/04.
 */

public class SlideFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SLIDE_URL = "slide_url";

    public static SlideFragment newInstance(int sectionNumber, String slideUrl) {
        SlideFragment fragment = new SlideFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ARG_SLIDE_URL, slideUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_slide, container, false);
        ImageView slideImage = rootView.findViewById(R.id.slide_image);

        Bundle bundle = getArguments();
        int sectionNumber = 1;
        String slideUrl = "";
        if (bundle != null) {
            sectionNumber = bundle.getInt(ARG_SECTION_NUMBER);
            slideUrl = bundle.getString(ARG_SLIDE_URL);
        }

        String url = String.format("%sslide_%d.jpg", slideUrl, sectionNumber - 1);
        // TODO: Retry on failure
        Glide.with(this).load(url).into(slideImage);

        return rootView;
    }

}
