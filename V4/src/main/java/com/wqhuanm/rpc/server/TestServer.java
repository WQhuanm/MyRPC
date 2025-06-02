package com.wqhuanm.rpc.server;

public class TestServer {

    public static void main(String[] args) {
        NettyRPCServer rpcServer = new NettyRPCServer();
        rpcServer.start(12345);
    }
}
