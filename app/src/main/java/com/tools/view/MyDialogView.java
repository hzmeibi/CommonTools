package com.tools.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tools.R;

/**
 * 简单弹窗 仅适用于简单布局
 * TextView ImageView Button
 * 复杂布局 可以通过接口回调 自定义处理
 */

public class MyDialogView extends DialogFragment {
    private MyDialogView mMyDialogView;
    private Activity mActivity;
    private int mLayoutId;//正在显示的layoutId
    private int curLayoutId;//当前传入的layoutId
    private SparseArray<View> views;
    private CallBackListener mCallBackListener;
    private Dialog dialog;
    private int mGravity = Gravity.CENTER;//默认居底部显示

    public MyDialogView() {
        mMyDialogView = this;
    }

    @SuppressLint("ValidFragment")
    public MyDialogView(Activity activity) {
        mActivity = activity;
        mMyDialogView = this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (dialog == null || curLayoutId != mLayoutId) {
            mLayoutId = curLayoutId;
            // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
            dialog = new Dialog(getActivity(), R.style.MyDialogTheme);
            dialog.setContentView(mLayoutId);
            dialog.setCanceledOnTouchOutside(true); // 外部点击取消
            // 设置宽度为屏宽, 靠近屏幕底部。
            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = mGravity; // 紧贴底部
            lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
            window.setAttributes(lp);
        }
        mCallBackListener.start(mMyDialogView, dialog);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (views != null) {
            views.clear();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }

    /**
     * 显示弹窗
     *
     * @param layoutId         布局ID
     * @param gravity          布局ID
     * @param CallBackListener 回调
     * @return
     */
    public MyDialogView showDialog(int layoutId, int gravity, CallBackListener CallBackListener) {
        curLayoutId = layoutId;
        mGravity = gravity;
        mCallBackListener = CallBackListener;
        mMyDialogView.setCancelable(true);
        mMyDialogView.show(mActivity.getFragmentManager(), "");
        return mMyDialogView;
    }

    /**
     * 关闭弹窗
     */
    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 设置回调接口
     * 1.view 初始化
     * 2.时间监听
     */
    public interface CallBackListener {
        void start(MyDialogView myDialogView, Dialog dialog);//监听返回视图创建完成

        void onClick(int id);//监听返回点击事件
    }


    /**
     * 设置文字
     * TextView Button
     *
     * @param viewId
     * @param strId
     * @return
     */
    public MyDialogView setText(int viewId, int strId) {
        TextView view = this.getItemView(viewId);
        view.setText(strId);
        return this;
    }

    /**
     * 设置文字颜色
     * TextView Button
     * 注意 colorId 必须为 getResources().getColor(colorId)
     *
     * @param viewId
     * @param colorId
     * @return
     */
    public MyDialogView setTextColor(int viewId, int colorId) {
        TextView view = this.getItemView(viewId);
        view.setTextColor(colorId);
        return this;
    }


    /**
     * 设置背景
     * TextView Button
     *
     * @param viewId
     * @param resId
     * @return
     */
    public MyDialogView setResource(int viewId, int resId) {
        View view = this.getItemView(viewId);
        view.setBackgroundResource(resId);
        return this;
    }


    /**
     * 获取子布局
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getItemView(final int viewId) {
        if (views == null) {
            views = new SparseArray();
        }
        View view = views.get(viewId);
        if (view == null) {
            view = dialog.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 添加点击事件
     *
     * @param ids
     * @return
     */
    public MyDialogView setOnClicks(final int... ids) {
        for (final int id : ids) {
            View view = getItemView(id);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallBackListener.onClick(id);
                }
            });
        }
        return this;
    }
}
