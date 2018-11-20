package com.pingan.haofang.network.server;


import com.pingan.haofang.network.converter.AppServerConverterFactory;
import com.pingan.haofang.network.interceptor.CommonParameterInterceptor;

import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 针对 H5 Server 封装的 Retrofit
 * <p>
 * 注：H5 Server 是指 m.pinganfang.com 域名对应的服务器，有不同于 AppServer 的 Header 和 公共参数 等配置
 * <p>
 * 释例：
 * <pre><code>
 * H5ServerRetrofit.getInstance()
 *         .init(UserService.class)
 *         .login(1, "yang101", "a")
 *         .subscribeOn(Schedulers.trampoline())
 *         .observeOn(Schedulers.trampoline())
 *         .subscribe(new AppServerObserver&lt;HttpReponseBody&lt;LoginResponseBody&gt;&gt;() {
 *
 *             public void onNext(HttpReponseBody&lt;LoginResponseBody&gt; loginResponseBody) {
 *
 *             }
 * }</pre></code>
 */
public class H5ServerRetrofit {

    private static volatile Retrofit INSTANCE = null;

    private static CommonParameterInterceptor sParamInterceptor;

    private static String sBaseUrl;

    private H5ServerRetrofit() {
    }

    public static Retrofit getInstance() {
        if (INSTANCE == null) {
            synchronized (H5ServerRetrofit.class) {
                if (INSTANCE == null) {
                    INSTANCE = create();
                }
            }
        }
        return INSTANCE;
    }

    public static void init(String baseUrl) {
        sBaseUrl = baseUrl;
        getInstance();
    }

    private static Retrofit create() {
        // 添加日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 添加 公共参数
        sParamInterceptor = new CommonParameterInterceptor();

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(sParamInterceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl(sBaseUrl)
                .addConverterFactory(AppServerConverterFactory.create())//转换 AppServer 返回格式
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                .client(client)
                .build();
    }

    public static void setCommonParameterMap(Map<String,String> map) {
        sParamInterceptor.setCommonParameterMap(map);
    }

    public static void addCommonParameterMap(Map<String,String> map) {
        sParamInterceptor.addCommonParameterMap(map);
    }
}
