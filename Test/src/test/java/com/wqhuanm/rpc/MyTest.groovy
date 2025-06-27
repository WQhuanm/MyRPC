package com.wqhuanm.rpc

import com.wqhuanm.rpc.dao.UserDao
import com.wqhuanm.rpc.service.impl.UserServiceImpl
import spock.lang.Specification

class test extends Specification {

    def dao = Mock(new UserDao())
    def service = Spy(UserServiceImpl) //groovy可以指定使用哪些字段来构造类

    def "这是测试函数的名称"() {
        // 测试函数的结构总体是given-when-then,中间可穿插and标签补充
        given: "构造输入条件"

        and: "可以在这里模拟函数"

        when: "测试函数的调用"

        then: "对测试结果的验证"
        with(response) {
            postCode == postCodeResult
            abbreviation == abbreviationResult
        }
//
//        where: "可以很方便的为上面使用过的变量提供赋值"
//        //第一行是列名，上面使用过的变量，一般每个列用|隔开，输入输出变量间用||隔开，即左边是输入值，右边是输出值
//        id | students                      || postCodeResult | abbreviationResult
//        1  | getStudent(1, "张三", "北京") || "100000"       | "京"
//        2  | getStudent(2, "李四", "上海") || "200000"       | "沪"
    }

}
