package com.kiro.rpc.exception;

import com.kiro.rpc.enumeration.RpcError;

/**
 * @author Xufangmin
 * @create 2021-08-16-10:46
 */
public class RpcException extends RuntimeException{

    public RpcException(RpcError error, String detail){
        super(error.getMessage() + ":" + detail);
    }

    public RpcException(String message, Throwable cause){
        super(message, cause);
    }

    public RpcException(RpcError error){
        super(error.getMessage());
    }
}
