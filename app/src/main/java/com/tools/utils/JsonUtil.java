package com.tools.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by milo on 15/12/7.
 * 封装json处理类，方便日后维护
 */
public class JsonUtil {

    public static JSONObject parseObject(Object s) {
        return JSON.parseObject(String.valueOf(s));
    }

    public static <T> T parseObject(String str, Class<T> cls) {
        return JSON.parseObject(str, cls);
    }

    public static <T> T parseObject(JSONObject jsonObject, Class<T> cls) {
        return JSON.parseObject(jsonObject.toString(), cls);
    }

    public static <T> List<T> parseArray(String str, Class<T> cls) {
        return JSON.parseArray(str, cls);
    }

    public static JSONObject parseObject(byte[] bytes) {
        return JSON.parseObject(new String(bytes));
    }

    public static String toJsonString(Object object) {
        return JSON.toJSONString(object);
    }

    /***
     * 获取JSON类型
     * 判断规则
     * 判断第一个字母是否为{或[ 如果都不是则不是一个JSON格式的文本
     * <p/>
     * type: 0-不是json格式；1-json object; 2-json array
     *
     * @param str
     * @return
     */
    public static int getJSONType(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        if (str.startsWith("{")) {
            return 1;
        } else if (str.startsWith("[")) {
            return 2;
        } else {
            return 0;
        }
    }
}
