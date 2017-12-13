package com.tools.http;


import com.tools.http.fastjson.FastjsonConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by milo on 16/10/25.
 * http管理类
 * 初始化 OkHttp 单例
 */
public class RetrofitManager {
    private volatile static RetrofitManager INSTANCE;
    private static Retrofit retrofit;

    public RetrofitManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        OkHttpClient client = builder
                .connectTimeout(60, TimeUnit.SECONDS)// 连接超时时间设置
                .readTimeout(60, TimeUnit.SECONDS)// 读取超时时间设置
                .retryOnConnectionFailure(false)// 失败重试
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.baidu.com/")//base url 必须要以"/"结束
                .addConverterFactory(FastjsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
    }

    public static Retrofit getRetrofit() {
        if (INSTANCE == null) {
            synchronized (RetrofitManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RetrofitManager();
                }
            }
        }
        return retrofit;
    }

}
