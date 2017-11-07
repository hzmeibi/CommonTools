package com.tools.base;

import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;

import com.tools.utils.AppManagerUtil;
import com.tools.view.MyProgressDialog;
import com.tools.view.MyToastView;
import com.trello.rxlifecycle.components.support.RxFragmentActivity;

/**
 * Activity 基类
 * 1.show Progress
 * 2.show Toast
 * 3.exitApp
 * 4.check Permission
 */
public class CommonActivity extends RxFragmentActivity {
    //记录退出的时候的两次点击的间隔时间
    private long exitTime = 0;
    private MyToastView mMyToastView;
    private MyProgressDialog mMyProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManagerUtil.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManagerUtil.getInstance().killActivity(this);
    }

    /**
     * show progress
     *
     * @param message
     */
    public void showProgress(int message) {
        if (mMyProgressDialog == null) {
            mMyProgressDialog = new MyProgressDialog(this);
        }
        mMyProgressDialog.showProgress(message);
    }

    /**
     * dismiss progress
     */
    public void dismissProgress() {
        mMyProgressDialog.dismiss();
    }

    /**
     * show Toast
     *
     * @param message
     */
    public void showToast(int message) {
        if (mMyToastView == null) {
            mMyToastView = new MyToastView(this);
        }
        mMyToastView.show(message);
    }

    /**
     * 双击退出app
     */
    public void exitApp(int id) {
        if ((System.currentTimeMillis() - exitTime) > 1000) {
            showToast(id);
            exitTime = System.currentTimeMillis();
        } else {
            AppManagerUtil.getInstance().killAllActivity();
            Process.killProcess(Process.myPid());
            System.exit(0);//正常退出App
        }
    }
}
