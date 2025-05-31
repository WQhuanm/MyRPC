package com.wqhuanm.rpc.service;

import com.wqhuanm.rpc.pojo.User;

public interface UserService {
    User getUserById(int id);
    boolean addUser(User user);
}
