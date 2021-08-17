package com.kiro.rpc.codec;

import com.kiro.rpc.entity.RpcRequest;
import com.kiro.rpc.enumeration.PackageType;
import com.kiro.rpc.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import sun.plugin2.message.Serializer;

/**
 * @author Xufangmin
 * @create 2021-08-16-11:01
 */
public class CommonEncoder extends MessageToByteEncoder {
    private static final int MAGIC_NUMBER = 0xCAFEBABE;
    private final CommonSerializer commonSerializer;

    public CommonEncoder(CommonSerializer commonSerializer){
        this.commonSerializer = commonSerializer;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(MAGIC_NUMBER);
        if(o instanceof RpcRequest){
            byteBuf.writeInt(PackageType.REQUEST_PACK.getCode());
        }else{
            byteBuf.writeInt(PackageType.RESPONSE_PACK.getCode());
        }
        byteBuf.writeInt(commonSerializer.getCode());
        byte[] bytes = commonSerializer.serialize(o);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }
}
