package com.tynmarket.serenade.view.listner;

import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by tynmarket on 2018/03/07.
 */

public abstract class OpenDrawerOnSwipeListener implements ViewPager.OnPageChangeListener, GestureDetector.OnGestureListener {
    private final int SWIPE_VELOCITY = 200;

    private int position;
    private float positionOffset;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (this.position != position) {
            this.position = position;
        }
        if (this.positionOffset != positionOffset) {
            this.positionOffset = positionOffset;
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (position == 0 && positionOffset == 0 && velocityX >= SWIPE_VELOCITY) {
            onSwipe();
        }
        return false;
    }

    public abstract void onSwipe();

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }
}
