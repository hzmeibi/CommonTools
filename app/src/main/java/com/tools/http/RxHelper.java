package com.tools.http;

import android.text.TextUtils;

import com.tools.base.CommonActivity;
import com.tools.base.MyApp;
import com.tools.model.CommonBaseModel;
import com.tools.utils.FileUtil;
import com.tools.utils.JsonUtil;
import com.tools.utils.LogUtil;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.io.File;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;


/**
 * Created by milo on 16/10/27.
 * 统一设置Observable / subscription
 */
public class RxHelper {
    private static final String TAG = "RxHelper";

    /**
     * set Observable
     *
     * @param context
     * @return
     */
    public static <T> Observable.Transformer<T, T> setObservable(final CommonActivity context) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                Observable<T> tObservable1 = (Observable<T>) tObservable
//                        .subscribeOn(Schedulers.io())
//                        .doOnSubscribe(new Action0() {
//                            @Override
//                            public void call() {
////                                LogUtil.v(TAG, "call");
//
//                            }
//                        })
//                        .subscribeOn(AndroidSchedulers.mainThread())
//                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxLifecycle.bindUntilEvent(context.lifecycle(), ActivityEvent.STOP));
                return tObservable1;
            }
        };
    }

    public static Observable.Transformer bindRxLifecycle(final CommonActivity context) {
        return new Observable.Transformer() {
            @Override
            public Object call(Object o) {
                return ((Observable) o).compose(RxLifecycle.bindUntilEvent(context.lifecycle(), ActivityEvent.STOP));
            }
        };
    }


    /**
     * set subscription
     *
     * @param subscription
     * @param mHttpCallback
     */
    public static void setSubscriptionToString(final CommonActivity activity, final String url, Observable<CommonBaseModel> subscription, final Class mTClass, final HttpCallback mHttpCallback) {
        subscription.subscribeOn(MyApp.getInstance().defaultSubscribeScheduler())//指定耗时进程
                .observeOn(AndroidSchedulers.mainThread())//指定最后观察者在主线程
                .subscribe(new Subscriber<CommonBaseModel>() {

                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError" + e.toString());
                        //失败返回对应的错误码
                        mHttpCallback.onFailure(0);
                    }

                    @Override
                    public void onNext(CommonBaseModel baseModel) {
                        if (baseModel == null) {
                            LogUtil.e(TAG, "onNext baseMode == null");
                            return;
                        }
                        LogUtil.e(TAG, JsonUtil.toJsonString(baseModel));
                        String state = baseModel.getCode();
                        //需要先判断状态值 具体情况根据实际项目
                        if (!TextUtils.isEmpty(state) && state.equals("200")) {
                            //请求成功  data可能为空
                            if (baseModel.getData() != null) {
                                String data = baseModel.getData().toString();
                                //成功 根据返回数据类型 解析对应数据  对象 集合
                                int type = JsonUtil.getJSONType(data);
                                if (type == 0) {
                                    //格式不对
                                    LogUtil.e(TAG, "Json 格式不对");
                                    mHttpCallback.onFailure(0);
                                } else if (type == 1) {
                                    LogUtil.json(data);
                                    //对象 对于字段不多或者单个值，直接返回
                                    if (mTClass == null) {
                                        mHttpCallback.onSuccess(data);
                                    } else {
                                        //反转实体类返回
                                        mHttpCallback.onSuccess(JsonUtil.parseObject(data, mTClass));
                                    }
                                } else if (type == 2) {
                                    //集合
                                    mHttpCallback.onSuccess(JsonUtil.parseArray(data, mTClass));
                                }
                                //存入缓存
//                                AppCacheUtil.get(MyApp.getInstance()).put(url, data);
                            } else {
                                //无参直接返回
                                mHttpCallback.onSuccess();
                            }

                        } else {
                            String msg = baseModel.getMsg();
                            LogUtil.e(TAG, msg);
                            //失败
                            mHttpCallback.onFailure(Integer.valueOf(state));//返回处理错误码
                            mHttpCallback.onFailure(msg);//返回错误提示信息 直接提示
                        }
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        LogUtil.i(TAG, "onStart: " + url);
                        mHttpCallback.onStart();
                    }
                });
    }

    /**
     * subscription
     * 文件下载
     * <p>
     * 文件下载在IO完成、将流保存在本地文件也需要在IO完成
     *
     * @param subscription
     */
    public static void setSubscriptionToByte(final Observable<ResponseBody> subscription, final File file, final HttpCallback mHttpCallback) {
        subscription.subscribeOn(MyApp.getInstance().defaultSubscribeScheduler())//指定IO线程
                .observeOn(AndroidSchedulers.mainThread())//指定最后观察者在IO线程
                .subscribe(new Subscriber<ResponseBody>() {

                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError" + e.toString());
                        //失败返回对应的错误码
                        mHttpCallback.onFailure(0);
                    }

                    @Override
                    public void onNext(ResponseBody body) {
                        if (body == null) {
                            LogUtil.e(TAG, "onNext body == null");
                            return;
                        }
                        LogUtil.i(TAG, "onNext" + body.toString());
                        try {
                            //返回字节
                            mHttpCallback.onSuccess(FileUtil.readStreamToBytes(body.byteStream(), file));
//                            mHttpCallback.onSuccess(body);
                        } catch (Exception e) {
                            e.printStackTrace();
                            mHttpCallback.onFailure(0);
                        }
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        LogUtil.i(TAG, "onStart");
                        mHttpCallback.onStart();
                    }
                });
    }
}
