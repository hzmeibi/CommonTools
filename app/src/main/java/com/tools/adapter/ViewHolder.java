package com.tools.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 通用ViewHolder
 */
public class ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;

    public ViewHolder(Context context, int layoutId) {
        this.mViews = new SparseArray<>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, null);
        mConvertView.setTag(this);
    }

    public static ViewHolder get(Context context, View convertView, int layoutId) {
        if (convertView == null) {
            return new ViewHolder(context, layoutId);
        }
        return (ViewHolder) convertView.getTag();
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public ViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public ViewHolder setBtnText(int viewId, String text) {
        Button tv = getView(viewId);
        tv.setText(text);
        return this;
    }


    public ViewHolder setBackgroundResource(int viewId, int resourceId) {
        View view = getView(viewId);
        view.setBackgroundResource(resourceId);
        return this;
    }

    public ViewHolder setOnclickListener(int viewId, final doOnclickListener listener) {
        getView(viewId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onclick(v);
            }
        });
        return this;
    }

    public interface doOnclickListener {
        void onclick(View view);
    }
}
