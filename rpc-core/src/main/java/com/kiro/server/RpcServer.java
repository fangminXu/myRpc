package com.kiro.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author Xufangmin
 * @create 2021-08-11-10:05
 */
public class RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);
    private final ExecutorService executorService;

    public RpcServer(){
        int corePoolSize = 5;
        int maxPoolSize = 50;
        long keepAliveTime = 60;
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(100);
        executorService = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue, Executors.defaultThreadFactory());
    }

    public void Register(Object service, int port){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("服务器开始启动注册...");
            Socket socket;
            while((socket = serverSocket.accept()) != null){
                logger.info("服务器接收到请求：" + socket.getInetAddress());
                executorService.execute(new WorkerThread(service, socket));
            }
        } catch (IOException e) {
            logger.error("服务端接收时发生错误", e);
        }
    }
}
