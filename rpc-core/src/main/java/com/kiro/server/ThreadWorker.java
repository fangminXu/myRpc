package com.kiro.server;

import com.kiro.entity.RpcRequest;
import com.kiro.entity.RpcResponse;
import com.kiro.enumeration.ResponseCode;
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
 * @create 2021-08-13-14:46
 */

@AllArgsConstructor
public class ThreadWorker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ThreadWorker.class);
    private Object service;
    private Socket socket;

    @Override
    public void run() {
        try(ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            RpcRequest rpcRequest = (RpcRequest)objectInputStream.readObject();
            RpcResponse rpcResponse = invokeMethod(rpcRequest);
            objectOutputStream.writeObject(rpcResponse);
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            logger.error("调用或发送时有错误发生：", e);
        }
    }

    public RpcResponse invokeMethod(RpcRequest rpcRequest) throws ClassNotFoundException, InvocationTargetException,
            IllegalAccessException {
        Class<?> clazz = Class.forName(rpcRequest.getInterfaceName());
        if(!clazz.isAssignableFrom(service.getClass())){
            return RpcResponse.fail(ResponseCode.CLASS_NOT_FOUND);
        }
        Method method;
        try{
            method = clazz.getMethod(rpcRequest.getMethodName(), rpcRequest.getParamsType());
        } catch (NoSuchMethodException e) {
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND);
        }
        Object data =  method.invoke(service, rpcRequest.getParameters());
        return RpcResponse.success(data);
    }
}
