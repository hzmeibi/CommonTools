package com.tools.view.webview;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.widget.LinearLayout;

import com.tools.utils.LogUtil;
import com.tools.view.webview.JsBridge.BridgeHandler;
import com.tools.view.webview.JsBridge.BridgeWebView;
import com.tools.view.webview.JsBridge.CallBackFunction;
import com.tools.view.webview.JsBridge.JavaCallHandler;
import com.tools.view.webview.JsBridge.JsHandler;
import com.tools.view.webview.JsBridge.MyCallBack;
import com.tools.view.webview.JsBridge.MyWebChromeClient;
import com.tools.view.webview.JsBridge.MyWebViewClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * MyWebView
 * 1.提供回调： 请求头 、开始、成功、失败、加载进度
 * 2.自定义加载进度 、控制返回键
 * 3.文件上传 建议使用函数调用 直接上传服务器（版本兼容问题）
 * 注意：必须初始化
 * 参考链接： https://github.com/lzyzsd/JsBridge
 */
public class MyWebView extends LinearLayout {
    private BridgeWebView mWebView;
    private MyCallBack mMyCallBack;
    private MyWebViewClient mMyWebViewClient;
    private MyWebChromeClient mMyWebChromeClient;
    private goBackListener mGoBackListener;
    private List<String> mainPages;

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 初始化WebView
     *
     * @param context    上下文
     * @param myCallBack 回调
     */
    public void init(Context context, String url, MyCallBack myCallBack) {
        // 初始化webview
        if (mWebView == null) {
            mWebView = new BridgeWebView(context);
        }
        mMyCallBack = myCallBack;
        //init webiewClient webChromeClient
        setMyWebViewClient();
        setMyWebChromeClient();

        WebSettings webviewSettings = mWebView.getSettings();
        // 判断系统版本是不是5.0或之上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //让系统不屏蔽混合内容和第三方Cookie
            CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);
            webviewSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // 不支持缩放
        webviewSettings.setSupportZoom(false);

        // 自适应屏幕大小
        webviewSettings.setUseWideViewPort(true);
        webviewSettings.setLoadWithOverviewMode(true);

        addView(mWebView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //返回键监听 主界面返回提醒
        mWebView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    //如果当前url是主界面 提示退出程序 并且处理返回键
                    boolean isBack = true;//是否可以返回上一个界面
                    if (mainPages != null && mainPages.size() >= 0) {
                        String curUrl = mWebView.getUrl();
                        LogUtil.i("curUrl: " + curUrl);
                        for (String url : mainPages) {
                            if (curUrl.contains(url)) {
                                isBack = false;
                                break;
                            }
                        }
                    }
                    if (isBack && mWebView.canGoBack()) {
                        mWebView.goBack();
                        mMyCallBack.goBack(false);
                    } else {
                        //连按两次退出app
                        mMyCallBack.goBack(true);
                    }
                }
                return true;
            }
        });
        loadUrl(url);//load
    }


    /**
     * 添加首页url 控制返回
     *
     * @param pages
     */
    public void addMainPage(List<String> pages) {
        mainPages = pages;
    }

    /**
     * 监听是否退出程序
     */
    public interface goBackListener {
        void goBack(boolean isExitApp);
    }

    public BridgeWebView getWebView() {
        return mWebView;
    }

    /**
     * 设置 WebViewClient
     */
    private void setMyWebViewClient() {
        if (mMyCallBack != null) {
            mMyWebViewClient = new MyWebViewClient(mWebView, mMyCallBack);
            mWebView.setWebViewClient(mMyWebViewClient);
        }

    }

    /**
     * 设置 WebChromeClient
     */
    private void setMyWebChromeClient() {
        if (mMyCallBack != null) {
            mMyWebChromeClient = new MyWebChromeClient(mMyCallBack);
            mWebView.setWebChromeClient(mMyWebChromeClient);
        }
    }

    /**
     * get WebViewClient
     *
     * @return
     */
    public MyWebViewClient getMyWebViewClient() {
        return mMyWebViewClient;
    }

    /**
     * get WebChromeClient
     *
     * @return
     */
    public MyWebChromeClient getMyWebChromeClient() {
        return mMyWebChromeClient;
    }

    /**
     * Loads the given URL.
     *
     * @param url the URL of the resource to load
     */
    private void loadUrl(String url) {
        loadUrl(url, null);
    }

    /**
     * Loads the given URL with the specified additional HTTP headers.
     *
     * @param url                   the URL of the resource to load
     * @param additionalHttpHeaders the additional headers to be used in the
     *                              HTTP request for this URL, specified as a map from name to
     *                              value. Note that if this map contains any of the headers
     *                              that are set by default by this WebView, such as those
     *                              controlling caching, accept types or the User-Agent, their
     *                              values may be overriden by this WebView's defaults.
     */
    private void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        loadUrl(url, additionalHttpHeaders, null);
    }

    /**
     * Loads the given URL with the specified additional HTTP headers.
     *
     * @param url                   the URL of the resource to load
     * @param additionalHttpHeaders the additional headers to be used in the
     *                              HTTP request for this URL, specified as a map from name to
     *                              value. Note that if this map contains any of the headers
     *                              that are set by default by this WebView, such as those
     *                              controlling caching, accept types or the User-Agent, their
     *                              values may be overriden by this WebView's defaults.
     * @param returnCallback        the CallBackFunction to be Used call js registerHandler Function,
     *                              rerurn response data.
     */
    private void loadUrl(String url, Map<String, String> additionalHttpHeaders, CallBackFunction returnCallback) {
        mWebView.loadUrl(url, additionalHttpHeaders, returnCallback);
    }

    /**
     * @param handler default handler,handle messages send by js without assigned handler name,
     *                if js message has handler name, it will be handled by named handlers registered by native
     */
    public void setDefaultHandler(BridgeHandler handler) {
        mWebView.setDefaultHandler(handler);
    }

    public void send(String data) {
        mWebView.send(data);
    }

    public void send(String data, CallBackFunction responseCallback) {
        mWebView.send(data, responseCallback);
    }


    /**
     * 注册本地java方法，以供js端调用
     *
     * @param handlerName 方法名称
     * @param handler     回调接口
     */
    public void registerHandler(final String handlerName, final JsHandler handler) {
        mWebView.registerHandler(handlerName, new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                if (handler != null) {
                    handler.OnHandler(handlerName, data, function);
                }
            }
        });

    }

    /**
     * 批量注册本地java方法，以供js端调用
     *
     * @param handlerNames 方法名称数组
     * @param handler      回调接口
     */
    public void registerHandlers(final ArrayList<String> handlerNames, final JsHandler handler) {
        if (handler != null) {
            for (final String handlerName : handlerNames) {
                mWebView.registerHandler(handlerName, new BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {
                        handler.OnHandler(handlerName, data, function);
                    }
                });
            }
        }
    }

    /**
     * 调用js端已经注册好的方法
     *
     * @param handlerName 方法名称
     * @param javaData    本地端传递给js端的参数，json字符串
     * @param handler     回调接口
     */
    public void callHandler(final String handlerName, String javaData, final JavaCallHandler handler) {
        mWebView.callHandler(handlerName, javaData, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                if (handler != null) {
                    handler.OnHandler(handlerName, data);
                }
            }
        });

    }

    /**
     * 批量调用js端已经注册好的方法
     *
     * @param handlerInfos 方法名称与参数的map，方法名为key
     * @param handler      回调接口
     */
    public void callHandler(final Map<String, String> handlerInfos, final JavaCallHandler handler) {
        if (handler != null) {
            for (final Map.Entry<String, String> entry : handlerInfos.entrySet()) {
                mWebView.callHandler(entry.getKey(), entry.getValue(), new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        handler.OnHandler(entry.getKey(), data);
                    }
                });
            }
        }
    }
}
