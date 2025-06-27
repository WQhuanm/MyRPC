package com.wqhuanm.rpc.service.impl;

import com.wqhuanm.rpc.dao.UserDao;
import com.wqhuanm.rpc.pojo.User;
import com.wqhuanm.rpc.service.UserService;

import java.util.UUID;

public class UserServiceImpl implements UserService
{
    private UserDao userDao;

    public UserServiceImpl(UserDao userDao)
    {
        this.userDao = userDao;
    }
    public User getUserById()
    {
        int userId = userDao.getUserId(123);
        User user = User.builder()
                .userId(userId).userName(UUID.randomUUID().toString())
                .address(UUID.randomUUID().toString()).sex(false).build();
        return user;
    }

}
