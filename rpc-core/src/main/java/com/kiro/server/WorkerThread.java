package com.kiro.server;

import com.kiro.rpc.entity.RpcRequest;
import com.kiro.rpc.entity.RpcResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @author Xufangmin
 * @create 2021-08-11-10:05
 */

@AllArgsConstructor
public class WorkerThread implements Runnable{
    private static Logger logger = LoggerFactory.getLogger(WorkerThread.class);

    private Object service;
    private Socket socket;


    @Override
    public void run() {
        logger.info("服务端开始执行调用");
        try(ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())){
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            Object data = method.invoke(service, rpcRequest.getParameters());
            objectOutputStream.writeObject(RpcResponse.success(data));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException |IllegalAccessException |InvocationTargetException e) {
            logger.error("调用时发生错误", e);
        }
    }
}
