package com.tools.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.tools.base.CommonActivity;

import java.util.ArrayList;

/**
 * 权限管理
 * https://github.com/googlesamples/easypermissions
 * http://blog.csdn.net/qq_25804863/article/details/53517129
 * 使用方法参考{@link CommonActivity }
 */
public class PermissionsUtil {
    //常用权限申请
    public static final int REQUEST_CODE_CAMERA = 1000;//相机
    public static final int REQUEST_CODE_STORAGE = 1001;//存储卡
    public static final int REQUEST_CODE_CONTACTS = 1002;//联系人
    public static final int REQUEST_CODE_LOCATION = 1003;//位置
    public static final int REQUEST_CODE_SMS = 1004;//短信
    public static final int REQUEST_CODE_SENSORS = 1005;//传感器
    public static final int REQUEST_CODE_PHONE = 1006;//手机
    public static final int REQUEST_CODE_MICROPHONE = 1007;//麦克风
    public static final int REQUEST_CODE_CALENDAR = 1008;//日历

    /**
     * 申请常用权限
     *
     * @param context     上下文
     * @param requestCode 请求码
     * @return
     */
    public static boolean requestCommonPermission(Context context, int requestCode) {
        String[] permissions = new String[0];
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                permissions = new String[]{Manifest.permission.CAMERA};

                break;
            case REQUEST_CODE_STORAGE:
                permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                break;
            case REQUEST_CODE_CONTACTS:
                permissions = new String[]{Manifest.permission.READ_CONTACTS};
                break;
            case REQUEST_CODE_LOCATION:
                permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
                break;
            case REQUEST_CODE_SMS:
                permissions = new String[]{Manifest.permission.READ_SMS};
                break;
            case REQUEST_CODE_SENSORS:
                permissions = new String[]{Manifest.permission.BODY_SENSORS};
                break;
            case REQUEST_CODE_PHONE:
                permissions = new String[]{Manifest.permission.CALL_PHONE};
                break;
            case REQUEST_CODE_MICROPHONE:
                permissions = new String[]{Manifest.permission.RECORD_AUDIO};
                break;
            case REQUEST_CODE_CALENDAR:
                permissions = new String[]{Manifest.permission.READ_CALENDAR};
                break;
        }

        if (hasPermissions(context, permissions)) {
            return true;
        } else {
            requestPermissions(context, "", requestCode, permissions);
            return false;
        }
    }


    public interface PermissionCallbacks extends ActivityCompat.OnRequestPermissionsResultCallback {

        void onPermissionsGranted(int requestCode, String perms);//同意回调

        void onPermissionsDenied(int requestCode, String perms, String never);//拒绝回调
    }

    /**
     * Check if the calling context has a set of permissions.
     *
     * @param context the calling context.
     * @param perms   one ore more permissions, such as {@code android.Manifest.permission.CAMERA}.
     * @return true if all permissions are already granted, false if at least one permission
     * is not yet granted.
     */
    public static boolean hasPermissions(Context context, String... perms) {
        // Always return true for SDK < M, let the system deal with the permissions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LogUtil.i("hasPermissions: API version < M, returning true by default");
            return true;
        }

        for (String perm : perms) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED);
            if (!hasPerm) {
                return false;
            }
        }

        return true;
    }

    /**
     * Request a set of permissions, showing rationale if the system requests it.
     *
     * @param object      Activity or Fragment requesting permissions. Should implement
     * @param rationale   a message explaining why the application needs this set of permissions, will
     *                    be displayed if the user rejects the request the first time.
     * @param requestCode request code to track this request, must be < 256.
     * @param perms       a set of permissions to be requested.
     */
    public static void requestPermissions(Object object, String rationale, int requestCode, String... perms) {
        requestPermissions(object, rationale, android.R.string.ok, requestCode, perms);
    }

    /**
     * Request a set of permissions, showing rationale if the system requests it.
     *
     * @param object              Activity or Fragment requesting permissions. Should implement
     * @param rationale           a message explaining why the application needs this set of permissions, will be displayed if the user rejects the request the first time.
     * @param snackbarActionResId custom text for snackbar action button
     * @param requestCode         request code to track this request, must be < 256.
     * @param perms               a set of permissions to be requested.
     */
    public static void requestPermissions(final Object object, String rationale, int snackbarActionResId,
                                          final int requestCode, final String... perms) {

        checkCallingObjectSuitability(object);
        Activity activity = getActivity(object);
        if (null == activity) {
            return;
        }
        executePermissionsRequest(object, perms, requestCode);

        boolean shouldShowRationale = false;
        for (String perm : perms) {
            shouldShowRationale = shouldShowRationale || shouldShowRequestPermissionRationale(object, perm);
            if (!shouldShowRequestPermissionRationale(object, perm)) {
                ((PermissionCallbacks) object).onPermissionsDenied(requestCode, perm, "never");
            }
        }
        if (shouldShowRationale) {
//            Snackbar.make(activity.findViewById(android.R.id.content), rationale, Snackbar.LENGTH_LONG)
//                    .setAction(snackbarActionResId, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            executePermissionsRequest(object, perms, requestCode);
//                        }
//                    })
//                    .show();
            executePermissionsRequest(object, perms, requestCode);
        } else { //勾选了拒绝且不在提示

        }
    }


    /**
     * Check if at least one permission in the list of denied permissions has been permanently
     * denied (user clicked "Never ask again").
     *
     * @param object            Activity or Fragment requesting permissions.
     * @param deniedPermissions list of denied permissions, usually from
     * @return {@code true} if at least one permission in the list was permanently denied.
     */

    public static boolean somePermissionPermanentlyDenied(Object object, final String... deniedPermissions) {
        for (String deniedPermission : deniedPermissions) {
            if (permissionPermanentlyDenied(object, deniedPermission)) {
                return true;
            }
        }

        return false;
    }


    /**
     * Check if a permission has been permanently denied (user clicked "Never ask again").
     *
     * @param object           Activity or Fragment requesting permissions.
     * @param deniedPermission denied permission.
     * @return {@code true} if the permissions has been permanently denied.
     */
    public static boolean permissionPermanentlyDenied(Object object, String deniedPermission) {
        return !shouldShowRequestPermissionRationale(object, deniedPermission);
    }


    /**
     * Handle the result of a permission request, should be called from the calling Activity's
     *
     * @param requestCode  requestCode argument to permission result callback.
     * @param permissions  permissions argument to permission result callback.
     * @param grantResults grantResults argument to permission result callback.
     * @param receivers    an array of objects that implement {@link PermissionCallbacks}.
     */
    public static void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                  int[] grantResults, Object... receivers) {

        // Make a collection of granted and denied permissions from the request.
        ArrayList<String> granted = new ArrayList<>();
        ArrayList<String> denied = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String perm = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(perm);
            } else {
                denied.add(perm);
            }
        }

        for (String grant : granted) {
            ((PermissionCallbacks) receivers[0]).onPermissionsGranted(requestCode, grant);
        }
        for (String deny : denied) {
            ((PermissionCallbacks) receivers[0]).onPermissionsDenied(requestCode, deny, "");
        }

        // iterate through all receivers
//        for (Object object : receivers) {
//            // Report granted permissions, if any.
//            if (!granted.isEmpty()) {
//                if (object instanceof PermissionCallbacks) {
//                    ((PermissionCallbacks) object).onPermissionsGranted(requestCode, granted);
//                }
//            }
//
//            // Report denied permissions, if any.
//            if (!denied.isEmpty()) {
//                if (object instanceof PermissionCallbacks) {
//                    ((PermissionCallbacks) object).onPermissionsDenied(requestCode, denied);
//                }
//            }
//        }

    }

    /**
     * prompt the user to go to the app's settings screen and enable permissions. If the
     * user clicks snackbar's action, they are sent to the settings screen. The result is returned
     * to the Activity via {@link Activity#onActivityResult(int, int, Intent)}.
     *
     * @param object         Activity or Fragment requesting permissions.
     * @param rationale      a message explaining why the application needs this set of permissions, will
     *                       be displayed on the snackbar.
     * @param snackbarAction text disPlayed on the snackbar's action
     * @param requestCode    If >= 0, this code will be returned in onActivityResult() when the activity exits.
     */
    public static void goSettings2Permissions(final Object object, String rationale, String snackbarAction, final int requestCode) {
        checkCallingObjectSuitability(object);
        final Activity activity = getActivity(object);
        if (null == activity) {
            return;
        }
//        Snackbar.make(activity.findViewById(android.R.id.content), rationale, Snackbar.LENGTH_LONG)
//                .setAction(snackbarAction, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
//                        intent.setData(uri);
//                        startForResult(object, intent, requestCode);
//                    }
//                })
//                .show();
    }

    @TargetApi(23)
    private static boolean shouldShowRequestPermissionRationale(Object object, String perm) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, perm);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else {
            return false;
        }
    }

    @TargetApi(23)
    private static void executePermissionsRequest(Object object, String[] perms, int requestCode) {
        checkCallingObjectSuitability(object);
        if (object instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) object, perms, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(perms, requestCode);
        } else if (object instanceof android.app.Fragment) {
            ((android.app.Fragment) object).requestPermissions(perms, requestCode);
        }
    }

    @TargetApi(11)
    private static Activity getActivity(Object object) {
        if (object instanceof Activity) {
            return ((Activity) object);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).getActivity();
        } else {
            return null;
        }
    }

    @TargetApi(11)
    private static void startForResult(Object object, Intent intent, int requestCode) {
        if (object instanceof Activity) {
            ((Activity) object).startActivityForResult(intent, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).startActivityForResult(intent, requestCode);
        } else if (object instanceof android.app.Fragment) {
            ((android.app.Fragment) object).startActivityForResult(intent, requestCode);
        }
    }

    private static void checkCallingObjectSuitability(Object object) {
        // Make sure Object is an Activity or Fragment
        boolean isActivity = object instanceof Activity;
        boolean isSupportFragment = object instanceof Fragment;
        boolean isAppFragment = object instanceof android.app.Fragment;
        boolean isMinSdkM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
        if (!(isSupportFragment || isActivity || (isAppFragment && isMinSdkM))) {
            if (isAppFragment) {
                throw new IllegalArgumentException(
                        "Target SDK needs to be greater than 23 if caller is android.app.Fragment");
            } else {
                throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
            }
        }
    }
}