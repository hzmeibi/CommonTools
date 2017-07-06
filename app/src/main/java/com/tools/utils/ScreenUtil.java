package com.tools.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 屏幕计算
 */
public class ScreenUtil {

    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 不管横屏还是竖屏，获取屏幕的真实宽
     *
     * @param context
     * @return
     */
    public static int getPortraitScreenWidth(Context context) {
        int w = getScreenWidth(context);
        int h = getScreenHeight(context);
        return Math.min(w, h);
    }

    /**
     * 不管横屏还是竖屏，获取屏幕的真实高
     *
     * @param context
     * @return
     */
    public static int getPortraitScreenHeight(Context context) {
        int w = getScreenWidth(context);
        int h = getScreenHeight(context);
        return Math.max(w, h);
    }

    /**
     * 得到设备的密度
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 把密度转换为像素
     */
    public static int dip2px(Context context, float px) {
        final float scale = getScreenDensity(context);
        return (int) (px * scale + 0.5f);
    }

    /**
     * 把像素转换为密度
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = getScreenDensity(context);
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 设置全屏显示
     *
     * @param context
     */
    public static void setFullScreen(Activity context) {
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window myWindow = context.getWindow();
        myWindow.setFlags(flag, flag);// 设置为全屏
    }

    /**
     * 计算状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;

        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    /**
     * 获取屏幕原始尺寸高度，包括虚拟功能键高度
     */
    public static int getDpi(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * 获取 虚拟按键的高度
     *
     * @param context
     * @return
     */
    public static int getNavigationHeight(Context context) {
        return getDpi(context) - getScreenHeight(context);
    }

    /**
     * 标题栏高度
     *
     * @return
     */
    public static int getTitleHeight(Activity activity) {
        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }

    /**
     * 获取控件到父控件的上边距
     *
     * @param myView
     * @return
     */
    public static int getRelativeTop(View myView) {
        return myView.getId() == android.R.id.content ? myView.getTop() : myView.getTop() + getRelativeTop((View) myView.getParent());
    }

    /**
     * 获取控件到父控件的左边距
     *
     * @param myView
     * @return
     */
    public static int getRelativeLeft(View myView) {
        return myView.getId() == android.R.id.content ? myView.getLeft() : myView.getLeft() + getRelativeLeft((View) myView.getParent());
    }

}
