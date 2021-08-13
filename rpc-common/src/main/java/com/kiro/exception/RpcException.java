package com.kiro.exception;

import com.kiro.enumeration.RpcError;
import com.sun.org.apache.xalan.internal.xsltc.dom.CachedNodeListIterator;

/**
 * @author Xufangmin
 * @create 2021-08-13-13:36
 */
public class RpcException extends RuntimeException {
    public RpcException(RpcError error, String detail){
        super(error.getMessage() + ": " + detail);
    }

    public RpcException(String message, Throwable cause){
        super(message, cause);
    }

    public RpcException(RpcError error){
        super(error.getMessage());
    }
}
