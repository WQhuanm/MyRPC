package com.wqhuanm.rpc.server;

import com.wqhuanm.rpc.register.ServiceRegister;
import com.wqhuanm.rpc.register.ZKServiceRegister;
import lombok.Data;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.HashMap;


@Data
public class ServiceProvider {

    private static final String SCAN_PATH = "/com/wqhuanm/rpc/service/impl";
    private static final String CLASS_PREFIX = "com.wqhuanm.rpc.service.impl.";
    private HashMap<String, Object> mp = new HashMap<>();
    private ServiceRegister register = new ZKServiceRegister();
    private String host;
    private int port;

    public ServiceProvider(String host, int port) {
        this.host = host;
        this.port = port;
        registerService();
    }

    public Object getService(String name) {
        return mp.get(name);
    }

    private void registerService() {
        File directory = new File(ServiceProvider.class.getResource(SCAN_PATH).getFile());
        File[] files = directory.listFiles();

        for (var file : files) {
            try {
                String className = CLASS_PREFIX + file.getName().substring(0, file.getName().lastIndexOf("."));
                Class<?> clazz = Class.forName(className);
                Class<?>[] interfaces = clazz.getInterfaces();
                for (var u : interfaces) {
                    mp.put(u.getName(), clazz.getDeclaredConstructor().newInstance());
                    register.register(u.getName(), new InetSocketAddress(host, port));
                }
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
