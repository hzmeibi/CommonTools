package com.tools.base;

import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.tools.R;
import com.tools.utils.AppManagerUtil;
import com.tools.utils.PermissionsUtil;
import com.tools.view.MyToastView;

/**
 * Activity 基类
 */
public class BaseActivity extends AppCompatActivity implements PermissionsUtil.PermissionCallbacks {
    //记录退出的时候的两次点击的间隔时间
    private long exitTime = 0;
    private MyToastView mMyToastView;

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
    public void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 1000) {
            showToast(R.string.my_exit_app);
            exitTime = System.currentTimeMillis();
        } else {
            AppManagerUtil.getInstance().killAllActivity();
            Process.killProcess(Process.myPid());
            System.exit(0);//正常退出App
        }
    }

    /**
     * 6.0 运行时权限申请
     *
     * @param requestCode 请求码
     * @return
     */
    public boolean checkPermission(int requestCode) {
        return PermissionsUtil.requestCommonPermission(this, requestCode);
    }

    /**
     * 6.0 运行时权限申请
     *
     * @param requestCode 请求码
     * @param permissions 请求权限
     * @return
     */
    public boolean checkPermission(int requestCode, String... permissions) {
        if (PermissionsUtil.hasPermissions(this, permissions)) {
            return true;
        } else {
            PermissionsUtil.requestPermissions(this, "", requestCode, permissions);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //init permission
        PermissionsUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, String perms) {
        //Granted

    }

    @Override
    public void onPermissionsDenied(int requestCode, String perms, String never) {
        //Denied

    }
}
