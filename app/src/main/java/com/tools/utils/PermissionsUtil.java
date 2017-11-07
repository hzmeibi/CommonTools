package com.tools.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tools.base.CommonActivity;

import java.util.List;

/**
 * 权限管理
 * https://github.com/Karumi/Dexter
 * https://github.com/googlesamples/easypermissions
 * http://blog.csdn.net/qq_25804863/article/details/53517129
 * 使用方法参考{@link CommonActivity }
 */
public class PermissionsUtil {
    private FragmentActivity mContext;
    private String[] permissions;
    private PermissionCallbacks mPermissionCallbacks;
    private AlertDialog mAlertDialog;

    /**
     * 上下文
     *
     * @param activity
     * @return
     */
    public PermissionsUtil withActivity(FragmentActivity activity) {
        mContext = activity;
        return this;
    }

    /**
     * 申明需要授权的权限
     *
     * @param permissions
     * @return
     */
    public PermissionsUtil withPermission(String... permissions) {
        this.permissions = permissions;
        return this;
    }

    /**
     * 回调接口
     *
     * @param permissionCallbacks
     * @return
     */
    public PermissionsUtil withCallBack(PermissionCallbacks permissionCallbacks) {
        mPermissionCallbacks = permissionCallbacks;
        return this;
    }


    /**
     * 检查更新
     */
    public void check() {
        Dexter.withActivity(mContext)
                .withPermissions(permissions)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        //权限是否全部同意
                        if (report.areAllPermissionsGranted()) {
                            //同意则做相应操作
                            mPermissionCallbacks.onPermissionsGranted(PermissionsUtil.this);
                        } else {
                            //没有授权 弹出弹窗
                            mPermissionCallbacks.onPermissionsDenied(PermissionsUtil.this);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        //存在未授权的权限 继续授权
                        if (permissions.size() > 0) {
                            token.continuePermissionRequest();
                        } else {
                            token.cancelPermissionRequest();
                        }
                    }
                }).check();
    }


    /**
     * 显示弹窗
     *
     * @param isCanCancle 是否能够取消
     * @param title       标题
     * @param message     内容
     * @param confirm     确认按钮
     */
    public void showDialog(boolean isCanCancle, int title, int message, int confirm) {
        if (mAlertDialog == null || !mAlertDialog.isShowing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setCancelable(isCanCancle);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton(confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //跳转到设置界面
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                    intent.setData(uri);
                    mContext.startActivity(intent);
                }
            });
            mAlertDialog = builder.create();
            mAlertDialog.show();
        }
    }

    /**
     * 关闭弹窗
     */
    public void dismissDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    /**
     * 接口回调
     */
    public interface PermissionCallbacks {
        void onPermissionsGranted(PermissionsUtil permissionsUtil);//同意回调

        void onPermissionsDenied(PermissionsUtil permissionsUtil);//拒绝回调
    }

}