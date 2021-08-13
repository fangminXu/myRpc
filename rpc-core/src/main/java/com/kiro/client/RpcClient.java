package com.kiro.client;

import com.kiro.entity.RpcRequest;
import com.kiro.entity.RpcResponse;
import com.kiro.enumeration.ResponseCode;
import com.kiro.enumeration.RpcError;
import com.kiro.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Xufangmin
 * @create 2021-08-13-13:29
 */
public class RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    public Object sendRequest(String host, int port, RpcRequest rpcRequest){
        try(Socket socket = new Socket(host, port)){
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            logger.info("客户端开始调用");
            outputStream.writeObject(rpcRequest);
            outputStream.flush();
            RpcResponse rpcResponse = (RpcResponse) inputStream.readObject();
            if(rpcResponse == null){
                logger.error("服务调用失败，service:{}", rpcRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_CAN_NOT_NULL);
            }
            if(rpcResponse.getCode() == null || rpcResponse.getCode() != ResponseCode.SUCCESS.getCode()){
                logger.error("服务调用失败，service:{}, response:{}", rpcRequest.getInterfaceName(), rpcResponse);
                throw new RpcException(RpcError.SERVISE_INVOCATION_FAILURE);
            }
            return rpcResponse.getData();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("调用时有错误发生：", e);
            throw new RpcException("服务调用失败", e);
        }
    }
}
