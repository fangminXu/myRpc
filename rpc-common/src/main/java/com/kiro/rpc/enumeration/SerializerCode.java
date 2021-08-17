package com.kiro.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字节流中标识序列化和反序列化器
 * @author Xufangmin
 * @create 2021-08-16-10:44
 */

@AllArgsConstructor
@Getter
public enum SerializerCode {
    JSON(1);

    private final int code;
}
