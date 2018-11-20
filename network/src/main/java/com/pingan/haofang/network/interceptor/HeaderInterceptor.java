package com.pingan.haofang.network.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * 添加在单个 ServerRetrofit 中所有请求公用的 HTTP header fields
 * 注意：该设置只在单个 ServerRetrofit 中全局生效
 */
public class HeaderInterceptor implements Interceptor {

    private Map<String, String> mHeaderMap;

    private Map<String, String> getHeaderMap() {
        return mHeaderMap == null ? new HashMap<String, String>() : mHeaderMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        mHeaderMap = headerMap;
    }

    public void addHeaderMap(Map<String, String> headerMap) {
        mHeaderMap.putAll(headerMap);
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder();
        for (Map.Entry<String, String> entry : getHeaderMap().entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue() == null ? "" : entry.getValue());
        }

        return chain.proceed(requestBuilder.build());
    }
}