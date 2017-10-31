package com.tools.http;

import com.tools.model.CommonBaseModel;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by milo on 16/10/25.
 * 封装网络请求
 */
public interface HttpRequest {
    /**
     * 异步get请求，无请求参数
     *
     * @param url 请求url 用@Url标注 与 @GET("auth/{path}")标注有区别
     *            如果使用 @GET("auth/path") 传进的路径 “/”符号会被编码成一个其他字符，@Url 标注则不会，
     *            并且如果传入的是一个完整的路径比如http://192.168.8.3:4000/media/b1b983e5cd2cfafefcb9
     *            则不会和retrofit初始化的baseUrl拼接。我们这里请求都传入完整的路径
     */
    @GET
    Observable<CommonBaseModel> get(@Url String url);

    /**
     * 异步get请求，有请求参数
     *
     * @param url    请求url
     * @param params 请求参数 hashmap键值对
     */
    @GET
    Observable<CommonBaseModel> get(@Url String url, @QueryMap Map<String, Object> params);


    /**
     * 异步post请求，无请求参数
     *
     * @param url 请求url
     */
    @POST
    Observable<CommonBaseModel> post(@Url String url);

    /**
     * 异步post请求，有请求参数
     *
     * @param url    请求url
     * @param params 请求参数 hashmap键值对
     */
    @FormUrlEncoded
    @POST
    Observable<CommonBaseModel> post(@Url String url, @FieldMap Map<String, Object> params);

    /**
     * 异步delete请求，有请求参数
     *
     * @param url    请求url
     * @param params 请求参数 hashmap键值对
     */
    @DELETE
    Observable<CommonBaseModel> delete(@Url String url, @QueryMap Map<String, Object> params);

    /**
     * 异步put请求，有请求参数
     *
     * @param url    请求url
     * @param params 请求参数 hashmap键值对
     */
    @FormUrlEncoded
    @PUT
    Observable<CommonBaseModel> put(@Url String url, @FieldMap Map<String, Object> params);


    /**
     * 异步get请求，无请求参数
     *
     * @param url 请求url
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

    /**
     * 异步get请求，有请求参数
     *
     * @param url    请求url
     * @param params 请求参数 hashmap键值对
     */
//    @Streaming
    @POST
    Observable<ResponseBody> download(@Url String url, @Body Map<String, Object> params);

    /**
     * 文件上传
     * 需要注意请求头类型
     * “@Part(“data”) String des”在Post请求中默认的Content-Type类型就是“application/json”
     * 文字的类型：
     * Content-Type: text/plain; charset=UTF-8
     * eg:
     * Map<String, RequestBody> params = new HashMap<>();
     * RequestBody requestParams = RequestBody.create(MediaType.parse("text/plain"),"value");
     * RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),file);
     * params.put("file\";filename=\""+headPhoto.getName(), fileBody); //文件上传  图片类型可以为：image/png
     * params.put(params,requestParams);
     * params.put("updateType",requestParams2);
     *
     * @param url
     * @param params
     * @return
     */
    @Multipart
    @POST
    Observable<CommonBaseModel> upload(@Url String url, @PartMap Map<String, RequestBody> params);

}
