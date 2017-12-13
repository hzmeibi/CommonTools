package com.tools.http;

/**
 * Created by milo on 16/10/25.
 * http回调
 */
public abstract class HttpCallback implements HttpInterface {
    @Override
    public void onStart() {
    }

    @Override
    public void onProgress(long curSize, long totalSize) {

    }

    @Override
    public void onSuccess(Object o) {
    }


    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(int status) {

    }

    @Override
    public void onFailure(String msg) {

    }
}
