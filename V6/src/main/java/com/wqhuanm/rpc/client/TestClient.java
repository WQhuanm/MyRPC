package com.wqhuanm.rpc.client;

import com.wqhuanm.rpc.pojo.Blog;
import com.wqhuanm.rpc.pojo.User;
import com.wqhuanm.rpc.service.BlogService;
import com.wqhuanm.rpc.service.UserService;

public class TestClient {

    public static void main(String[] args) {
        NettyRPCClient client = new NettyRPCClient();
        ServiceProxy proxy = new ServiceProxy(client);
        UserService userService = proxy.getProxy(UserService.class);
        User user = userService.getUserById(666);
        System.out.println("从服务端得到的user为：" + user );
        System.out.println();
        User addUser = User.builder().userId(123).userName("zhang").address("asSAS").sex(false).build();
        boolean b = userService.addUser(addUser);
        System.out.println("向服务端插入数据：" + b);
        System.out.println();

        BlogService blogService = proxy.getProxy(BlogService.class);
        Blog blog = Blog.builder().blogId(233).title("ciallo").text("ccccccccccqw").build();
        int i = blogService.addBlog(blog);
        System.out.println(i);
    }
}
