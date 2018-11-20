package com.pingan.haofang.network.converter;

import com.pingan.haofang.network.response.HttpResponseBody;
import com.pingan.haofang.network.converter.request.GsonJsonRequestBodyConverter;
import com.pingan.haofang.network.converter.response.GsonJsonReponseBodyConverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.annotations.Nullable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 支持 GsonJson 格式转换
 * <br>
 * 如果 API 服务器是 AppServer 建议使用子类 AppServerConvert
 */
public class GsonJsonConverterFactory<T extends HttpResponseBody> extends Converter.Factory {

    public static Converter.Factory create() {
        return new GsonJsonConverterFactory();
    }

    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new GsonJsonRequestBodyConverter<T>();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, T> responseBodyConverter(final Type type, Annotation[] annotations, Retrofit retrofit) {
        return new GsonJsonReponseBodyConverter<>(type);
    }
}
