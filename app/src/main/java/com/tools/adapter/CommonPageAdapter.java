package com.tools.adapter;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 通用ViewPager适配器
 * 注意：数据初始化和事件监听 放在Viewpager的OnPageChangeListener中处理
 * 不能放在instantiateItem中处理，因为会出现错乱
 */
public class CommonPageAdapter<T> extends PagerAdapter {
    private List<View> mListViews;
    private int mChildCount = 0;

    /**
     * 构造方法，参数是我们的页卡，这样比较方便。
     *
     * @param mListViews views
     */
    public CommonPageAdapter(List<View> mListViews) {
        this.mListViews = mListViews;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListViews.get(position));//删除页卡
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
        View view = mListViews.get(position);
        container.addView(view);//添加页卡
        return view;
    }

    @Override
    public int getCount() {
        return mListViews.size();//返回页卡的数量
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;//官方提示这样写
    }


    public void reFresh(List<View> viewList) {
        mListViews = viewList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mChildCount = getCount();
    }
}
