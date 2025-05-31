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

public class RPCServer {

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            10, 20, 60, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100));
    private static HashMap<String, Object> mp = new HashMap<>();

    static {
        mp.put(UserService.class.getName(), new UserServiceImpl());
        mp.put(BlogService.class.getName(), new BlogServiceImpl());
    }

    public static void main(String[] args) {
        try {
            System.out.println(Runtime.getRuntime().availableProcessors());
            System.out.println("服务端监听端口12345");
            ServerSocket serverSocket = new ServerSocket(12345);

            while (true) {
                Socket socket = serverSocket.accept();
                executor.execute(new WorkerThread(socket, mp));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
