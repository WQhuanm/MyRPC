package com.wqhuanm.rpc.server;

import com.wqhuanm.rpc.common.Request;
import com.wqhuanm.rpc.common.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ServerNettyHandler extends SimpleChannelInboundHandler<Request> {
    private ServiceProvider serviceProvider;

    public ServerNettyHandler(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request msg) throws Exception {
        Response response = getResponse(msg);
        ctx.writeAndFlush(response);
        ctx.close();
    }

    private Response getResponse(Request request) {
        Object service = serviceProvider.getService(request.getInterfaceName());
        try {
            System.out.println("正在查询请求");
            Method method = service.getClass().getMethod(request.getMethodName(), request.getParamTypes());
            Object invoke = method.invoke(service, request.getParams());
            return Response.success(invoke);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }
}