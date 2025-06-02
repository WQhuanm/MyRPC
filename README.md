

### docker 部署zookeeper
```shell
docker pull zookeeper
docker run -d --name zookeeper -p 2181:2181  -v zookeeper_data:/var/lib/zookeeper zookeeper:latest
```

./bin/zkCli.sh  进入客户端




