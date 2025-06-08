package com.wqhuanm.rpc.register;

import com.wqhuanm.rpc.loadbalance.LoadBalance;
import com.wqhuanm.rpc.loadbalance.RandomLoadBalance;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;

public class ZKServiceRegister implements ServiceRegister {

    private CuratorFramework client;
    private static final String ROOT_PATH = "MyRPC";

    private LoadBalance loadBalance = new RandomLoadBalance();

    public ZKServiceRegister() {
        this.client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(4000).retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace(ROOT_PATH).build();
        client.start();
        System.out.println("ZOOKEEPER连接成功");
    }

    @Override
    public void register(String serviceName, InetSocketAddress address) {
        try {
            if (client.checkExists().forPath("/" + serviceName) == null) {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                        .forPath("/" + serviceName);
            }
            String path = "/" + serviceName + "/" + getServiceAddress(address);
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InetSocketAddress discover(String serviceName) {

        try {
            List<String> list = client.getChildren().forPath("/" + serviceName);
            String s = loadBalance.balance(list);
            return parseAddress(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getServiceAddress(InetSocketAddress serverAddress) {
        return serverAddress.getHostName() + ":" + serverAddress.getPort();
    }

    private InetSocketAddress parseAddress(String address) {
        String[] result = address.split(":");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }
}
