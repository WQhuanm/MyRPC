package com.wqhuanm.rpc.common;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Response implements Serializable {

    private int code;
    private String msg;
    private Object data;

    public static Response success(Object data) {
        return Response.builder().code(200).data(data).build();
    }

    public static Response fail() {
        return Response.builder().code(500).msg("服务器错误").build();
    }


}
