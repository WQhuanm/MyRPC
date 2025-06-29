package com.wqhuanm.rpc

import com.wqhuanm.rpc.dao.UserDao
import com.wqhuanm.rpc.pojo.User
import com.wqhuanm.rpc.service.impl.UserServiceImpl
import spock.lang.Specification

class test extends Specification //使用Spock需要继承的类
{
    def dao = Mock(UserDao)
    def spy = Spy(new UserServiceImpl(userDao: dao)) //groovy可以指定使用哪些字段来构造类


}
