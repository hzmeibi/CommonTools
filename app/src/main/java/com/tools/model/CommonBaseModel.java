package com.tools.model;

import java.io.Serializable;

/**
 * Created by milo on 16/10/25.
 * 基类
 */
public class CommonBaseModel<T> implements Serializable {
    private String msg;
    private String code;//200 成功
    private T data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
