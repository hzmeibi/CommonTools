package com.tools.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import java.util.List;

import rx.Scheduler;
import rx.schedulers.Schedulers;


/**
 * Created by milo on 16/10/25.
 */
public class MyApp extends Application {
    public static MyApp mContext;
    private Scheduler defaultSubscribeScheduler;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static MyApp getInstance() {
        return mContext;
    }

    public Scheduler defaultSubscribeScheduler() {
        //复用
        if (defaultSubscribeScheduler == null) {
            defaultSubscribeScheduler = Schedulers.io();
        }
        return defaultSubscribeScheduler;
    }

    //User to change scheduler from tests
    public void setDefaultSubscribeScheduler(Scheduler scheduler) {
        this.defaultSubscribeScheduler = scheduler;
    }

    /***
     * 判断服务是否开启
     *
     * @param mContext
     * @param className
     * @return
     */
    public boolean isServiceRunning(Context mContext, String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}