package com.pingan.haofang.network.converter;

import com.pingan.haofang.network.response.HttpResponseBody;
import com.pingan.haofang.network.converter.response.AppServerResponseBodyConvert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.annotations.Nullable;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * AppServerConvert
 * <br>
 * 在创建 Retrofit 时使用，内置了 JSON 转换
 * 使用 AppServerConvert 意味着将使用 AppServer 所要求的 JSON 格式(HttpResponseBody)
 * <br>
 * 如果你所用请求的 API 提供的不是这种格式，请自定义一个新的 ConvertFactory
 */
public class AppServerConverterFactory<T extends HttpResponseBody> extends Converter.Factory {

    public static Converter.Factory create() {
        return new AppServerConverterFactory();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, T> responseBodyConverter(final Type type, Annotation[] annotations, Retrofit retrofit) {
        return new AppServerResponseBodyConvert<>(type);
    }
}