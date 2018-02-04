package com.tynmarket.serenade.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tynmarket.serenade.fragment.TweetListFragment;

/**
 * Created by tyn-iMarket on 2018/01/15.
 */

public class TweetListPagerAdapter extends FragmentPagerAdapter {
    public TweetListPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return TweetListFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "ツイート";
            case 1:
                return "いいね";
            case 2:
                return "未定";
        }
        return null;
    }
}