package com.kiro.rpc.socket.server;

import com.kiro.rpc.RequestHandler;
import com.kiro.rpc.entity.RpcRequest;
import com.kiro.rpc.registry.ServiceRegistry;
import com.kiro.rpc.serializer.CommonSerializer;
import com.kiro.rpc.socket.util.ObjectReader;
import com.kiro.rpc.socket.util.ObjectWriter;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * @author Xufangmin
 * @create 2021-08-16-13:36
 */

@AllArgsConstructor
public class RequestHandlerThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandlerThread.class);

    private Socket socket;
    private RequestHandler handler;
    private ServiceRegistry registry;
    private CommonSerializer serializer;

    @Override
    public void run() {
        try(InputStream inputStream = socket.getInputStream(); OutputStream outputStream = socket.getOutputStream()) {
            RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(inputStream);
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = registry.getService(interfaceName);
            Object result = handler.handle(rpcRequest, service);
            ObjectWriter.writeObject(outputStream, result, serializer);
        } catch (IOException e) {
            LOGGER.error("调用或发送时有错误发生：", e);
        }
    }
}
