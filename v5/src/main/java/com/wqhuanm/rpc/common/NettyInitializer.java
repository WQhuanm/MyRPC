package com.wqhuanm.rpc.common;

import com.wqhuanm.rpc.codec.DeCode;
import com.wqhuanm.rpc.codec.EnCode;
import com.wqhuanm.rpc.codec.JsonSerializer;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.Constructor;


/**
 * 对入站和出站的消息分别进行解码和编码
 */
public class NettyInitializer extends ChannelInitializer<SocketChannel> {
    private Class<? extends ChannelInboundHandlerAdapter> inboundHandler;
    public NettyInitializer(Class<? extends ChannelInboundHandlerAdapter> handler) {
        this.inboundHandler = handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //添加调用链，入站消息从链头处理到尾，出站从尾到头

        //消息格式：[长度(4字节)][消息体]
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        //出站时计算消息体长度并写入前4个字节
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(new DeCode());
        pipeline.addLast(new EnCode(new JsonSerializer()));

        var constructor = inboundHandler.getDeclaredConstructor();
        constructor.setAccessible(true);
        pipeline.addLast(constructor.newInstance());
    }
}
