package com.kiro.rpc.socket.client;

import com.kiro.rpc.RpcClient;
import com.kiro.rpc.entity.RpcRequest;
import com.kiro.rpc.entity.RpcResponse;
import com.kiro.rpc.enumeration.ResponseCode;
import com.kiro.rpc.enumeration.RpcError;
import com.kiro.rpc.exception.RpcException;
import com.kiro.rpc.registry.NacosServiceDiscovery;
import com.kiro.rpc.registry.NacosServiceRegistry;
import com.kiro.rpc.registry.ServiceDiscovery;
import com.kiro.rpc.registry.ServiceRegistry;
import com.kiro.rpc.serializer.CommonSerializer;
import com.kiro.rpc.socket.util.ObjectReader;
import com.kiro.rpc.socket.util.ObjectWriter;
import com.kiro.rpc.util.RpcMessageChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Xufangmin
 * @create 2021-08-16-12:57
 */
public class SocketClient implements RpcClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketClient.class);

    private final ServiceDiscovery serviceDiscovery;
    private CommonSerializer serializer;

    public SocketClient(){
        this.serviceDiscovery = new NacosServiceDiscovery();
    }

    @Override
    public Object sendRequest(RpcRequest request) {
        if(serializer == null){
            LOGGER.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(request.getInterfaceName());
        try(Socket socket = new Socket()){
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream, request, serializer);
            Object obj = ObjectReader.readObject(inputStream);
            RpcResponse rpcResponse = (RpcResponse) obj;
            if(rpcResponse == null){
                LOGGER.error("服务调用失败, service:{}", request.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + request.getInterfaceName());
            }
            if(rpcResponse.getCode() == null || rpcResponse.getCode() != ResponseCode.SUCCESS.getCode()){
                LOGGER.error("服务调用失败, service:{}, response:{}", request.getInterfaceName(), rpcResponse);
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + request.getInterfaceName());
            }
            RpcMessageChecker.check(request, rpcResponse);
            return rpcResponse.getData();
        } catch (IOException e) {
            LOGGER.error("调用时有错误发生：", e);
            throw new RpcException("服务调用失败", e);
        }
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
