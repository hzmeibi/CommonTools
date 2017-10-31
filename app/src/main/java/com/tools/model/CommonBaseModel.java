package com.tools.model;

import java.io.Serializable;

/**
 * Created by milo on 16/10/25.
 * 基类
 */
public class CommonBaseModel<T> implements Serializable {
    private String msg;
    private String status;//200 成功
    private T result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
