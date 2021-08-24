package com.kiro.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 请求对象
 * @author Xufangmin
 * @create 2021-08-16-10:28
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {
    /**
     * 请求号
     */
    private String requestId;

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 调用方法参数
     */
    private Object[] parameters;

    /**
     * 参数类型
     */
    private Class<?>[] paramsType;

    /**
     * 是否是心跳包
     */
    private boolean heartbeat;
}
