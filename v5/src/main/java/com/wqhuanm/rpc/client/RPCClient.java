package com.wqhuanm.rpc.client;


import com.wqhuanm.rpc.common.Request;
import com.wqhuanm.rpc.common.Response;

public interface RPCClient {

    Response sendRequest(Request request);
}
