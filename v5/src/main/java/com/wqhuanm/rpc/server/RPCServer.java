package com.wqhuanm.rpc.server;

import com.wqhuanm.rpc.service.BlogService;
import com.wqhuanm.rpc.service.UserService;
import com.wqhuanm.rpc.service.impl.BlogServiceImpl;
import com.wqhuanm.rpc.service.impl.UserServiceImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public interface RPCServer {
    void start(int port);
}
