package com.wqhuanm.rpc.server;

import com.wqhuanm.rpc.common.NettyInitializer;
import com.wqhuanm.rpc.common.Request;
import com.wqhuanm.rpc.common.Response;
import com.wqhuanm.rpc.service.BlogService;
import com.wqhuanm.rpc.service.UserService;
import com.wqhuanm.rpc.service.impl.BlogServiceImpl;
import com.wqhuanm.rpc.service.impl.UserServiceImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class NettyRPCServer implements RPCServer {

    private static final HashMap<String, Object> mp = new HashMap<>();

    static {
        mp.put(UserService.class.getName(), new UserServiceImpl());
        mp.put(BlogService.class.getName(), new BlogServiceImpl());
    }

    public void start(int port) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();//启动辅助类

        try {
            ChannelFuture channelFuture = serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new NettyInitializer(NettyHandler.class)).bind(port).sync();
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

    static class NettyHandler extends SimpleChannelInboundHandler<Request> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Request msg) throws Exception {
            Response response = getResponse(msg);
            ctx.writeAndFlush(response);
            ctx.close();
        }

        private Response getResponse(Request request) {
            Object service = mp.get(request.getInterfaceName());
            try {
                Method method = service.getClass().getMethod(request.getMethodName(), request.getParamTypes());
                Object invoke = method.invoke(service, request.getParams());
                return Response.success(invoke);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

        }
    }

}
