package com.wqhuanm.rpc.common;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Request implements Serializable {

    private String interfaceName;
    private String methodName;
    private Object[] params;
    private Class<?>[] paramTypes;
}
