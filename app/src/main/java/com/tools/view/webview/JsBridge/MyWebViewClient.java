package com.tools.view.webview.JsBridge;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
        if (mMyCallBack.shouldOverrideUrlLoading(view, url)) {
            //已经重载被
            return true;
        } else {
            //部分h5的url、需要重新加载
            if (url.startsWith("weixin://wap/pay?")) {
                //支持h5微信支付 跳转微信支付
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
                return true;
            } else if (url.startsWith("tel:")) {
                //拨打电话
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
                return true;
            } else {
                if (mMyCallBack.onPageHeaders(url) != null) {
                    view.loadUrl(url, mMyCallBack.onPageHeaders(url));
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        }
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
}
