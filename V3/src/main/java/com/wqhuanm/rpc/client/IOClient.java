package com.wqhuanm.rpc.client;

import com.wqhuanm.rpc.common.Request;
import com.wqhuanm.rpc.common.Response;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.Channel;

public class IOClient {

    public static Object sendRequest(String host, int port, Request request) {
        try {
            Socket socket = new Socket(host, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            System.out.println("发送请求: " + request);
            out.writeObject(request);
            out.flush();

            Response response = (Response) in.readObject();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


}
