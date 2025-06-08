package com.wqhuanm.rpc.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class Response implements Serializable {

    private int code;
    private String msg;
    private Object data;
    // 更新,这里我们需要加入这个，不然用其它序列化方式（除了java Serialize）得不到data的type
    private Class<?> dataType;

    public static Response success(Object data) {
        return Response.builder().code(200).data(data).dataType(data.getClass())
                .build();
    }

    public static Response fail() {
        return Response.builder().code(500).msg("服务器错误").build();
    }


}
