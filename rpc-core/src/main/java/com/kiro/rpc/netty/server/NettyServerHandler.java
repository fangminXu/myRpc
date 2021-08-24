package com.kiro.rpc.netty.server;

import com.kiro.rpc.RequestHandler;
import com.kiro.rpc.entity.RpcRequest;
import com.kiro.rpc.factory.SingletonFactory;
import com.kiro.rpc.factory.ThreadPoolFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
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
    private static final String THREAD_NAME_PREFIX = "netty-server-handler";
    private final RequestHandler requestHandler;

    public NettyServerHandler(){
        requestHandler = SingletonFactory.getInstance(RequestHandler.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest request) throws Exception {
        try{
            if(request.isHeartbeat()){
                LOGGER.info("接收到客户端心跳包");
                return;
            }
            LOGGER.info("服务器端接收到请求:{}", request);
            Object result = requestHandler.handle(request);
            if(channelHandlerContext.channel().isActive() && channelHandlerContext.channel().isWritable()){
                channelHandlerContext.writeAndFlush(result);
            }else{
                LOGGER.error("通道不可写");
            }
        } finally {
            ReferenceCountUtil.release(request);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        LOGGER.error("处理调用过程时有错误发生：");
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if(state == IdleState.READER_IDLE){
                LOGGER.info("长时间未收到心跳包，断开连接...");
                ctx.close();
            }
        }else{
            super.userEventTriggered(ctx, evt);
        }
    }
}
