package com.tools.http.fastjson;

import com.alibaba.fastjson.serializer.SerializeConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


public final class FastjsonConverterFactory extends Converter.Factory {

    public static FastjsonConverterFactory create() {
        return new FastjsonConverterFactory(SerializeConfig.getGlobalInstance());
    }

    /**
     * Create an instance using {@code mapper} for conversion.
     */
    public static FastjsonConverterFactory create(SerializeConfig serializeConfig) {
        return new FastjsonConverterFactory(serializeConfig);
    }

    private final SerializeConfig serializeConfig;

    private FastjsonConverterFactory(SerializeConfig serializeConfig) {
        if (serializeConfig == null) throw new NullPointerException("mapper == null");
        this.serializeConfig = serializeConfig;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new FastjsonResponseBodyConverter<>(type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new FastjsonRequestBodyConverter<>(serializeConfig);
    }
}
