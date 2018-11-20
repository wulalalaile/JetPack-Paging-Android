package com.pingan.haofang.network.server;

import com.pingan.haofang.network.converter.GsonJsonConverterFactory;
import com.pingan.haofang.network.interceptor.CommonParameterInterceptor;
import com.pingan.haofang.network.interceptor.HeaderInterceptor;
import com.pingan.haofang.network.uitils.SSLUtil;

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
 */
public class UploadServerRetrofit {

    private static volatile Retrofit INSTANCE = null;

    private static HeaderInterceptor sHeaderIntercptor;
    private static CommonParameterInterceptor sParamInterceptor;

    private static String sBaseUrl;

    private UploadServerRetrofit() {
    }

    public static Retrofit getInstance() {
        if (INSTANCE == null) {
            synchronized (UploadServerRetrofit.class) {
                if (INSTANCE == null) {
                    INSTANCE = create();
                }
            }
        }
        return INSTANCE;
    }

    public static void init(String baseUrl){
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

        OkHttpClient client = SSLUtil.getUnsafeOkHttpClient()
                .newBuilder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(sHeaderIntercptor)
                .addInterceptor(sParamInterceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl(sBaseUrl)
                .addConverterFactory(GsonJsonConverterFactory.create())//转换 FastJson 返回格式
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

}