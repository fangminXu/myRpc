package com.kiro.rpc.entity;

import com.kiro.rpc.enumeration.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 响应对象
 * @author Xufangmin
 * @create 2021-08-16-10:31
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    public static<T> RpcResponse<T> success(T data){
        return new RpcResponse<T>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }

    public static<T> RpcResponse<T> fail(ResponseCode code){
        return new RpcResponse<T>(code.getCode(), code.getMessage(), null);
    }
}
