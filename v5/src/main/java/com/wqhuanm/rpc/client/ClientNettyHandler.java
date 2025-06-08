package com.wqhuanm.rpc.client;

import com.wqhuanm.rpc.common.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

class ClientNettyHandler extends SimpleChannelInboundHandler<Response> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response msg) throws Exception {
        AttributeKey<Response> key = AttributeKey.valueOf("response");
        ctx.channel().attr(key).set(msg);
        ctx.channel().close();
    }
}
