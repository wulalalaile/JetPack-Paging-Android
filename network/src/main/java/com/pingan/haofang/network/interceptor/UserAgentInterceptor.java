package com.pingan.haofang.network.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * 添加在单个 ServerRetrofit 中所有请求公用的 HTTP header fields
 * 注意：该设置只在单个 ServerRetrofit 中全局生效
 */
public class UserAgentInterceptor implements Interceptor {

    private String userAgent;

    private String getUserAgent() {
        return userAgent == null || userAgent.length() == 0 ? "" : userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void addUserAgent(String userAgent) {

    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder();
        requestBuilder.addHeader("User-Agent", getUserAgent());

        return chain.proceed(requestBuilder.build());
    }
}