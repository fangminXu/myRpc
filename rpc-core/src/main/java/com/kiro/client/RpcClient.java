package com.kiro.client;

import com.kiro.rpc.entity.RpcRequest;
import com.kiro.rpc.entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Xufangmin
 * @create 2021-08-11-9:52
 */
public class RpcClient {
    Logger logger = LoggerFactory.getLogger(RpcClient.class);

    public RpcResponse sendMessage(String host, int port, RpcRequest request){
        try(Socket socket = new Socket(host, port)){
            logger.info("客户端开始发送请求");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
            return (RpcResponse) objectInputStream.readObject();
        } catch (IOException |ClassNotFoundException e) {
            logger.error("客户端发送或接收错误", e);
        }
        return null;
    }
}
