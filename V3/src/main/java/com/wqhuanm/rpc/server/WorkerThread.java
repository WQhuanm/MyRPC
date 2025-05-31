package com.wqhuanm.rpc.server;


import com.wqhuanm.rpc.common.Request;
import com.wqhuanm.rpc.common.Response;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;

@AllArgsConstructor
public class WorkerThread extends Thread {

    private Socket socket;
    private HashMap<String, Object> mp;

    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            Request request = (Request) ois.readObject();
            Response response = getResponse(request);
            oos.writeObject(response);
            oos.flush();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Response getResponse(Request request) {
        Object service = mp.get(request.getInterfaceName());
        try {
            Method method = service.getClass().getMethod(request.getMethodName(), request.getParamTypes());
            Object invoke = method.invoke(service, request.getParams());
            return Response.success(invoke);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

}
