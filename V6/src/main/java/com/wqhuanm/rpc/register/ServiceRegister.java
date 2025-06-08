package com.wqhuanm.rpc.register;

import java.net.InetSocketAddress;

public interface ServiceRegister {
    void register(String serviceName, InetSocketAddress address);

    InetSocketAddress discover(String serviceName);


}
