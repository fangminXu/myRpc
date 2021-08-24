package com.kiro.rpc.netty.server;

import com.kiro.rpc.RpcServer;
import com.kiro.rpc.codec.CommonDecoder;
import com.kiro.rpc.codec.CommonEncoder;
import com.kiro.rpc.enumeration.RpcError;
import com.kiro.rpc.exception.RpcException;
import com.kiro.rpc.hook.ShutdownHook;
import com.kiro.rpc.registry.NacosServiceRegistry;
import com.kiro.rpc.registry.ServiceProvider;
import com.kiro.rpc.registry.ServiceProviderImpl;
import com.kiro.rpc.registry.ServiceRegistry;
import com.kiro.rpc.serializer.CommonSerializer;
import com.kiro.rpc.serializer.JsonSerializer;
import com.kiro.rpc.serializer.KryoSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author Xufangmin
 * @create 2021-08-16-15:46
 */
public class NettyServer implements RpcServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);
    private final CommonSerializer serializer;
    private final String host;
    private final int port;

    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;

    public NettyServer(String host, int port){
        this(host, port, DEFAULT_SERIALIZER);
    }

    public NettyServer(String host, int port, Integer serializer){
        this.host = host;
        this.port = port;
        serviceRegistry = new NacosServiceRegistry();
        serviceProvider = new ServiceProviderImpl();
        this.serializer = CommonSerializer.getByCode(serializer);
    }


    @Override
    public <T> void publishService(T sevice, Class<T> serviceClass) {
        //本地注册表
        serviceProvider.addServiceProvider(sevice, serviceClass);
        //注册中心
        serviceRegistry.register(serviceClass.getCanonicalName(), new InetSocketAddress(host, port));
    }

    @Override
    public void start() {
        if(serializer == null){
            LOGGER.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        ShutdownHook.getShutdownHook().addClearAllHook();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            pipeline.addLast(new CommonEncoder(serializer));
                            pipeline.addLast(new CommonDecoder());
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error("启动服务器时有错误发生：", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}
