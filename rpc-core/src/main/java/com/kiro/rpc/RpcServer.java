package com.kiro.rpc;

/**
 * 服务端的通用方法，包含一个开启服务端的方法
 * @author Xufangmin
 * @create 2021-08-16-10:52
 */
public interface RpcServer {

    void start(int port);

}
