package com.tools.http;

/**
 * Created by milo on 16/10/25.
 */
public interface HttpInterface<T> {

    /**
     * start
     */
    void onStart();

    /**
     * 进度
     *
     * @param curSize   long
     * @param totalSize long
     */
    void onProgress(long curSize, long totalSize);

    /**
     * onSuccess
     *
     * @param t result
     */
    void onSuccess(T t);


    /**
     * onSuccess
     */
    void onSuccess();


    /**
     * 请求失败,返回状态码
     *
     * @param status int
     */
    void onFailure(int status);


    /**
     * 请求失败,返回提示信息
     *
     * @param msg String
     */
    void onFailure(String msg);


}
