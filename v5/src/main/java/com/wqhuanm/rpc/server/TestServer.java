package com.wqhuanm.rpc.server;

public class TestServer {

    public static void main(String[] args) {
        NettyRPCServer rpcServer = new NettyRPCServer(new ServiceProvider("127.0.0.1", 12345));
        rpcServer.start();
    }
}
