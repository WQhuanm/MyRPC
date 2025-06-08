package com.wqhuanm.rpc.client;

import com.wqhuanm.rpc.common.NettyInitializer;
import com.wqhuanm.rpc.common.Request;
import com.wqhuanm.rpc.common.Response;
import com.wqhuanm.rpc.register.ServiceRegister;
import com.wqhuanm.rpc.register.ZKServiceRegister;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;


public class NettyRPCClient implements RPCClient {
    private Bootstrap bootstrap = new Bootstrap();//辅助启动类
    private ServiceRegister zkRegister = new ZKServiceRegister();

    public NettyRPCClient() {
        bootstrap.group(new NioEventLoopGroup()).channel(NioSocketChannel.class)
                .handler(new NettyInitializer(ClientNettyHandler.class, null));
    }

    @Override
    public Response sendRequest(Request request) {
        try {
            InetSocketAddress discover = zkRegister.discover(request.getInterfaceName());
            System.out.println("负载均衡选择了" + discover + "服务器");
            Channel channel = bootstrap.connect(discover.getHostName(), discover.getPort())
                    .sync().channel();
            System.out.println("与服务器建立连接，发送请求ing");
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


}
