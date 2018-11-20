package com.pingan.haofang.network.server;

import com.pingan.haofang.network.converter.AppServerConverterFactory;
import com.pingan.haofang.network.interceptor.CommonParameterInterceptor;
import com.pingan.haofang.network.interceptor.HeaderInterceptor;
import com.pingan.haofang.network.interceptor.SignParameterInterceptor;
import com.pingan.haofang.network.interceptor.UserAgentInterceptor;

import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 针对 AppServer 封装的 Retrofit
 * <p>
 * 注：AppServer 指的是通常请求 API 的服务器，还有 UploadServer、H5Server 等实现
 * </p>
 * 释例：
 * <pre><code>
 * AppRetrofit.getInstance()
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
public class AppServerRetrofit {

    private static volatile Retrofit INSTANCE = null;

    private static HeaderInterceptor sHeaderIntercptor;
    private static CommonParameterInterceptor sParamInterceptor;
    private static SignParameterInterceptor sSignParameterInterceptor;
    private static UserAgentInterceptor sUserAgentInterceptor;

    private static String sBaseUrl="https://newsapi.org";

    private AppServerRetrofit() {
    }

    public static Retrofit getInstance() {
        if (INSTANCE == null) {
            synchronized (AppServerRetrofit.class) {
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
        // 添加 Header
        sHeaderIntercptor = new HeaderInterceptor();
        // 添加 公共参数
        sParamInterceptor = new CommonParameterInterceptor();
        // 添加 校验参数字段
        sSignParameterInterceptor = new SignParameterInterceptor();
        // 添加 User-Agent
        sUserAgentInterceptor = new UserAgentInterceptor();

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(sHeaderIntercptor)
                .addInterceptor(sParamInterceptor)
                .addInterceptor(sSignParameterInterceptor)
                .addInterceptor(sUserAgentInterceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl(sBaseUrl)
                .addConverterFactory(AppServerConverterFactory.create())//转换 AppServer 返回格式
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                .client(client)
                .build();
    }

    public static void setHeaderMap(Map<String,String> map) {
        sHeaderIntercptor.setHeaderMap(map);
    }

    public static void setCommonParameterMap(Map<String,String> map) {
        sParamInterceptor.setCommonParameterMap(map);
    }

    public static void addCommonParameterMap(Map<String,String> map) {
        sParamInterceptor.addCommonParameterMap(map);
    }

    public static void setUserAgent(String userAgent) {
        sUserAgentInterceptor.setUserAgent(userAgent);
    }
}
