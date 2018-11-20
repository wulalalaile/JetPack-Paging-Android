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
 * 按照服务端已定义规则进行参数的签名
 * 注意：该设置只在单个 ServerRetrofit 中全局生效
 */
public class SignParameterInterceptor implements Interceptor {

    private static final Gson mGson = new Gson();

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        HttpUrl originalUrl = originalRequest.url();
        HttpUrl.Builder urlBuilder = originalUrl.newBuilder();

        Request.Builder requestBuilder = originalRequest.newBuilder();

        Map<String, Object> parameterMap = new HashMap<>();

        String method = originalRequest.method();
        if (method.equalsIgnoreCase("GET")) {
            // 获取原生参数
            for (String name : originalUrl.queryParameterNames()) {
                // 添加到新参数map内
                String value = originalUrl.queryParameter(name);
                if (Util.isEmpty(value)) {
                    continue;
                }
                parameterMap.put(name, value);
            }
            Map<String, String> verifyedMap = Util.getVerifyedMap(originalUrl, parameterMap);
            for (Map.Entry<String, String> entry : verifyedMap.entrySet()) {
                urlBuilder.addEncodedQueryParameter(entry.getKey(), entry.getValue());
            }
            requestBuilder.url(urlBuilder.build());
        } else {
            RequestBody originalBody = originalRequest.body();
            if (originalBody instanceof FormBody) {

                FormBody oldBody = ((FormBody) originalBody);
                FormBody.Builder newFormBuilder = new FormBody.Builder();
                //获取原有参数，并添加到新 FormBody
                for (int i = 0; i < oldBody.size(); i++) {
                    String name = oldBody.name(i);
                    String value = oldBody.value(i);
                    if (Util.isEmpty(value)) {
                        continue;
                    }
                    parameterMap.put(name, value);
                    newFormBuilder.add(name, value);
                }
                //获取校验后的参数，并添加到新 FormBody 里面
                Map<String, String> verifyedMap = Util.getVerifyedMap(originalUrl, parameterMap);
                for (Map.Entry<String, String> entry : verifyedMap.entrySet()) {
                    newFormBuilder.add(entry.getKey(), entry.getValue());
                }
                requestBuilder.method(method, newFormBuilder.build());
            } else if (originalBody instanceof MultipartBody) {
                // 不做处理参数处理
            } else {
                Buffer buffer = new Buffer();
                originalBody.writeTo(buffer);
                String oldJsonParams = buffer.readUtf8();
                HashMap hashMap = mGson.fromJson(oldJsonParams, HashMap.class);// 原始参数

                Map verifyedMap = Util.getVerifyedMap(originalUrl, parameterMap);

                hashMap.putAll(verifyedMap);

                String newJsonParams = mGson.toJson(hashMap); // {"page":0,"publicParams":{"imei":'xxxxx',"sdk":14,.....}}
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
