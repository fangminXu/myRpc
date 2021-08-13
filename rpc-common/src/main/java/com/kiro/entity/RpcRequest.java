package com.kiro.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Xufangmin
 * @create 2021-08-13-13:15
 */

@Data
@Builder
public class RpcRequest implements Serializable {
    /**
     * 接口名称
     */
    private String interfaceName;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 参数
     */
    private Object[] parameters;
    /**
     * 参数类型
     */
    private Class<?>[] paramsType;
}
