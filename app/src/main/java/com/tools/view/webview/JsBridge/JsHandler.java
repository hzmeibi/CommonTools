package com.tools.view.webview.JsBridge;


/**
 * Class description
 *
 * @author YEZHENNAN220
 * @date 2016-07-08 16:24
 */
public interface JsHandler {

    public void OnHandler(String handlerName, String responseData, CallBackFunction function);

}
