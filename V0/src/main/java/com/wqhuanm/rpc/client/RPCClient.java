package com.wqhuanm.rpc.client;

import com.wqhuanm.rpc.pojo.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RPCClient {


    public static void main(String[] args) {
        try {
            System.out.println("客户端尝试与服务端连接");
            Socket socket = new Socket("localhost", 12345);
            System.out.println("连接建立");
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeInt(123);
            oos.flush();

            User user = (User) ois.readObject();
            System.out.println("服务端返回的User:" + user);

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
