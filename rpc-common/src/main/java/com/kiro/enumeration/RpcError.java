package com.kiro.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Xufangmin
 * @create 2021-08-13-13:27
 */

@AllArgsConstructor
@Getter
public enum RpcError {

    SERVISE_INVOCATION_FAILURE("服务调用出现失败"),
    SERVICE_CAN_NOT_NULL("注册的服务不得为空"),
    SERVICE_NOT_FOUND("找不到对应的服务"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务未实现接口");

    private final String message;
}
