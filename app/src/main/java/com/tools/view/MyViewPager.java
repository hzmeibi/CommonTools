package com.tools.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * MyViewPager
 * 动态控制是否关闭滑动
 * 1.引导页
 * 2.相册页面 （结合PhotoView 放大缩小）
 */

public class MyViewPager extends ViewPager {
    private boolean isCanScroll = true;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return this.isCanScroll && super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isCanScroll && super.onTouchEvent(event);
    }

    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

}
