package com.tools.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tools.R;


/**
 * 提示视图
 * 可以添加图标
 */
public class MyToastView extends Toast {
    private MyToastView mMyToastView;
    private Context mContext;
    private Toast mToast;
    private Integer mId;

    public MyToastView(Context context) {
        super(context);
        mContext = context;
        mMyToastView = this;
    }

    /**
     * 自定义Toast样式
     *
     * @param resId
     * @param textId
     * @param duration
     * @description
     */
    private Toast makeText(int resId, int textId, int duration) {
        Toast result = new Toast(mContext);
        // 获取LayoutInflater对象
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 由layout文件创建一个View对象
        View layout = inflater.inflate(R.layout.view_my_toast, null);

        // 实例化ImageView和TextView对象
        ImageView imageView = (ImageView) layout.findViewById(R.id.ImageView);
        TextView textView = (TextView) layout.findViewById(R.id.message);

        //这里我为了给大家展示就使用这个方面既能显示无图也能显示带图的toast
        if (resId == 0) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(resId);
        }
        textView.setText(textId);
        result.setView(layout);
        result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        result.setDuration(duration);

        return result;
    }

    /**
     * show toast
     * 强制使用ID 方便维护
     *
     * @paramid
     */
    public void show(int id) {
        show(0, id, 100);
    }

    /**
     * show toast
     *
     * @param drawableId
     * @param id
     */
    public void show(int drawableId, int id) {
        mToast = mMyToastView.makeText(drawableId, id, 100);
        mToast.show();
        mId = id;
        show(drawableId, id, 100);
    }

    /**
     * show toast
     *
     * @param drawableId
     * @param id
     * @param duration
     */
    public void show(int drawableId, int id, int duration) {
        if (mToast != null && !(mId == id)) {
            mToast.cancel();
        }
        mToast = mMyToastView.makeText(drawableId, id, duration);
        mToast.show();
        mId = id;
    }
}
