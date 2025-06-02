package com.wqhuanm.rpc.codec;

import com.wqhuanm.rpc.common.Request;
import com.wqhuanm.rpc.common.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EnCode extends MessageToByteEncoder {
    private Serializer serializer;

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg instanceof Request) {
            out.writeShort(0);
        } else if (msg instanceof Response) {
            out.writeShort(1);
        }
        out.writeShort(serializer.getType());
        byte[] serialize = serializer.serialize(msg);
        out.writeInt(serialize.length);
        out.writeBytes(serialize);
    }
}
