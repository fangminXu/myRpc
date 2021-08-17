package com.kiro.rpc.netty.client;

import com.kiro.rpc.RpcClient;
import com.kiro.rpc.codec.CommonDecoder;
import com.kiro.rpc.codec.CommonEncoder;
import com.kiro.rpc.entity.RpcRequest;
import com.kiro.rpc.entity.RpcResponse;
import com.kiro.rpc.enumeration.RpcError;
import com.kiro.rpc.exception.RpcException;
import com.kiro.rpc.netty.server.NettyServerHandler;
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

/**
 * @author Xufangmin
 * @create 2021-08-16-13:44
 */
public class NettyClient implements RpcClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);

    private String host;
    private int port;
    private static final Bootstrap BOOTSTRAP;

    private CommonSerializer commonSerializer;

    public NettyClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    static {
        EventLoopGroup group = new NioEventLoopGroup();
        BOOTSTRAP = new Bootstrap();
        BOOTSTRAP.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true);
    }

    @Override
    public Object sendRequest(RpcRequest request) {
        if(commonSerializer == null){
            LOGGER.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        BOOTSTRAP.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new CommonDecoder());
                pipeline.addLast(new CommonEncoder(commonSerializer));
                pipeline.addLast(new NettyClientHandler());
            }
        });
        try {
            ChannelFuture future = BOOTSTRAP.connect(host, port).sync();
            LOGGER.info("客户端连接到服务器{}:{}", host, port);
            Channel channel = future.channel();
            if(channel != null){
                channel.writeAndFlush(request).addListener(future1 -> {
                    if(future1.isSuccess()){
                        LOGGER.info(String.format("客户端发送消息：%s", request.toString()));
                    } else {
                        LOGGER.error("发送消息时有错误发生：", future1.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse" + request.getRequestId());
                RpcResponse rpcResponse = channel.attr(key).get();
                RpcMessageChecker.check(request, rpcResponse);
                return rpcResponse.getData();
            }
        } catch (InterruptedException e) {
            LOGGER.error("发送消息时有错误发生:", e);
        }
        return null;
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.commonSerializer = serializer;
    }
}
