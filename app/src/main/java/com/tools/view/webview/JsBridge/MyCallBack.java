package com.tools.view.webview.JsBridge;

import android.webkit.WebView;

import java.util.Map;

/**
 * 提供回调
 * 加载状态  开始、成功、失败 回退
 */
public abstract class MyCallBack {
    /**
     * shouldOverrideUrlLoading
     *
     * @param view
     * @param url
     * @return
     */
    public abstract boolean shouldOverrideUrlLoading(WebView view, String url);

    /**
     * onPageHeaders
     *
     * @param url url
     * @return HttpHeaders
     */
    public abstract Map<String, String> onPageHeaders(String url);

    /**
     * start
     *
     * @param view WebView
     * @param url  url
     * @return
     */
    public abstract void start(WebView view, String url);

    /**
     * progress
     *
     * @param newProgress progress
     */
    public abstract void onProgressChanged(int newProgress);

    /**
     * finish
     *
     * @param view WebView
     * @param url  url
     */
    public abstract void finish(WebView view, String url);

    /**
     * onPageError
     *
     * @param url url
     * @return errorUrl
     */
    public abstract String onPageError(String url);

    /**
     * goBack
     *
     * @param isExitApp
     */
    public abstract void goBack(boolean isExitApp);
}
