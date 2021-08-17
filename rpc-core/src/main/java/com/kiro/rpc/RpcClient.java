package com.kiro.rpc;

import com.kiro.rpc.entity.RpcRequest;

/**
 * 客户端的通用接口，包含一个发送请求的方法
 * @author Xufangmin
 * @create 2021-08-16-10:51
 */
public interface RpcClient {

    Object sendRequest(RpcRequest request);

}
