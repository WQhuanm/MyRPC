package com.wqhuanm.rpc.server;

import com.wqhuanm.rpc.common.NettyInitializer;
import com.wqhuanm.rpc.common.Request;
import com.wqhuanm.rpc.common.Response;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NettyRPCServer implements RPCServer {

    private ServiceProvider serviceProvider;
    private int port;

    public NettyRPCServer(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
        port = serviceProvider.getPort();
    }

    public void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();//启动辅助类

        try {
            ChannelFuture channelFuture = serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new NettyInitializer(ServerNettyHandler.class, serviceProvider))
                    .bind(port).sync();
            System.out.println("服务器启动成功，监听端口：" + port);
            //死循环监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }


}
