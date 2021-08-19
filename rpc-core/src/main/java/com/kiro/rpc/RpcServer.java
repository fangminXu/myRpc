package com.kiro.rpc;

import com.kiro.rpc.serializer.CommonSerializer;

/**
 * 服务端的通用方法，包含一个开启服务端的方法
 * @author Xufangmin
 * @create 2021-08-16-10:52
 */
public interface RpcServer {

    void start();

    void setSerializer(CommonSerializer serializer);

    <T> void publishService(T sevice, Class<T> serviceClass);
}
