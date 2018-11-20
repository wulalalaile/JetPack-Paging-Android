package com.pingan.haofang.network.exception;

import java.io.IOException;

/**
 * Token 过期异常时抛出
 * 通常在 AppServerObserver 内的 onError() 处理
 */
public class TokenExpiredException extends IOException {

    public TokenExpiredException(String message) {
        super(message);
    }
}
