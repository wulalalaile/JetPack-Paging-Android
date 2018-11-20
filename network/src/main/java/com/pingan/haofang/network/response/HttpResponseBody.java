package com.pingan.haofang.network.response;

/**
 * HttpResponseBody
 * <br>
 * 服务端约定好的 json 格式({"code":0,"msg":"success",data:{}})
 * @param <T> 和服务接口提供方定义的具体类型
 */
public class HttpResponseBody<T> {

    /**
     * code : 200001
     * msg : 请先登录
     * data : {}
     */
    public int resultcode;
    public String status;
    public T articles;

    @Override
    public String toString() {
        return "HttpResponseBody{" +
                "resultcode=" + resultcode +
                ", status='" + status + '\'' +
                ", articles=" + articles +
                '}';
    }
}
