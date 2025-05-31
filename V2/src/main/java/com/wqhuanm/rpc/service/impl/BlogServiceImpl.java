package com.wqhuanm.rpc.service.impl;

import com.wqhuanm.rpc.pojo.Blog;
import com.wqhuanm.rpc.service.BlogService;

public class BlogServiceImpl implements BlogService {
    @Override
    public int addBlog(Blog blog) {
        System.out.println("新增blog，id为" + blog.getBlogId());
        return 1;
    }
}
