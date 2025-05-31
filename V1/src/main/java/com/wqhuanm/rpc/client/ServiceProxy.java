package com.wqhuanm.rpc.client;

import com.wqhuanm.rpc.common.Request;
import com.wqhuanm.rpc.common.Response;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


@AllArgsConstructor
public class ServiceProxy implements InvocationHandler {
    private String host;
    private int port;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = Request.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName()).params(args).paramTypes(method.getParameterTypes())
                .build();

        Response response = (Response) IOClient.sendRequest(host, port, request);
        return response.getData();
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }


}
