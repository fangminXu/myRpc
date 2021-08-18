package com.kiro.rpc;

import com.kiro.rpc.entity.RpcRequest;
import com.kiro.rpc.entity.RpcResponse;
import com.kiro.rpc.enumeration.ResponseCode;
import com.kiro.rpc.registry.ServiceProvider;
import com.kiro.rpc.registry.ServiceProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 服务端处理请求
 * @author Xufangmin
 * @create 2021-08-16-13:20
 */
public class RequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);
    private static final ServiceProvider SERVICE_PROVIDER;

    static {
        SERVICE_PROVIDER = new ServiceProviderImpl();
    }

    public Object handle(RpcRequest rpcRequest){
        Object result = null;
        Object service = SERVICE_PROVIDER.getServiceProvider(rpcRequest.getInterfaceName());
        try {
            result = invokeTargetMethod(rpcRequest, service);
            LOGGER.info("服务:{} 成功调用方法:{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.error("调用或发送时有错误发生：", e);
        }
        return result;
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) throws InvocationTargetException,
            IllegalAccessException {
        Method method;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamsType());
        } catch (NoSuchMethodException e) {
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND, rpcRequest.getRequestId());
        }
        Object data =  method.invoke(service, rpcRequest.getParameters());
        return RpcResponse.success(data, rpcRequest.getRequestId());
    }
}
