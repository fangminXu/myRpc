package com.kiro.rpc.netty.server;

import com.kiro.rpc.RequestHandler;
import com.kiro.rpc.entity.RpcRequest;
import com.kiro.rpc.util.ThreadPoolFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * @author Xufangmin
 * @create 2021-08-16-15:38
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServerHandler.class);
    private static RequestHandler requestHandler;
    private static final String THREAD_NAME_PREFIX = "netty-server-handler";
    private static final ExecutorService threadPool;

    static {
        requestHandler = new RequestHandler();
        threadPool = ThreadPoolFactory.createDefaultThreadPool(THREAD_NAME_PREFIX);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest request) throws Exception {
        threadPool.execute(() -> {
            try {
                LOGGER.info("服务器接收到请求：{}", request);
                Object result = requestHandler.handle(request);
                ChannelFuture future = channelHandlerContext.writeAndFlush(result);
                future.addListener(ChannelFutureListener.CLOSE);
            } finally {
                ReferenceCountUtil.release(request);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        LOGGER.error("处理调用过程时有错误发生：");
        cause.printStackTrace();
        ctx.close();
    }
}
