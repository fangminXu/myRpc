package com.kiro.rpc.util;

import com.kiro.rpc.entity.RpcRequest;
import com.kiro.rpc.entity.RpcResponse;
import com.kiro.rpc.enumeration.ResponseCode;
import com.kiro.rpc.enumeration.RpcError;
import com.kiro.rpc.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 检查请求与响应
 * @author Xufangmin
 * @create 2021-08-17-16:37
 */
public class RpcMessageChecker {
    public static final String INTERFACE_NAME = "interfaceName";
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcMessageChecker.class);

    private RpcMessageChecker(){}

    public static void check(RpcRequest rpcRequest, RpcResponse rpcResponse){
        if(rpcResponse == null){
            LOGGER.error("调用服务失败，serviceName:{}", rpcRequest.getInterfaceName());
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
        if(!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())){
            throw new RpcException(RpcError.REPONSE_NOT_MATCH);
        }
        if(rpcResponse.getCode() == null || !rpcResponse.getCode().equals(ResponseCode.SUCCESS.getCode())){
            LOGGER.error("调用服务失败,serviceName:{},RpcResponse:{}", rpcRequest.getInterfaceName(), rpcResponse);
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }
}
