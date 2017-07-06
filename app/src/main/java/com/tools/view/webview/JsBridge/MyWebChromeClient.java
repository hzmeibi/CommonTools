package com.tools.view.webview.JsBridge;

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
}
