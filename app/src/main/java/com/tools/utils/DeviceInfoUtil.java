package com.tools.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

/**
 * Created by milo on 16/5/9.
 */
public class DeviceInfoUtil {

    /**
     * 获取手机的IMEI--手机唯一标示
     *
     * @return
     */
    public static String getDeviceImei(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获取手机的型号
     *
     * @return
     */
    public String getDeviceModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机系统的版本
     *
     * @return
     */
    public String getDeviceVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * SDK
     *
     * @return
     */
    public int getDeviceSDK() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * CODENAME
     *
     * @return
     */
    public String getDeviceCodeName() {
        return android.os.Build.VERSION.CODENAME;
    }

    public String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }
}
