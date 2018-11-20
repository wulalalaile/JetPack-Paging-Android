package com.pingan.haofang.network.exception;

import java.io.IOException;

/**
 * 服务端异常
 * <br>
 * 返回 Json 的 code 不等于 0 时抛出
 */
public class AppServerException extends IOException {

    public AppServerException(String message) {
        super(message);
    }
}
