package com.kiro.rpc.entity;

import com.kiro.rpc.enumeration.StatusCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Xufangmin
 * @create 2021-08-10-17:15
 */

@Data
public class RpcResponse<T> implements Serializable {
    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    public static <T>  RpcResponse<T> success(T data){
        RpcResponse<T> rpcResponse = new RpcResponse<T>();
        rpcResponse.setCode(StatusCode.SUCCESS.getCode());
        rpcResponse.setMessage(StatusCode.SUCCESS.getMessage());
        rpcResponse.setData(data);
        return rpcResponse;
    }

    public static <T> RpcResponse<T> fail(StatusCode code){
        RpcResponse<T> rpcResponse = new RpcResponse<T>();
        rpcResponse.setCode(code.getCode());
        rpcResponse.setMessage(code.getMessage());
        return rpcResponse;
    }

}
