package com.tools.view.webview.JsBridge;

import android.graphics.Bitmap;
import android.webkit.WebView;

/**
 * 监听url 加载状态  开始、成功、失败
 */

public class MyWebViewClient extends BridgeWebViewClient {
    private MyCallBack mMyCallBack;

    public MyWebViewClient(BridgeWebView webView, MyCallBack callBack) {
        super(webView);
        mMyCallBack = callBack;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (mMyCallBack.onPageHeaders(url) != null) {
            view.loadUrl(url, mMyCallBack.onPageHeaders(url));
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        mMyCallBack.start(view, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        mMyCallBack.finish(view, url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        view.loadUrl(mMyCallBack.onPageError(failingUrl));
    }


//    /**
//     * onPageHeaders
//     *
//     * @param url url
//     * @return HttpHeaders
//     */
//    public abstract Map<String, String> onPageHeaders(String url);
//
//    /**
//     * start
//     *
//     * @param view WebView
//     * @param url  url
//     * @return
//     */
//    public abstract void start(WebView view, String url);
//
//    /**
//     * finish
//     *
//     * @param view WebView
//     * @param url  url
//     */
//    public abstract void finish(WebView view, String url);
//
//    /**
//     * onPageError
//     *
//     * @param url url
//     * @return errorUrl
//     */
//    public abstract String onPageError(String url);
}
