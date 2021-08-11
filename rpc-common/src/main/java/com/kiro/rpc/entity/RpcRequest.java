package com.kiro.rpc.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Xufangmin
 * @create 2021-08-10-17:06
 */

@Data
@Builder
public class RpcRequest implements Serializable {
    /**
     * 接口名
     */
    private String interfaceName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 方法参数列表
     */
    private Object[] parameters;

    /**
     * 方法参数类型
     */
    private Class<?>[] paramTypes;
}
