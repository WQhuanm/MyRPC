package com.wqhuanm.rpc.server;

import com.wqhuanm.rpc.pojo.User;
import com.wqhuanm.rpc.service.UserService;
import com.wqhuanm.rpc.service.impl.UserServiceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
                    try {    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                        int userId = ois.readInt();
                        User user = userService.getUserById(userId);
                        oos.writeObject(user);
                        oos.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
