package com.tools.view.webview.JsBridge;

import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * 设置Progress
 */
public class MyWebChromeClient extends WebChromeClient {
    private MyCallBack mMyCallBack;

    public MyWebChromeClient(MyCallBack callBack) {
        super();
        mMyCallBack = callBack;
    }


    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        mMyCallBack.onProgressChanged(newProgress);
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        //webview定位相关设置 部分手机定位无效
        callback.invoke(origin, true, false);
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }
}
