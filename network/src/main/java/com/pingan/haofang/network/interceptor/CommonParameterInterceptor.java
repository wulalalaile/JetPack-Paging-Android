package com.pingan.haofang.network.interceptor;

import com.google.gson.Gson;
import com.pingan.haofang.network.uitils.Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * 添加在单个 ServerRetrofit 中所有请求公用的添加GET/POST/PUT等格式的参数
 * 注意：该设置只在单个 ServerRetrofit 中全局生效
 * 注意：因为可能在之后的服务端获取参数方式不一样，目前 formBuilder 并没有将参数 encode，如果有问题可以查看是否因此导致
 */
public class CommonParameterInterceptor implements Interceptor {

    private final Gson mGson = new Gson();

    private Map<String, String> mCommonParameterMap;

    public Map<String, String> getCommonParameterMap() {
        return mCommonParameterMap == null ? new HashMap<String, String>() : mCommonParameterMap;
    }

    public void setCommonParameterMap(Map<String, String> commonParameterMap) {
        mCommonParameterMap = commonParameterMap;
    }

    public void addCommonParameterMap(Map<String, String> commonParameterMap) {
        mCommonParameterMap.putAll(commonParameterMap);
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        HttpUrl originalUrl = originalRequest.url();
        HttpUrl.Builder urlBuilder = originalUrl.newBuilder();

        Request.Builder requestBuilder = originalRequest.newBuilder();

        String method = originalRequest.method();
        if (method.equalsIgnoreCase("GET")) {
            for (Map.Entry<String, String> entry : getCommonParameterMap().entrySet()) {
                String value = entry.getValue();
                if (Util.isEmpty(value)) {
                    continue;
                }
                urlBuilder.addEncodedQueryParameter(entry.getKey(), value);
            }
        } else {
            RequestBody body = originalRequest.body();
            if (body instanceof FormBody) {
                //获取原有参数，并添加到新 FormBody 里面
                FormBody oldBody = ((FormBody) body);
                FormBody.Builder formBuilder = new FormBody.Builder();

                for (int i = 0; i < oldBody.size(); i++) {
                    String name = oldBody.name(i);
                    String value = oldBody.value(i);
                    if (Util.isEmpty(value)) {
                        continue;
                    }
                    formBuilder.add(name, value);//获取原有参数，并添加到新 FormBody 里面
                }
                for (Map.Entry<String, String> entry : getCommonParameterMap().entrySet()) {
                    String value = entry.getValue();
                    if (Util.isEmpty(value)) {
                        continue;
                    }
                    formBuilder.add(entry.getKey(), value);
                }
                requestBuilder.method(method, formBuilder
                        .build());
            } else if (body instanceof MultipartBody) {
                // 通常用作图片上传
                MultipartBody oldBody = (MultipartBody) body;
                MultipartBody.Builder multipartBodybuilder = new MultipartBody.Builder();
                for (MultipartBody.Part part : oldBody.parts()) {
                    multipartBodybuilder.addPart(part);
                }
                for (Map.Entry<String, String> entry : getCommonParameterMap().entrySet()) {
                    multipartBodybuilder.addFormDataPart(entry.getKey(), Util.getNotNullString(entry.getValue()));
                }
                multipartBodybuilder.setType(MultipartBody.FORM);
                requestBuilder.method(method, multipartBodybuilder
                        .build());
            } else {
                Buffer buffer = new Buffer();
                body.writeTo(buffer);
                String oldJsonParams = buffer.readUtf8();
                HashMap parameterMap = mGson.fromJson(oldJsonParams, HashMap.class);// 原始参数

                for (Map.Entry<String, String> entry : getCommonParameterMap().entrySet()) {
                    parameterMap.put(entry.getKey(), Util.getNotNullString(entry.getValue()));
                }

                String newJsonParams = mGson.toJson(parameterMap); // {"page":0,"publicParams":{"imei":'xxxxx',"sdk":14,.....}}
                MediaType type = MediaType.parse("application/json; charset=utf-8");
                requestBuilder.method(method, RequestBody.create(type, newJsonParams));
            }
        }

        Request request = requestBuilder
                .url(urlBuilder.build())
                .build();
        return chain.proceed(request);
    }
}
