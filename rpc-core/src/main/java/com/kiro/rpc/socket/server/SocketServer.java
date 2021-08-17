package com.kiro.rpc.socket.server;

import com.kiro.rpc.RequestHandler;
import com.kiro.rpc.RpcServer;
import com.kiro.rpc.enumeration.RpcError;
import com.kiro.rpc.exception.RpcException;
import com.kiro.rpc.registry.DefaultServiceRegistry;
import com.kiro.rpc.registry.ServiceRegistry;
import com.kiro.rpc.serializer.CommonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author Xufangmin
 * @create 2021-08-16-13:05
 */
public class SocketServer implements RpcServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketServer.class);
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPICITY = 100;
    private final ExecutorService threadPool;
    private RequestHandler requestHandler = new RequestHandler(); //定义处理请求的工具类
    private final ServiceRegistry serviceRegistry; //定义注册工具类
    private CommonSerializer serializer;

    public SocketServer(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPICITY);
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                blockingQueue, Executors.defaultThreadFactory());
    }

    @Override
    public void start(int port) {
        if(serializer == null){
            LOGGER.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        try(ServerSocket serverSocket = new ServerSocket(port)){
            LOGGER.info("服务器开始启动...");
            Socket socket;
            while((socket = serverSocket.accept()) != null){
                LOGGER.info("消费者连接{}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket, requestHandler, serviceRegistry, serializer));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            LOGGER.error("服务器启动时有错误发送：", e);
        }
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
