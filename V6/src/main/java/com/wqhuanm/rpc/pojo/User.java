package com.wqhuanm.rpc.pojo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User implements Serializable {
    private  Integer userId;
    private String userName;
    private String address;
    private Boolean sex;
}
