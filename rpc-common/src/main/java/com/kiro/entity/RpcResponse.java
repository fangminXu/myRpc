package com.kiro.entity;

import com.kiro.enumeration.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author Xufangmin
 * @create 2021-08-13-13:19
 */

@AllArgsConstructor
@Data
public class RpcResponse<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    public static <T> RpcResponse<T> success(T data) {
        return new RpcResponse<T>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS
                .getMessage(), data);
    }

    public static <T> RpcResponse<T> fail(ResponseCode code){
        return new RpcResponse<T>(code.getCode(), code.getMessage(), null);
    }
}
