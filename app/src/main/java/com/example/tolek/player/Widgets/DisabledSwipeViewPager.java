package com.example.tolek.player.Widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Tolek on 07.03.2018.
 */

public class DisabledSwipeViewPager extends ViewPager {
    private boolean enableSwipe;

    public DisabledSwipeViewPager(Context context) {
        super(context);
        init();
    }

    public DisabledSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        enableSwipe = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return enableSwipe && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return enableSwipe && super.onTouchEvent(event);
    }

    public void setEnableSwipe(boolean enableSwipe) {
        this.enableSwipe = enableSwipe;
    }
}
