package com.pingan.haofang.network.converter.response;

import com.pingan.haofang.network.response.HttpResponseBody;
import com.pingan.haofang.network.uitils.Util;
import com.pingan.haofang.network.exception.AppServerException;
import com.pingan.haofang.network.exception.TokenExpiredException;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * AppServer 的响应转换类
 * 通过父类 GsonJsonReponseBodyConverter 先转换了 Json ，再进行了 AppServer 所使用的协议特有的 code 判断
 * 比如用户登录超时，可扩展更多
 *
 * @param <T>
 */
public class AppServerResponseBodyConvert<T extends HttpResponseBody> extends GsonJsonReponseBodyConverter<T> implements Converter<ResponseBody, T> {

    public AppServerResponseBodyConvert(Type type) {
        super(type);
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        T t = super.convert(value);
        Util.checkNotNull(t, "Json 反序列化服务端响应失败，请检查接口返回和实体类是否一致");
        switch (t.resultcode) {
            case 0:
            case 200:
                return t;
            case 20001:// hft 服务器
            case 200001://助手 服务器
                throw new TokenExpiredException("请求所用 Token 已经过期，可能是该帐号在其他地方登陆");
            default:
                throw new AppServerException(t.status);
        }
    }
}