package com.kiro.rpc.socket.server;

import com.kiro.rpc.RequestHandler;
import com.kiro.rpc.RpcServer;
import com.kiro.rpc.enumeration.RpcError;
import com.kiro.rpc.exception.RpcException;
import com.kiro.rpc.registry.NacosServiceRegistry;
import com.kiro.rpc.registry.ServiceProvider;
import com.kiro.rpc.registry.ServiceProviderImpl;
import com.kiro.rpc.registry.ServiceRegistry;
import com.kiro.rpc.serializer.CommonSerializer;
import com.kiro.rpc.util.ThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author Xufangmin
 * @create 2021-08-16-13:05
 */
public class SocketServer implements RpcServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketServer.class);
    private final ExecutorService threadPool;
    private final String host;
    private final int port;
    private RequestHandler requestHandler = new RequestHandler(); //定义处理请求的工具类
    private CommonSerializer serializer;

    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;

    public SocketServer(String host, int port) {
        this.host = host;
        this.port = port;
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
    }

    @Override
    public void start() {
        if(serializer == null){
            LOGGER.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }

        try(ServerSocket serverSocket = new ServerSocket(port)){
            LOGGER.info("服务器开始启动...");
            Socket socket;
            while((socket = serverSocket.accept()) != null){
                LOGGER.info("消费者连接{}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket, requestHandler, serializer));
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

    @Override
    public <T> void publishService(Object sevice, Class<T> serviceClass) {
        serviceProvider.addServiceProvider(sevice);
        serviceRegistry.register(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
    }
}
