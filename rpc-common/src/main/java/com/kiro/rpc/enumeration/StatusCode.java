package com.kiro.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Xufangmin
 * @create 2021-08-10-17:11
 */

@Getter
@AllArgsConstructor
public enum StatusCode {

    SUCCESS(200, "成功调用"),
    FAIL(500, "方法调用失败"),
    NOT_FOUND_METHOD(500, "未找到指定方法"),
    NOT_FOUND_CLASS(500, "未找到指定类");

    private final Integer code;
    private final String message;

}
