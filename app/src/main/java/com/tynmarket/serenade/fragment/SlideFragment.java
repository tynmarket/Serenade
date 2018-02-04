package com.tynmarket.serenade.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

    public static SlideFragment newInstance(int sectionNumber) {
        SlideFragment fragment = new SlideFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
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
        if (bundle != null) {
            sectionNumber = bundle.getInt(ARG_SECTION_NUMBER);
        }

        String url = String.format("https://speakerd.s3.amazonaws.com/presentations/f763f8798a5542ac8f3779472f34caef/slide_%d.jpg", sectionNumber - 1);
        Log.d("Serenade", url);
        Glide.with(this).load(url).into(slideImage);

        return rootView;
    }
}
