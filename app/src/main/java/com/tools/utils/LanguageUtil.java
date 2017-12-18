package com.tools.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by milo on 2017/12/13.
 * LanguageUtil
 */

public class LanguageUtil {

    /**
     * 获取当前语言
     *
     * @return
     */
    public static String getCurLanguage() {
        String result = Locale.getDefault().getLanguage();
        String country = Locale.getDefault().getCountry();
        return country + "-" + result;
    }


    /**
     * 设置语言
     *
     * @param context  上下文
     * @param language 语言
     */
    public static void setLanguage(Context context, String language) {
        Configuration configuration = context.getResources().getConfiguration();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        if (language.contains("en")) {
            configuration.locale = Locale.ENGLISH;

        } else if (language.contains("ja")) {
            configuration.locale = Locale.JAPANESE;
        } else {
            configuration.locale = Locale.CHINESE;
        }
        context.getResources().updateConfiguration(configuration, displayMetrics);
    }
}
