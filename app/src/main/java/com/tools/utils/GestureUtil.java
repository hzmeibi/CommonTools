package com.tools.utils;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by milo on 17/7/27.
 * 视图手势监听
 * 点击 onClick
 * 左滑 scrollLeft
 * 右滑 scrollRight
 * 注意：左滑/右滑 完成后  onClick 事件也会执行 所以具体操作通过回调返回上层处理
 */
public class GestureUtil implements View.OnClickListener, View.OnTouchListener, GestureDetector.OnGestureListener {
    private scrollListener mScrollListener;
    private GestureDetector mGestureDetector;

    private static final int FLING_MIN_DISTANCE = 50;//横向移动最小值
    private static final int FLING_MIN_VELOCITY = 0;//竖向移动最小值

    /**
     * 初始化
     *
     * @param view           view
     * @param scrollListener 回调
     */
    public GestureUtil(View view, scrollListener scrollListener) {
        mScrollListener = scrollListener;
        view.setOnClickListener(this);
        view.setOnTouchListener(this);
        view.setLongClickable(true);
        mGestureDetector = new GestureDetector(this);
    }

    public interface scrollListener {
        void onClick();

        boolean scrollLeft();

        boolean scrollRight();
    }

    @Override
    public void onClick(View v) {
        mScrollListener.onClick();
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

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            // Fling left
            return mScrollListener.scrollLeft();

        } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
            // Fling right
            return mScrollListener.scrollRight();
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);//动作交给mGestureDetector执行
    }
}
