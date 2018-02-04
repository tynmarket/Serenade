package com.tynmarket.serenade.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.tynmarket.serenade.fragment.SlideFragment;

/**
 * Created by tynmarket on 2018/02/04.
 */

public class SlideViewerPagerAdapter extends FragmentStatePagerAdapter {
    public SlideViewerPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return SlideFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
