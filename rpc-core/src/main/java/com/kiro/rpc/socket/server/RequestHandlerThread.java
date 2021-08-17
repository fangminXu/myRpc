package com.kiro.rpc.socket.server;

import com.kiro.rpc.RequestHandler;
import com.kiro.rpc.entity.RpcRequest;
import com.kiro.rpc.registry.ServiceRegistry;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    @Override
    public void run() {
        try(ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())){
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            RpcRequest request = (RpcRequest) inputStream.readObject();
            String interfaceName = request.getInterfaceName();
            Object service = registry.getService(interfaceName);
            Object result = handler.handle(request, service);
            outputStream.writeObject(result);
            outputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error("调用或发送时有错误发生：", e);
        }
    }
}
