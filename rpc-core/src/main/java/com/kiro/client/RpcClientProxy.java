package com.kiro.client;

import com.kiro.rpc.entity.RpcRequest;
import com.kiro.rpc.entity.RpcResponse;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Xufangmin
 * @create 2021-08-11-9:52
 */

@AllArgsConstructor
public class RpcClientProxy implements InvocationHandler {
    private String host;
    private Integer port;

    public <T> T getProxy(Class<T> clazz){
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = RpcRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes()).build();
        RpcClient rpcClient = new RpcClient();
        return ((RpcResponse)rpcClient.sendMessage(host, port, rpcRequest)).getData();
    }
}
