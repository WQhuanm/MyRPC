package com.wqhuanm.rpc.service.impl;

import com.wqhuanm.rpc.pojo.User;
import com.wqhuanm.rpc.service.UserService;

import java.util.Random;
import java.util.UUID;

public class UserServiceImpl implements UserService {


    @Override
    public User getUserById(int id) {
        Random rd = new Random();
        System.out.println("客户端查询了" + id + "的用户");
        User user = User.builder()
                .userId(rd.nextInt(100)).userName(UUID.randomUUID().toString())
                .address(UUID.randomUUID().toString()).sex(false).build();
        return user;
    }

    @Override
    public boolean addUser(User user) {
        return true;
    }
}
