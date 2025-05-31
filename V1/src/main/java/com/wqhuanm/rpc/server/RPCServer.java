package com.wqhuanm.rpc.server;

import com.wqhuanm.rpc.common.Request;
import com.wqhuanm.rpc.common.Response;
import com.wqhuanm.rpc.pojo.User;
import com.wqhuanm.rpc.service.UserService;
import com.wqhuanm.rpc.service.impl.UserServiceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class RPCServer {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        try {

            System.out.println("服务端监听端口12345");
            ServerSocket serverSocket = new ServerSocket(12345);

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
//                        System.out.println(Thread.currentThread().getName());
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                        Request request = (Request) ois.readObject();

                        Method method = UserService.class.getMethod(request.getMethodName(), request.getParamTypes());
                        Object invoke = method.invoke(userService, request.getParams());
                        oos.writeObject(Response.success(invoke));
                        oos.flush();
                    } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                             InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
