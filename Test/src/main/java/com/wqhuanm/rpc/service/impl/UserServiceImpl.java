package com.wqhuanm.rpc.service.impl;

import com.wqhuanm.rpc.dao.UserDao;
import com.wqhuanm.rpc.pojo.User;
import com.wqhuanm.rpc.service.UserService;

import java.util.UUID;

public class UserServiceImpl implements UserService
{
    private UserDao userDao;

    public User getUserById(int userId)
    {
        if(!allow())return  null;
        User user = userDao.getUser(userId);
        return user;
    }

    public boolean allow(){
        return  false;
    }

}
