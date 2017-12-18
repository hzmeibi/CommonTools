package com.tools.base;

import com.tools.view.MyProgressDialog;
import com.tools.view.MyToastView;
import com.trello.rxlifecycle.components.support.RxFragment;

public class CommonFragment extends RxFragment {
    private MyToastView mMyToastView;
    private MyProgressDialog mMyProgressDialog;

    /**
     * show progress
     *
     * @param message
     */
    public void showProgress(int message) {
        if (mMyProgressDialog == null) {
            mMyProgressDialog = new MyProgressDialog(getContext());
        }
        mMyProgressDialog.showProgress(message);
    }

    /**
     * show progress
     *
     * @param message
     */
    public void showProgress(String message) {
        if (mMyProgressDialog == null) {
            mMyProgressDialog = new MyProgressDialog(getContext());
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
            mMyToastView = new MyToastView(getContext());
        }
        mMyToastView.show(message);
    }

    /**
     * show Toast
     *
     * @param message
     */
    public void showToast(String message) {
        if (mMyToastView == null) {
            mMyToastView = new MyToastView(getContext());
        }
        mMyToastView.show(message);
    }

}
