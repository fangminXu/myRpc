package com.kiro.rpc.netty.client;

import com.kiro.rpc.RpcClient;
import com.kiro.rpc.codec.CommonDecoder;
import com.kiro.rpc.codec.CommonEncoder;
import com.kiro.rpc.entity.RpcRequest;
import com.kiro.rpc.entity.RpcResponse;
import com.kiro.rpc.enumeration.RpcError;
import com.kiro.rpc.exception.RpcException;
import com.kiro.rpc.netty.server.NettyServerHandler;
import com.kiro.rpc.registry.NacosServiceDiscovery;
import com.kiro.rpc.registry.NacosServiceRegistry;
import com.kiro.rpc.registry.ServiceDiscovery;
import com.kiro.rpc.registry.ServiceRegistry;
import com.kiro.rpc.serializer.CommonSerializer;
import com.kiro.rpc.serializer.JsonSerializer;
import com.kiro.rpc.serializer.KryoSerializer;
import com.kiro.rpc.util.RpcMessageChecker;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Xufangmin
 * @create 2021-08-16-13:44
 */
public class NettyClient implements RpcClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    private static final Bootstrap BOOTSTRAP;
    private static final EventLoopGroup GROUP;

    static {
        GROUP = new NioEventLoopGroup();
        BOOTSTRAP = new Bootstrap();
        BOOTSTRAP.group(GROUP)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true);
    }

    private final ServiceDiscovery serviceDiscovery;
    private CommonSerializer commonSerializer;

    public NettyClient(){
        this.serviceDiscovery = new NacosServiceDiscovery();
    }

    @Override
    public Object sendRequest(RpcRequest request) {
        if(commonSerializer == null){
            LOGGER.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        AtomicReference<Object> result = new AtomicReference<>(null);
        try {
            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(request.getInterfaceName());
            Channel channel = ChannelProvider.get(inetSocketAddress, commonSerializer);
            if(!channel.isActive()){
                GROUP.shutdownGracefully();
                LOGGER.info("服务器未开启...");
                return null;
            }
            channel.writeAndFlush(request).addListener(future -> {
                if(future.isSuccess()){
                    LOGGER.info(String.format("客户端发送消息: %s", request.toString()));
                }else{
                    LOGGER.error("发送消息时有错误发生:" , future.cause());
                }
            });
            channel.closeFuture().sync();
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse" + request.getRequestId());
            RpcResponse rpcResponse = channel.attr(key).get();
            RpcMessageChecker.check(request, rpcResponse);
            result.set(rpcResponse.getData());
        } catch (InterruptedException e) {
            LOGGER.error("发送消息时有错误发生：", e);
            Thread.currentThread().interrupt();
        }
        return result.get();
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.commonSerializer = serializer;
    }
}
