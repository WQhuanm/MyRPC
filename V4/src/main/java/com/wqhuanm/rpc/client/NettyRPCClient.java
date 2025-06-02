package com.wqhuanm.rpc.client;

import com.wqhuanm.rpc.common.NettyInitializer;
import com.wqhuanm.rpc.common.Request;
import com.wqhuanm.rpc.common.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.NoArgsConstructor;

import java.util.concurrent.CompletableFuture;

public class NettyRPCClient implements RPCClient {

    private static final Bootstrap bootstrap = new Bootstrap();//辅助启动类
    private static final EventLoopGroup group = new NioEventLoopGroup();//事件循环处理组
    private String host;
    private int port;
    private CompletableFuture<Response> future;


    public NettyRPCClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    static {
        bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new NettyInitializer(NettyHandler.class));
    }

    @Override
    public Response sendRequest(Request request) {
        try {
            Channel channel = bootstrap.connect(host, port).sync().channel();
            channel.writeAndFlush(request);
            channel.closeFuture().sync();

            AttributeKey<Response> key = AttributeKey.valueOf("response");
            Response response = channel.attr(key).get();
            System.out.println("获取到response: " + response);
            return response;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @NoArgsConstructor
    static class NettyHandler extends SimpleChannelInboundHandler<Response> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Response msg) throws Exception {
            AttributeKey<Response> key = AttributeKey.valueOf("response");
            ctx.channel().attr(key).set(msg);
            ctx.channel().close();
        }
    }

}
