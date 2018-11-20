package com.pingan.haofang.network.converter.request;

import com.google.gson.Gson;
import com.pingan.haofang.network.response.HttpResponseBody;

import io.reactivex.annotations.NonNull;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * 将请求参数用 GsonJson 转换成 Request Body
 */
public class GsonJsonRequestBodyConverter<T extends HttpResponseBody> implements Converter<T, RequestBody> {

    private final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private Gson gson = new Gson();

    @Override
    public RequestBody convert(@NonNull T value) {
        return RequestBody.create(MEDIA_TYPE, gson.toJson(value));
    }
}