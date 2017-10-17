package com.tools.view;

import android.app.ProgressDialog;
import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * 加载视图
 * 使用系统的ProgressDialog
 */
public class MyProgressDialog {
    private Context mContext;
    private ProgressDialog progress;
    private WeakReference<Context> contextReference;

    public MyProgressDialog(Context context) {
        contextReference = new WeakReference<>(context);
        mContext = contextReference.get();
    }

    /**
     * 实现了弱引用，不会造成内存泄漏。
     *
     * @param id
     */
    public void showProgress(int id) {
        showProgress();
        setMessage(mContext.getResources().getString(id));
    }

    /**
     * 通过setMessage和showProgress，可以实现进度条的显示 在按后退键的时候消失且不会再弹对话框。
     *
     * @param message
     */
    public void setMessage(String message) {
        progress.setMessage(message);
    }

    public void showProgress() {
        if (progress == null) {
            progress = new ProgressDialog(mContext);
            progress.setCanceledOnTouchOutside(false);
        }
        if (!isShowing()) {
            progress.show();
        }
    }


    public void dismiss() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
            progress = null;
        }
    }

    public boolean isShowing() {
        return progress.isShowing();
    }
}
