package com.tools.http;


import com.tools.base.CommonActivity;
import com.tools.base.MyApp;
import com.tools.model.CommonBaseModel;
import com.tools.utils.AppCacheUtil;
import com.tools.utils.JsonUtil;
import com.tools.utils.LogUtil;

import java.io.File;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;


/**
 * Created by milo on 16/10/26.
 * Http 封装回调类
 * get             有参无参
 * post            有参无参
 * upload          有参
 * download binary 有参无参
 * <p/>
 * 添加文件缓存：
 * 1.从缓存取,有则返回缓存数据，没有则从服务器获取;
 * 2.从服务器获取,有网络直接获取更新数据，无网络提示不刷新数据
 */
public class HttpUtil {
    private static final String TAG = "HttpUtil";
    private volatile static HttpRequest mHttpRequest;
    private static boolean isCache = false;
    private static Observable<CommonBaseModel> subscription;

    /**
     * 获取Http请求
     *
     * @return
     */
    private static HttpRequest getHttpRequest() {
        if (mHttpRequest == null) {
            synchronized (HttpUtil.class) {
                if (mHttpRequest == null) {
                    mHttpRequest = RetrofitManager.getRetrofit().create(HttpRequest.class);
                }
            }
        }
        return mHttpRequest;
    }

    /**
     * 获取缓存成功,直接返回数据
     *
     * @param tClass       解析对象
     * @param httpCallback 回调接口
     */
    public static boolean getCacheSuccess(CommonActivity activity, String url, Class tClass, HttpCallback httpCallback) {
        boolean isSuccess = false;//获取缓存是否成功
        if (isCache) {
            String cache = AppCacheUtil.get(MyApp.getInstance()).getAsString(url);
            //成功 根据返回数据类型 解析对应数据  对象 集合
            LogUtil.v(TAG, "getLocalCache: " + cache);
            LogUtil.json(TAG, String.valueOf(cache));
            //根据类型返回对应数据
            int type = JsonUtil.getJSONType(cache);
            if (type == 0) {
                //格式不对
                LogUtil.e(TAG, "Json 格式不对");
                httpCallback.onFailure(0);
            } else if (type == 1) {
                //对象 对于字段不多或者单个值，直接返回
                LogUtil.json(TAG, cache);
                if (tClass == null) {
                    httpCallback.onSuccess(cache);
                } else {
                    //反转实体类返回
                    httpCallback.onSuccess(JsonUtil.parseObject(cache, tClass));
                }
                isSuccess = true;
            } else if (type == 2) {
                //集合
                LogUtil.json(TAG, cache);
                httpCallback.onSuccess(JsonUtil.parseArray(cache, tClass));
                isSuccess = true;
            }
        }
        return isSuccess;
    }


    /**
     * get
     *
     * @param activity     上下文
     * @param url          网址
     * @param tClass       解析对象
     * @param httpCallback 回调接口
     */
    public static void getCallback(CommonActivity activity, String url, Class tClass, HttpCallback httpCallback) {
        //判断是否需要从缓存中取数据
        if (getCacheSuccess(activity, url, tClass, httpCallback)) {
            return;
        }
        subscription = getHttpRequest()
                .get(url)
                .compose(RxHelper.<CommonBaseModel>setObservable(activity));
        RxHelper.setSubscriptionToString(activity, url, subscription, tClass, httpCallback);
    }

    /**
     * get
     *
     * @param activity     上下文
     * @param url          网址
     * @param headers      请求头
     * @param tClass       解析对象
     * @param httpCallback 回调接口
     */
    public static void getCallback(CommonActivity activity, String url, Map<String, String> headers, Class tClass, HttpCallback httpCallback) {
        //判断是否需要从缓存中取数据
        if (getCacheSuccess(activity, url, tClass, httpCallback)) {
            return;
        }
        subscription = getHttpRequest()
                .get1(url, headers)
                .compose(RxHelper.<CommonBaseModel>setObservable(activity));
        RxHelper.setSubscriptionToString(activity, url, subscription, tClass, httpCallback);
    }

    /**
     * get
     *
     * @param activity     上下文
     * @param url          网址
     * @param tClass       解析对象
     * @param params       请求参数
     * @param httpCallback 回调接口
     */
    public static void getCallback(final CommonActivity activity, String url, Class tClass, Map<String, Object> params, HttpCallback httpCallback) {
        //判断是否需要从缓存中取数据
        if (getCacheSuccess(activity, url, tClass, httpCallback)) {
            return;
        }
        subscription = getHttpRequest()
                .get(url, params)
                .compose(RxHelper.<CommonBaseModel>setObservable(activity));
        RxHelper.setSubscriptionToString(activity, url, subscription, tClass, httpCallback);
    }

    /**
     * get
     *
     * @param activity     上下文
     * @param url          网址
     * @param headers      请求头
     * @param tClass       解析对象
     * @param params       请求参数
     * @param httpCallback 回调接口
     */
    public static void getCallback(final CommonActivity activity, String url, Map<String, String> headers, Class tClass, Map<String, Object> params, HttpCallback httpCallback) {
        //判断是否需要从缓存中取数据
        if (getCacheSuccess(activity, url, tClass, httpCallback)) {
            return;
        }
        subscription = getHttpRequest()
                .get1(url, headers, params)
                .compose(RxHelper.<CommonBaseModel>setObservable(activity));
        RxHelper.setSubscriptionToString(activity, url, subscription, tClass, httpCallback);
    }


    /**
     * post
     *
     * @param activity     上下文
     * @param url          网址
     * @param tClass       解析对象
     * @param httpCallback 回调接口
     */
    public static void postCallback(CommonActivity activity, String url, Class tClass, HttpCallback httpCallback) {
        //判断是否需要从缓存中取数据
        if (getCacheSuccess(activity, url, tClass, httpCallback)) {
            return;
        }
        subscription = getHttpRequest()
                .post(url)
                .compose(RxHelper.<CommonBaseModel>setObservable(activity));
        RxHelper.setSubscriptionToString(activity, url, subscription, tClass, httpCallback);
    }

    /**
     * post
     *
     * @param activity     上下文
     * @param url          网址
     * @param headers      请求头
     * @param tClass       解析对象
     * @param httpCallback 回调接口
     */
    public static void postCallback(CommonActivity activity, String url, Map<String, String> headers, Class tClass, HttpCallback httpCallback) {
        //判断是否需要从缓存中取数据
        if (getCacheSuccess(activity, url, tClass, httpCallback)) {
            return;
        }
        subscription = getHttpRequest()
                .post1(url, headers)
                .compose(RxHelper.<CommonBaseModel>setObservable(activity));
        RxHelper.setSubscriptionToString(activity, url, subscription, tClass, httpCallback);
    }

    /**
     * post
     *
     * @param activity     上下文
     * @param url          网址
     * @param tClass       解析对象
     * @param params       请求参数
     * @param httpCallback 回调接口
     */
    public static void postCallback(final CommonActivity activity, String url, Class tClass, Map<String, Object> params, HttpCallback httpCallback) {
        //判断是否需要从缓存中取数据
        if (getCacheSuccess(activity, url, tClass, httpCallback)) {
            return;
        }
        subscription = getHttpRequest()
                .post(url, params)
                .compose(RxHelper.<CommonBaseModel>setObservable(activity));
        RxHelper.setSubscriptionToString(activity, url, subscription, tClass, httpCallback);
    }


    /**
     * post
     *
     * @param activity     上下文
     * @param url          网址
     * @param headers      请求头
     * @param tClass       解析对象
     * @param params       请求参数
     * @param httpCallback 回调接口
     */
    public static void postCallback(final CommonActivity activity, String url, Map<String, String> headers, Class tClass, Map<String, Object> params, HttpCallback httpCallback) {
        //判断是否需要从缓存中取数据
        if (getCacheSuccess(activity, url, tClass, httpCallback)) {
            return;
        }
        subscription = getHttpRequest()
                .post1(url, headers, params)
                .compose(RxHelper.<CommonBaseModel>setObservable(activity));
        RxHelper.setSubscriptionToString(activity, url, subscription, tClass, httpCallback);
    }


    /**
     * upload
     *
     * @param activity     上下文
     * @param url          网址
     * @param tClass       解析对象
     * @param params       请求参数
     * @param httpCallback 回调接口
     */
    public static void upload(final CommonActivity activity, String url, Class tClass, Map<String, RequestBody> params, HttpCallback httpCallback) {
        subscription = getHttpRequest()
                .upload(url, params)
                .compose(RxHelper.<CommonBaseModel>setObservable(activity));
        RxHelper.setSubscriptionToString(activity, url, subscription, tClass, httpCallback);
    }


    /**
     * upload
     *
     * @param activity     上下文
     * @param url          网址
     * @param headers      请求头
     * @param tClass       解析对象
     * @param params       请求参数
     * @param httpCallback 回调接口
     */
    public static void upload(final CommonActivity activity, String url, Map<String, String> headers, Class tClass, Map<String, RequestBody> params, HttpCallback httpCallback) {
        subscription = getHttpRequest()
                .upload1(url, headers, params)
                .compose(RxHelper.<CommonBaseModel>setObservable(activity));
        RxHelper.setSubscriptionToString(activity, url, subscription, tClass, httpCallback);
    }

    /***
     * download binary
     *
     * @param activity     上下文
     * @param url          网址
     * @param file         文件
     * @param httpCallback 回调接口
     */
    public static void download(CommonActivity activity, String url, File file, HttpCallback httpCallback) {
        Observable<ResponseBody> subscription = getHttpRequest()
                .download(url)
                .compose(RxHelper.<ResponseBody>setObservable(activity));
        RxHelper.setSubscriptionToByte(subscription, file, httpCallback);
    }

    /**
     * download binary
     *
     * @param activity     上下文
     * @param url          网址
     * @param headers      请求头
     * @param file         保存文件
     * @param httpCallback 回调接口
     */
    public static void download(CommonActivity activity, String url, Map<String, String> headers, File file, HttpCallback httpCallback) {
        Observable<ResponseBody> subscription = getHttpRequest()
                .download1(url, headers)
                .compose(RxHelper.<ResponseBody>setObservable(activity));
        RxHelper.setSubscriptionToByte(subscription, file, httpCallback);
    }

    /**
     * download binary
     *
     * @param activity     上下文
     * @param url          网址
     * @param file         文件
     * @param params       请求参数
     * @param httpCallback 回调接口
     */
    public static void download(final CommonActivity activity, String url, File file, Map<String, Object> params, HttpCallback httpCallback) {
        Observable<ResponseBody> subscription = getHttpRequest()
                .download(url, params)
                .compose(RxHelper.<ResponseBody>setObservable(activity));
        RxHelper.setSubscriptionToByte(subscription, file, httpCallback);
    }


    /**
     * download binary
     *
     * @param activity     上下文
     * @param url          网址
     * @param headers      请求头
     * @param file         文件
     * @param params       请求参数
     * @param httpCallback 回调接口
     */
    public static void download(final CommonActivity activity, String url, Map<String, String> headers, File file, Map<String, Object> params, HttpCallback httpCallback) {
        Observable<ResponseBody> subscription = getHttpRequest()
                .download1(url, headers, params)
                .compose(RxHelper.<ResponseBody>setObservable(activity));
        RxHelper.setSubscriptionToByte(subscription, file, httpCallback);
    }
}
