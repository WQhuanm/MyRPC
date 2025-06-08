package com.wqhuanm.rpc.server;

public class TestServer {

    public static void main(String[] args) {

        new Thread(() -> {
            NettyRPCServer rpcServer = new NettyRPCServer(new ServiceProvider("127.0.0.1", 12345));
            rpcServer.start();
        }).start();
        new Thread(() -> {
            NettyRPCServer rpcServer = new NettyRPCServer(new ServiceProvider("127.0.0.1", 54321));
            rpcServer.start();
        }).start();

    }
}
